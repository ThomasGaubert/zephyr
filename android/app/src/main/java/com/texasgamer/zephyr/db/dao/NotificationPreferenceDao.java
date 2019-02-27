package com.texasgamer.zephyr.db.dao;

import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * DAO for notification preferences.
 */
@Dao
public interface NotificationPreferenceDao {
    @Query("SELECT * FROM notification_preferences ORDER BY title COLLATE NOCASE ASC")
    LiveData<List<NotificationPreferenceEntity>> loadNotificationPreferences();

    @Query("SELECT * FROM notification_preferences WHERE packageName = :packageName")
    LiveData<NotificationPreferenceEntity> loadNotificationPreference(String packageName);

    @Query("SELECT * FROM notification_preferences WHERE packageName = :packageName")
    NotificationPreferenceEntity loadNotificationPreferenceSync(String packageName);

    @Query("UPDATE notification_preferences SET enabled = 1")
    void enableAll();

    @Query("UPDATE notification_preferences SET enabled = 0")
    void disableAll();

    @Query("UPDATE notification_preferences SET enabled = :enabled WHERE packageName = :packageName")
    void updateNotificationPreference(String packageName, boolean enabled);

    @Query("DELETE FROM notification_preferences WHERE packageName NOT IN (:installedPackageNames)")
    void removeOrphanedNotificationPreferences(String[] installedPackageNames);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNotificationPreferences(List<NotificationPreferenceEntity> notificationPreferences);
}
