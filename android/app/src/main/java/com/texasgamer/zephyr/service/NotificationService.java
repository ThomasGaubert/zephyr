package com.texasgamer.zephyr.service;

import android.app.Notification;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.db.repository.NotificationPreferenceRepository;
import com.texasgamer.zephyr.model.DismissNotificationPayload;
import com.texasgamer.zephyr.model.NotificationPayload;
import com.texasgamer.zephyr.model.NotificationPreference;
import com.texasgamer.zephyr.service.threading.ZephyrExecutors;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import androidx.annotation.NonNull;

/**
 * Service which listens for and processes notifications.
 */
public class NotificationService extends NotificationListenerService {

    private static final String LOG_TAG = "NotificationService";

    @Inject
    ILogger logger;
    @Inject
    ApplicationUtils appUtils;
    @Inject
    NotificationPreferenceRepository notificationPreferenceRepository;

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
        logger.log(LogPriority.VERBOSE, LOG_TAG, "onNotificationPosted: [%s]\t%s", sbn.getId(), sbn.getPackageName());
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            if (isValidNotification(sbn)) {
                NotificationPayload notificationPayload = new NotificationPayload();
                notificationPayload.packageName = sbn.getPackageName();
                notificationPayload.id = sbn.getId();
                notificationPayload.timestamp = sbn.getPostTime();
                notificationPayload.title = getNotificationTitle(sbn);
                notificationPayload.message = getNotificationMessage(sbn);

                logger.log(LogPriority.VERBOSE, LOG_TAG, "Notification: %s\t%s", notificationPayload.title, notificationPayload.message);
                EventBus.getDefault().post(notificationPayload);
            }
        });
    }

    @Override
    public void onNotificationRemoved(@NonNull StatusBarNotification sbn) {
        logger.log(LogPriority.VERBOSE, LOG_TAG, "onNotificationRemoved: [%s]\t%s", sbn.getId(), sbn.getPackageName());
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            if (isValidNotification(sbn)) {
                DismissNotificationPayload dismissNotificationPayload = new DismissNotificationPayload();
                dismissNotificationPayload.packageName = sbn.getPackageName();
                dismissNotificationPayload.id = sbn.getId();

                logger.log(LogPriority.VERBOSE, LOG_TAG, "Dismissing notification: %s", sbn.getId());
                EventBus.getDefault().post(dismissNotificationPayload);
            }
        });
    }

    private boolean isValidNotification(@NonNull StatusBarNotification sbn) {
        if (sbn.getPackageName().equals(BuildConfig.APPLICATION_ID)) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Invalid notification: Zephyr");
            return false;
        }

        if (!sbn.isClearable()) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Invalid notification: Not clearable");
            return false;
        }

        if (getPackageManager().getLaunchIntentForPackage(sbn.getPackageName()) == null) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Invalid notification: No launch intent");
            return false;
        }

        NotificationPreference notificationPreference = notificationPreferenceRepository.getNotificationPreferenceSync(sbn.getPackageName());
        if (notificationPreference == null) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Invalid notification: null preference");
            return false;
        }

        if (!notificationPreference.isEnabled()) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Invalid notification: Disabled");
            return false;
        }

        logger.log(LogPriority.DEBUG, LOG_TAG, "Valid notification.");
        return true;
    }

    @NonNull
    private String getNotificationTitle(@NonNull StatusBarNotification sbn) {
        return sbn.getNotification().extras.getString(Notification.EXTRA_TITLE, appUtils.getAppName(sbn.getPackageName()).toString());
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
