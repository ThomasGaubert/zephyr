package com.texasgamer.zephyr.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.room.Embedded;

import com.texasgamer.zephyr.db.entity.AppInfoEntity;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;

/**
 * Container for an app's metadata ({@link AppInfoEntity}) and associated notification preferences ({@link NotificationPreferenceEntity}).
 */
public class ZephyrNotificationPreference implements IAppInfo, INotificationPreference {

    @Embedded
    private NotificationPreferenceEntity mNotificationPreferenceEntity;

    @Embedded
    private AppInfoEntity mAppInfoEntity;

    @Override
    public String getPackageName() {
        return mAppInfoEntity.getPackageName();
    }

    @Override
    public String getTitle() {
        return mAppInfoEntity.getTitle();
    }

    @Override
    public int getColor() {
        return mAppInfoEntity.getColor();
    }

    @Nullable
    @Override
    public Drawable getIcon() {
        return mAppInfoEntity.getIcon();
    }

    @Override
    public void setIcon(@Nullable Drawable icon) {
        mAppInfoEntity.setIcon(icon);
    }

    @Override
    public boolean isEnabled() {
        return mNotificationPreferenceEntity.isEnabled();
    }

    public void setNotificationPreferenceEntity(NotificationPreferenceEntity notificationPreferenceEntity) {
        mNotificationPreferenceEntity = notificationPreferenceEntity;
    }

    public void setAppInfoEntity(AppInfoEntity appInfoEntity) {
        mAppInfoEntity = appInfoEntity;
    }
}
