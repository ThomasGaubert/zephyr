package com.texasgamer.zephyr.db.repository;

import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * Notification preference repository.
 */
public interface NotificationPreferenceRepository extends IRepository {
    @NonNull
    LiveData<List<NotificationPreferenceEntity>> getNotificationPreferences();

    @NonNull
    LiveData<List<NotificationPreferenceEntity>> getNotificationPreferencesByName(@NonNull String name);

    @NonNull
    LiveData<NotificationPreferenceEntity> getNotificationPreference(@NonNull String packageName);

    NotificationPreferenceEntity getNotificationPreferenceSync(@NonNull String packageName);

    void updateNotificationPreference(@NonNull String packageName, boolean enabled);

    void enableAll();

    void enableAll(@NonNull String name);

    void disableAll();

    void disableAll(@NonNull String name);
}
