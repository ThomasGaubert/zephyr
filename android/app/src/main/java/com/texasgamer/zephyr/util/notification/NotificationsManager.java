package com.texasgamer.zephyr.util.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import androidx.core.content.ContextCompat;

public class NotificationsManager {

    private static final String LOG_TAG = "NotificationsManager";

    private Context mContext;
    private ILogger mLogger;

    public NotificationsManager(Context context, ILogger logger) {
        mContext = context;
        mLogger = logger;
    }

    public void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLogger.log(LogPriority.INFO, LOG_TAG, "Creating notification channels...");
            // Status notification
            CharSequence name = mContext.getString(R.string.status_notif_channel_title);
            NotificationChannel channel = new NotificationChannel(ZephyrNotificationChannel.STATUS, name, NotificationManager.IMPORTANCE_LOW);
            channel.setDescription(mContext.getString(R.string.status_notif_channel_desc));
            channel.setLightColor(ContextCompat.getColor(mContext, R.color.primary));
            channel.setShowBadge(false);
            channel.setBypassDnd(false);

            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            mLogger.log(LogPriority.INFO, LOG_TAG, "Done creating notification channels.");
        }
    }
}
