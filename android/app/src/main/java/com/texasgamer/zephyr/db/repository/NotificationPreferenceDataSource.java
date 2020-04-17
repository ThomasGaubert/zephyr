package com.texasgamer.zephyr.db.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.texasgamer.zephyr.db.dao.NotificationPreferenceDao;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.util.threading.ZephyrExecutors;

import java.util.List;

import javax.inject.Inject;

/**
 * Notification preference data source.
 */
public class NotificationPreferenceDataSource implements NotificationPreferenceRepository {

    private NotificationPreferenceDao mNotificationPreferenceDao;

    @Inject
    public NotificationPreferenceDataSource(@NonNull NotificationPreferenceDao notificationPreferenceDao) {
        mNotificationPreferenceDao = notificationPreferenceDao;
    }

    @NonNull
    public LiveData<List<NotificationPreferenceEntity>> getNotificationPreferences() {
        return mNotificationPreferenceDao.loadNotificationPreferences();
    }

    @NonNull
    public LiveData<List<NotificationPreferenceEntity>> getNotificationPreferencesByName(@NonNull String name) {
        return mNotificationPreferenceDao.loadNotificationPreferencesByName("%" + name + "%");
    }

    @NonNull
    public NotificationPreferenceEntity getNotificationPreferenceSync(@NonNull String packageName) {
        return mNotificationPreferenceDao.loadNotificationPreferenceSync(packageName);
    }

    @NonNull
    public LiveData<NotificationPreferenceEntity> getNotificationPreference(@NonNull String packageName) {
        return mNotificationPreferenceDao.loadNotificationPreference(packageName);
    }

    public void updateNotificationPreference(@NonNull String packageName, boolean enabled) {
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            mNotificationPreferenceDao.updateNotificationPreference(packageName, enabled);
        });
    }

    public void enableAll() {
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            mNotificationPreferenceDao.enableAll();
        });
    }

    public void enableAll(@NonNull String name) {
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            mNotificationPreferenceDao.enableAll("%" + name + "%");
        });
    }

    public void disableAll() {
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            mNotificationPreferenceDao.disableAll();
        });
    }

    public void disableAll(@NonNull String name) {
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            mNotificationPreferenceDao.disableAll("%" + name + "%");
        });
    }
}
