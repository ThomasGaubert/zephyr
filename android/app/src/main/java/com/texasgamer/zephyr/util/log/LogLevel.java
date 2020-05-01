package com.texasgamer.zephyr.util.log;

import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef(value = {LogLevel.VERBOSE, LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARNING, LogLevel.ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface LogLevel {
    int VERBOSE = Log.VERBOSE;
    int DEBUG = Log.DEBUG;
    int INFO = Log.INFO;
    int WARNING = Log.WARN;
    int ERROR = Log.ERROR;
}
