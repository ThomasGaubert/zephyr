package com.texasgamer.zephyr.util.theme;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef(value = {Theme.SYSTEM_DEFAULT, Theme.LIGHT, Theme.DARK})
@Retention(RetentionPolicy.SOURCE)
public @interface Theme {
    int SYSTEM_DEFAULT = 0;
    int LIGHT = 1;
    int DARK = 2;
}
