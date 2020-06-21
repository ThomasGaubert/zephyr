package com.texasgamer.zephyr.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.model.ZephyrNotificationPreference;

import java.util.List;

/**
 * DAO for {@link NotificationPreferenceEntity}.
 */
@Dao
public interface NotificationPreferenceDao {
    @Query("SELECT * FROM notification_preferences AS pref_ JOIN app_info AS app_ ON (pref_.packageName = app_.packageName) "
            + "ORDER BY app_.title COLLATE NOCASE ASC")
    LiveData<List<ZephyrNotificationPreference>> loadNotificationPreferences();

    @Query("SELECT * FROM notification_preferences AS pref_ JOIN app_info AS app_ ON (pref_.packageName = app_.packageName) "
            + "WHERE app_.title LIKE :name "
            + "ORDER BY app_.title COLLATE NOCASE ASC")
    LiveData<List<ZephyrNotificationPreference>> loadNotificationPreferencesByName(String name);

    @Query("SELECT * FROM notification_preferences WHERE packageName = :packageName")
    LiveData<NotificationPreferenceEntity> loadNotificationPreference(String packageName);

    @Query("SELECT * FROM notification_preferences WHERE packageName = :packageName")
    NotificationPreferenceEntity loadNotificationPreferenceSync(String packageName);

    @Query("UPDATE notification_preferences SET enabled = 1")
    void enableAll();

    @Query("UPDATE notification_preferences SET enabled = 1 "
            + "WHERE packageName IN (SELECT packageName FROM app_info AS app_ WHERE app_.title LIKE :name)")
    void enableAll(String name);

    @Query("UPDATE notification_preferences SET enabled = 0")
    void disableAll();

    @Query("UPDATE notification_preferences SET enabled = 0 "
            + "WHERE packageName IN (SELECT packageName FROM app_info AS app_ WHERE app_.title LIKE :name)")
    void disableAll(String name);

    @Query("UPDATE notification_preferences SET enabled = :enabled WHERE packageName = :packageName")
    void updateNotificationPreference(String packageName, boolean enabled);

    @Query("DELETE FROM notification_preferences WHERE packageName NOT IN (:installedPackageNames)")
    void removeOrphanedNotificationPreferences(String... installedPackageNames);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNotificationPreferences(List<NotificationPreferenceEntity> notificationPreferences);
}
