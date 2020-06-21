package com.texasgamer.zephyr.db.entity;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.texasgamer.zephyr.model.AppInfo;

/**
 * Represents metadata for apps installed on the system.
 */
@Entity(tableName = "app_info", indices = {@Index(value = "packageName", unique = true)})
public class AppInfoEntity extends AppInfo {
    @PrimaryKey
    @NonNull
    private String packageName;
    private String title;
    @ColorInt
    private int color;

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

    public AppInfoEntity() {

    }

    @Ignore
    public AppInfoEntity(@NonNull String packageName, @NonNull String title, @ColorInt int color) {
        this.packageName = packageName;
        this.title = title;
        this.color = color;
    }
}
