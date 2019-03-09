package com.texasgamer.zephyr.util.log;

import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef(value = {LogPriority.VERBOSE, LogPriority.DEBUG, LogPriority.INFO, LogPriority.WARNING, LogPriority.ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface LogPriority {
    int VERBOSE = Log.VERBOSE;
    int DEBUG = Log.DEBUG;
    int INFO = Log.INFO;
    int WARNING = Log.WARN;
    int ERROR = Log.ERROR;
}
