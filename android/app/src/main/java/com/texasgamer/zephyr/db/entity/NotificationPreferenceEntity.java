package com.texasgamer.zephyr.db.entity;

import com.texasgamer.zephyr.model.NotificationPreference;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification_preferences", indices = {@Index(value = "packageName", unique = true)})
public class NotificationPreferenceEntity implements NotificationPreference {

    @PrimaryKey
    @NonNull
    private String packageName;
    private String title;
    @ColorInt
    private int color;
    private boolean enabled;

    @NonNull
    @Override
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(@NonNull String packageName) {
        this.packageName = packageName;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @ColorInt
    public int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
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
    public NotificationPreferenceEntity(@NonNull String packageName, @NonNull String title, @ColorInt int color, boolean enabled) {
        this.packageName = packageName;
        this.title = title;
        this.color = color;
        this.enabled = enabled;
    }
}
