package sugar.free.telesto.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.DecimalFormat;
import java.util.Date;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import sugar.free.telesto.R;
import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.activities.LauncherActivity;
import sugar.free.telesto.descriptors.ActiveBasalRate;
import sugar.free.telesto.descriptors.ActiveTBR;
import sugar.free.telesto.descriptors.BatteryStatus;
import sugar.free.telesto.descriptors.CartridgeStatus;
import sugar.free.telesto.descriptors.OperatingMode;

public class NotificationUtil {

    public static final int STATUS_NOTIFICATION_ID = 1;
    public static final int CRITICAL_ERROR_NOTIFICATION_ID = 2;

    private static final String STATUS_NOTIFICATION_CHANNEL_ID = "STATUS";
    private static final String CRITICAL_ERROR_NOTIFICATION_CHANNEL_ID = "CRITICAL_ERROR";

    public static void setupNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            NotificationChannel statusChannel = new NotificationChannel(STATUS_NOTIFICATION_CHANNEL_ID,
                    TelestoApp.getInstance().getString(R.string.status), NotificationManager.IMPORTANCE_LOW);
            statusChannel.setDescription(context.getString(R.string.shows_the_latest_known_status_of_your_pump));
            statusChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(statusChannel);

            NotificationChannel criticalErrorChannel = new NotificationChannel(CRITICAL_ERROR_NOTIFICATION_CHANNEL_ID,
                    TelestoApp.getInstance().getString(R.string.critical_error), NotificationManager.IMPORTANCE_HIGH);
            criticalErrorChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            criticalErrorChannel.setShowBadge(true);
            criticalErrorChannel.setDescription(TelestoApp.getInstance().getString(R.string.critical_error_description));
            criticalErrorChannel.setLightColor(0xFFFF0000);
            criticalErrorChannel.setBypassDnd(true);
            criticalErrorChannel.enableLights(true);
            criticalErrorChannel.setVibrationPattern(new long[] {0, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200});
            notificationManager.createNotificationChannel(criticalErrorChannel);
        }
    }

    public static void showCriticalErrorNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CRITICAL_ERROR_NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setContentTitle(context.getString(R.string.critical_error));
        builder.setContentText(context.getString(R.string.critical_error_occurred));
        builder.setLights(0xFFFF0000, 500, 500);
        builder.setVibrate(new long[] {0, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200});
        Intent intent = new Intent(context, LauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        Notification notification = builder.build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(CRITICAL_ERROR_NOTIFICATION_ID, notification);
    }

    public static Notification showStatusNotification(Context context, boolean connected, Date lastUpdated, OperatingMode operatingMode, BatteryStatus batteryStatus,
                                                      CartridgeStatus cartridgeStatus, ActiveBasalRate activeBasalRate, ActiveTBR activeTBR) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, STATUS_NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setPriority(NotificationCompat.PRIORITY_LOW);
        builder.setOngoing(true);
        builder.setColor(ContextCompat.getColor(context, connected ? R.color.colorConnected : R.color.colorDisconnected));
        Intent intent = new Intent(context, LauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        if (lastUpdated != null) {
            builder.setWhen(lastUpdated.getTime());
            switch (operatingMode) {
                case STARTED:
                    StringBuilder stringBuilder = new StringBuilder(context.getString(R.string.started));
                    if (activeBasalRate != null) {
                        stringBuilder.append(": ");
                        stringBuilder.append(new DecimalFormat("#0.00").format(activeTBR == null ? activeBasalRate.getActiveBasalRate() : activeBasalRate.getActiveBasalRate() / 100D * ((double) activeTBR.getPercentage())));
                        stringBuilder.append(" U/h");
                        if (activeTBR != null) {
                            stringBuilder.append(" ");
                            stringBuilder.append(activeTBR.getPercentage());
                            stringBuilder.append("%");
                        }
                        stringBuilder.append(" (");
                        stringBuilder.append(activeBasalRate.getActiveBasalProfileName());
                        stringBuilder.append(")");
                    }
                    builder.setContentTitle(stringBuilder.toString());
                    break;
                case STOPPED:
                    builder.setContentTitle(context.getString(R.string.stopped));
                    break;
                case PAUSED:
                    builder.setContentTitle(context.getString(R.string.paused));
                    break;
            }
            StringBuilder stringBuilder = new StringBuilder();
            if (batteryStatus != null) {
                stringBuilder.append(context.getString(R.string.battery));
                stringBuilder.append(": ");
                stringBuilder.append(batteryStatus.getBatteryAmount());
                stringBuilder.append("%");
            }
            if (cartridgeStatus != null) {
                if (batteryStatus != null) stringBuilder.append("  ");
                stringBuilder.append(context.getString(R.string.cartridge));
                stringBuilder.append(": ");
                if (cartridgeStatus.isInserted()) {
                    stringBuilder.append((int) Math.ceil(cartridgeStatus.getRemainingAmount()));
                    stringBuilder.append("U");
                } else stringBuilder.append(context.getString(R.string.not_inserted));
            }
            builder.setContentText(stringBuilder.toString());
        } else {
            builder.setShowWhen(false);
            builder.setContentTitle(context.getString(R.string.pump_status_unknown));
        }
        Notification notification = builder.build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(STATUS_NOTIFICATION_ID, notification);
        return notification;
    }

}
