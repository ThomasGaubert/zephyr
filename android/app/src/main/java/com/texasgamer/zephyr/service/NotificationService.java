package com.texasgamer.zephyr.service;

import android.app.Notification;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.model.NotificationPayload;
import com.texasgamer.zephyr.provider.AppProvider;
import com.texasgamer.zephyr.util.NotificationPreferenceManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import androidx.annotation.NonNull;

public class NotificationService extends NotificationListenerService {

    private static final String LOG_TAG = "NotificationService";

    @Inject
    ILogger logger;
    @Inject
    AppProvider appProvider;
    @Inject
    NotificationPreferenceManager notificationPreferenceManager;

    @Override
    public void onCreate() {
        ZephyrApplication.getApplicationComponent().inject(this);
        super.onCreate();

        logger.log(LogPriority.VERBOSE, LOG_TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(@NonNull StatusBarNotification sbn) {
        logger.log(LogPriority.DEBUG, LOG_TAG, "onNotificationPosted: [%s]\t%s", sbn.getId(), sbn.getPackageName());
        if (isValidNotification(sbn)) {
            NotificationPayload notificationPayload = new NotificationPayload();
            notificationPayload.title = getNotificationTitle(sbn);
            notificationPayload.message = getNotificationMessage(sbn);
            notificationPayload.id = sbn.getId();

            logger.log(LogPriority.DEBUG, LOG_TAG, "Notification: %s\t%s", notificationPayload.title, notificationPayload.message);
            EventBus.getDefault().post(notificationPayload);
        }
    }

    @Override
    public void onNotificationRemoved(@NonNull StatusBarNotification sbn) {
        logger.log(LogPriority.DEBUG, LOG_TAG, "onNotificationRemoved: [%s]\t%s", sbn.getId(), sbn.getPackageName());
    }

    private boolean isValidNotification(@NonNull StatusBarNotification sbn) {
        if (!sbn.isClearable()) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Invalid notification: Not clearable");
            return false;
        }

        if (getPackageManager().getLaunchIntentForPackage(sbn.getPackageName()) == null) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Invalid notification: No launch intent");
            return false;
        }

        if (sbn.getPackageName().equals(BuildConfig.APPLICATION_ID)) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Invalid notification: Zephyr");
            return false;
        }

//        if (!notificationPreferenceManager.getNotificationPreference(sbn.getPackageName()).enabled) {
//            logger.log(LogPriority.DEBUG, LOG_TAG, "Invalid notification: Disabled");
//            return false;
//        }

        logger.log(LogPriority.DEBUG, LOG_TAG, "Valid notification.");
        return true;
    }

    @NonNull
    private String getNotificationTitle(@NonNull StatusBarNotification sbn) {
        return sbn.getNotification().extras.getString(Notification.EXTRA_TITLE, appProvider.getAppName(sbn.getPackageName()).toString());
    }

    @NonNull
    private String getNotificationMessage(@NonNull StatusBarNotification sbn) {
        CharSequence result = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT);
        if (result != null) {
            return result.toString();
        }

        result = sbn.getNotification().tickerText;
        if (result != null) {
            return result.toString();
        } else {
            return "";
        }
    }
}
