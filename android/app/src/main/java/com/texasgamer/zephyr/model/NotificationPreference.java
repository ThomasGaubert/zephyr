package com.texasgamer.zephyr.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

/**
 * Notification preference.
 */
public interface NotificationPreference {

    String getPackageName();

    String getTitle();

    @ColorInt
    int getColor();

    boolean isEnabled();

    @Nullable
    Drawable getIcon();

    void setIcon(@Nullable Drawable icon);
}
