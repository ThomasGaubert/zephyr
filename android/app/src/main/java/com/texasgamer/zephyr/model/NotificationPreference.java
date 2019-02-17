package com.texasgamer.zephyr.model;

import androidx.annotation.ColorInt;

public interface NotificationPreference {

    String getPackageName();

    String getTitle();

    @ColorInt
    int getColor();

    boolean getEnabled();
}
