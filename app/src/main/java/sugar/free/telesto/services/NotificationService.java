package sugar.free.telesto.services;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;

import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.descriptors.TelestoState;
import sugar.free.telesto.services.connection_service.ConnectionService;
import sugar.free.telesto.utils.NotificationUtil;

public class NotificationService extends LifecycleService implements TelestoApp.InitializationCompletedCallback, Observer<Object>, ConnectionService.StateCallback {

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TelestoApp.awaitCompletedInitialization(this);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public void onInitializationCompleted() {
        startForeground(NotificationUtil.STATUS_NOTIFICATION_ID, showNotification());
        TelestoApp.getStatusService().getLastUpdated().observe(this, this);
        TelestoApp.getConnectionService().registerStateCallback(this);
    }

    @Override
    public void onChanged(Object object) {
        showNotification();
    }

    private Notification showNotification() {
        StatusService statusService = TelestoApp.getStatusService();
        ConnectionService connectionService = TelestoApp.getConnectionService();
        return NotificationUtil.showStatusNotification(this, connectionService.getState() == TelestoState.CONNECTED, statusService.getLastUpdated().getValue(), statusService.getOperatingMode().getValue(), statusService.getBatteryStatus().getValue(),
                statusService.getCartridgeStatus().getValue(), statusService.getActiveBasalRate().getValue(), statusService.getActiveTBR().getValue());
    }

    @Override
    public void stateChanged(TelestoState state) {
        showNotification();
    }
}
