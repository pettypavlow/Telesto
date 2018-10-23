package sugar.free.telesto.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.List;

import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.activities.AlertActivity;
import sugar.free.telesto.descriptors.Alert;
import sugar.free.telesto.descriptors.AlertStatus;
import sugar.free.telesto.descriptors.TelestoState;
import sugar.free.telesto.services.connection_service.ConnectionService;
import sugar.free.telesto.utils.MessageRequestUtil;

public class AlertService extends Service implements TelestoApp.InitializationCompletedCallback, ConnectionService.StateCallback {

    private final List<ActiveAlertCallback> alertCallbacks = new ArrayList<>();
    private boolean connectionRequested;
    private Object $alertLock = new Object[0];
    private Alert alert;
    private Thread thread;
    private AlertActivity alertActivity;
    private Ringtone ringtone;
    private Vibrator vibrator;
    private boolean vibrating;

    private LocalBinder localBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        TelestoApp.awaitCompletedInitialization(this);
    }

    private void retrieveRingtone() {
        String selectedTone = TelestoApp.getSharedPreferences().getString("alertRingtone", null);
        Uri uri = selectedTone == null ? RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE) : Uri.parse(selectedTone);
        ringtone = RingtoneManager.getRingtone(this, uri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ringtone.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                    .setLegacyStreamType(AudioManager.STREAM_RING).build());
        } else ringtone.setStreamType(AudioManager.STREAM_RING);
    }

    public Alert getAlert() {
        synchronized ($alertLock) {
            return alert;
        }
    }

    public void registerAlertCallback(ActiveAlertCallback callback) {
        synchronized (alertCallbacks) {
            alertCallbacks.add(callback);
        }
    }

    public void unregisterAlertCallback(ActiveAlertCallback callback) {
        synchronized (alertCallbacks) {
            alertCallbacks.remove(callback);
        }
    }

    public void setAlertActivity(AlertActivity alertActivity) {
        this.alertActivity = alertActivity;
    }

    @Override
    public void onInitializationCompleted() {
        TelestoApp.getConnectionService().registerStateCallback(this);
        stateChanged(TelestoApp.getConnectionService().getState());
    }

    @Override
    public void stateChanged(TelestoState state) {
        if (state != TelestoState.CONNECTED) {
            if (thread != null) thread.interrupt();
        } else {
            thread = new Thread(this::queryActiveAlert);
            thread.start();
        }
    }

    private void stopAlerting() {
        if (vibrating) {
            vibrator.cancel();
            vibrating = false;
        }
        if (ringtone != null && ringtone.isPlaying()) ringtone.stop();
    }

    private void alert() {
        if (!vibrating) {
            vibrator.vibrate(new long[] {0, 1000, 1000}, 0);
            vibrating = true;
        }
        if (ringtone == null || !ringtone.isPlaying()) {
            retrieveRingtone();
            ringtone.play();
        }
    }

    private void queryActiveAlert() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Alert alert = MessageRequestUtil.getActiveAlert();
                if (Thread.currentThread().isInterrupted()) break;
                synchronized ($alertLock) {
                    if ((this.alert == null && alert != null)
                            || (this.alert != null && alert == null)
                            || (this.alert != null && alert != null && !this.alert.equals(alert))) {
                        if (this.alert != null && (alert == null || this.alert.getAlertId() != alert.getAlertId())) stopAlerting();
                        this.alert = alert;
                        synchronized (alertCallbacks) {
                            for (ActiveAlertCallback callback : alertCallbacks)
                                callback.onAlertChange(alert);
                        }
                        if (alertActivity != null && alert != null)
                            new Handler(Looper.getMainLooper()).post(() -> alertActivity.update(alert));
                    }
                }
                if (alert == null) {
                    stopAlerting();
                    if (connectionRequested) {
                        TelestoApp.getConnectionService().withdrawConnectionRequest(this);
                        connectionRequested = false;
                    }
                    if (alertActivity != null)
                        new Handler(Looper.getMainLooper()).post(() -> alertActivity.finish());
                } else {
                    if (alert.getAlertStatus() == AlertStatus.ACTIVE) alert();
                    else stopAlerting();
                    if (!connectionRequested) {
                        TelestoApp.getConnectionService().requestConnection(this);
                        connectionRequested = true;
                    }
                    if (alertActivity == null) {
                        Intent intent = new Intent(AlertService.this, AlertActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        new Handler(Looper.getMainLooper()).post(() -> startActivity(intent));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
        if (connectionRequested) {
            TelestoApp.getConnectionService().withdrawConnectionRequest(this);
            connectionRequested = false;
        }
        if (alertActivity != null)
            new Handler(Looper.getMainLooper()).post(() -> alertActivity.finish());
        stopAlerting();
    }

    public interface ActiveAlertCallback {
        void onAlertChange(Alert alert);
    }

    public class LocalBinder extends Binder {
        public AlertService getService() {
            return AlertService.this;
        }
    }
}
