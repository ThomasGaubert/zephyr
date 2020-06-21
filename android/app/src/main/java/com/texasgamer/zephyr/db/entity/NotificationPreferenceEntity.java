package com.texasgamer.zephyr.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.texasgamer.zephyr.model.INotificationPreference;

/**
 * Represents notification preference.
 */
@Entity(tableName = "notification_preferences", indices = {@Index(value = "packageName", unique = true)})
public class NotificationPreferenceEntity implements INotificationPreference {

    @PrimaryKey
    @NonNull
    private String packageName;
    private boolean enabled;

    @NonNull
    @Override
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(@NonNull String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public NotificationPreferenceEntity() {

    }

    @Ignore
    public NotificationPreferenceEntity(@NonNull String packageName, boolean enabled) {
        this.packageName = packageName;
        this.enabled = enabled;
    }
}
