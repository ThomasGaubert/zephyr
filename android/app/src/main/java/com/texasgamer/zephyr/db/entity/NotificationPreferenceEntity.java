package com.texasgamer.zephyr.db.entity;

import com.texasgamer.zephyr.model.NotificationPreference;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification_preferences", indices = {@Index(value = "packageName")})
public class NotificationPreferenceEntity implements NotificationPreference {

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
    public boolean getEnabled() {
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
