package com.texasgamer.zephyr.util.log;

import android.util.Log;

import com.texasgamer.zephyr.Constants;

import androidx.annotation.NonNull;

public class Logger implements ILogger {
    @Override
    public void log(@LogPriority int priority, @NonNull String tag, @NonNull String message) {
        if (priority < Constants.MIN_LOG_LEVEL) {
            return;
        }

        switch (priority) {
            case LogPriority.VERBOSE:
                Log.v(tag, message);
                break;
            case LogPriority.DEBUG:
                Log.d(tag, message);
                break;
            case LogPriority.INFO:
                Log.i(tag, message);
                break;
            case LogPriority.WARNING:
                Log.w(tag, message);
                break;
            case LogPriority.ERROR:
                Log.e(tag, message);
                break;
        }
    }

    @Override
    public void log(@LogPriority int priority, @NonNull String tag, @NonNull String message, @NonNull Object... args) {
        log(priority, tag, String.format(message, args));
    }

    @Override
    public void log(@LogPriority int priority, @NonNull String tag, @NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        log(priority, tag, String.format(message, args) + '\n' + throwable.toString());
    }

    @Override
    public void log(@LogPriority int priority, @NonNull String tag, @NonNull Throwable throwable) {
        log(priority, tag, throwable.toString());
    }
}
