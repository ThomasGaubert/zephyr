package com.texasgamer.zephyr.db.repository;

import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public interface NotificationPreferenceRepository extends IRepository {
    @NonNull
    LiveData<List<NotificationPreferenceEntity>> getNotificationPreferences();

    @NonNull
    LiveData<NotificationPreferenceEntity> getNotificationPreference(@NonNull String packageName);

    NotificationPreferenceEntity getNotificationPreferenceSync(@NonNull String packageName);

    void updateNotificationPreference(@NonNull String packageName, boolean enabled);

    void enableAll();

    void disableAll();
}