package com.texasgamer.zephyr.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.room.Ignore;

/**
 * Represents metadata for apps installed on the system.
 */
public abstract class AppInfo implements IAppInfo {

    @Ignore
    @Nullable
    private Drawable mIcon;

    @Nullable
    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(@Nullable Drawable icon) {
        mIcon = icon;
    }
}
