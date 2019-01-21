package com.texasgamer.zephyr.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({ZephyrCardType.DEFAULT,
        ZephyrCardType.INFO,
        ZephyrCardType.SUCCESS,
        ZephyrCardType.WARNING,
        ZephyrCardType.ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface ZephyrCardType {
    int DEFAULT = 0;
    int INFO = 1;
    int SUCCESS = 2;
    int WARNING = 3;
    int ERROR = 4;
}
