package com.texasgamer.zephyr.util.log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef(value = {LogPriority.VERBOSE, LogPriority.DEBUG, LogPriority.INFO, LogPriority.WARNING, LogPriority.ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface LogPriority {
    int VERBOSE = 1;
    int DEBUG = 2;
    int INFO = 3;
    int WARNING = 4;
    int ERROR = 5;
}
