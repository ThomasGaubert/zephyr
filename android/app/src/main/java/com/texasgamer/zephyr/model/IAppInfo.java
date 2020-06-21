package com.texasgamer.zephyr.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;

/**
 * Represents metadata for apps installed on the system.
 */
public interface IAppInfo {

    String getPackageName();

    String getTitle();

    Drawable getIcon();

    @ColorInt
    int getColor();

    void setIcon(Drawable icon);
}
