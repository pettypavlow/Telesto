package sugar.free.telesto;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import sugar.free.telesto.database.TelestoDatabase;
import sugar.free.telesto.services.AlertService;
import sugar.free.telesto.services.HistoryService;
import sugar.free.telesto.services.NotificationService;
import sugar.free.telesto.services.StatusService;
import sugar.free.telesto.services.connection_service.ConnectionService;
import sugar.free.telesto.utils.NotificationUtil;

public class TelestoApp extends Application {

    private static TelestoApp instance;
    private static boolean initializationCompleted;
    private static List<InitializationCompletedCallback> initializationCompletedCallbacks = Collections.synchronizedList(new ArrayList<>());
    private static SharedPreferences sharedPreferences;
    private static ConnectionService connectionService;
    private static StatusService statusService;
    private static TelestoDatabase database;
    private static HistoryService historyService;
    private static AlertService alertService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            if (binder instanceof ConnectionService.LocalBinder)
                connectionService = ((ConnectionService.LocalBinder) binder).getService();
            else if (binder instanceof StatusService.LocalBinder)
                statusService = ((StatusService.LocalBinder) binder).getService();
            else if (binder instanceof HistoryService.LocalBinder)
                historyService = ((HistoryService.LocalBinder) binder).getService();
            else if (binder instanceof AlertService.LocalBinder)
                alertService = ((AlertService.LocalBinder) binder).getService();
            if (connectionService == null || statusService == null || historyService == null || alertService == null)
                return;
            Log.d("TelestoApp", "Initialization complete");
            initializationCompleted = true;
            for (InitializationCompletedCallback callback : initializationCompletedCallbacks)
                callback.onInitializationCompleted();
            initializationCompletedCallbacks = null;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public static void awaitCompletedInitialization(InitializationCompletedCallback callback) {
        if (initializationCompleted) callback.onInitializationCompleted();
        else initializationCompletedCallbacks.add(callback);
    }

    public static TelestoApp getInstance() {
        return TelestoApp.instance;
    }

    public static boolean isInitializationCompleted() {
        return TelestoApp.initializationCompleted;
    }

    public static SharedPreferences getSharedPreferences() {
        return TelestoApp.sharedPreferences;
    }

    public static ConnectionService getConnectionService() {
        return TelestoApp.connectionService;
    }

    public static StatusService getStatusService() {
        return TelestoApp.statusService;
    }

    public static AlertService getAlertService() {
        return alertService;
    }

    public static TelestoDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        database = TelestoDatabase.instantiate(this);
        NotificationUtil.setupNotificationChannels(this);
        startService(new Intent(this, ConnectionService.class));
        startService(new Intent(this, StatusService.class));
        ContextCompat.startForegroundService(this, new Intent(this, NotificationService.class));
        bindService(new Intent(this, ConnectionService.class), serviceConnection, BIND_AUTO_CREATE | BIND_IMPORTANT);
        bindService(new Intent(this, StatusService.class), serviceConnection, BIND_AUTO_CREATE | BIND_IMPORTANT);
        bindService(new Intent(this, HistoryService.class), serviceConnection, BIND_AUTO_CREATE | BIND_IMPORTANT);
        bindService(new Intent(this, AlertService.class), serviceConnection, BIND_AUTO_CREATE | BIND_IMPORTANT);
    }

    public interface InitializationCompletedCallback {
        void onInitializationCompleted();
    }
}
