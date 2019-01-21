package com.texasgamer.zephyr.db.dao;

import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface NotificationPreferenceDao {
    @Query("SELECT * FROM notification_preferences")
    LiveData<List<NotificationPreferenceEntity>> loadNotificationPreferences();

    @Query("SELECT * FROM notification_preferences WHERE packageName = :packageName")
    LiveData<NotificationPreferenceEntity> loadNotificationPreference(@NonNull String packageName);

    @Query("UPDATE notification_preferences SET enabled = 1")
    void enableAll();

    @Query("UPDATE notification_preferences SET enabled = 0")
    void disableAll();

    @Query("DELETE FROM notification_preferences WHERE packageName NOT IN (:installedPackageNames)")
    void removeOrphanedNotificationPreferences(String[] installedPackageNames);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNotificationPreferences(List<NotificationPreferenceEntity> notificationPreferences);
}
