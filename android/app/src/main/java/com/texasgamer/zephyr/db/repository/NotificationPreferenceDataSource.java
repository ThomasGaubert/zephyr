package com.texasgamer.zephyr.db.repository;

import com.texasgamer.zephyr.db.dao.NotificationPreferenceDao;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.service.threading.ZephyrExecutors;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class NotificationPreferenceDataSource implements NotificationPreferenceRepository {

    private NotificationPreferenceDao notificationPreferenceDao;

    @Inject
    public NotificationPreferenceDataSource(@NonNull NotificationPreferenceDao notificationPreferenceDao) {
        this.notificationPreferenceDao = notificationPreferenceDao;
    }

    @NonNull
    public LiveData<List<NotificationPreferenceEntity>> getNotificationPreferences() {
        return notificationPreferenceDao.loadNotificationPreferences();
    }

    @NonNull
    public LiveData<NotificationPreferenceEntity> getNotificationPreference(@NonNull String packageName) {
        return notificationPreferenceDao.loadNotificationPreference(packageName);
    }

    public void enableAll() {
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            notificationPreferenceDao.enableAll();
        });
    }

    public void disableAll() {
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            notificationPreferenceDao.disableAll();
        });
    }
}
