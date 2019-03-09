package com.texasgamer.zephyr.util.log;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.util.config.IConfigManager;

import androidx.annotation.NonNull;

/**
 * Logger.
 */
public class Logger implements ILogger {

    private IConfigManager mConfigManager;
    private ILogSanitizer mLogSanitizer;

    public Logger(@NonNull IConfigManager configManager, @NonNull ILogSanitizer logSanitizer) {
        this.mConfigManager = configManager;
        this.mLogSanitizer = logSanitizer;
    }

    @Override
    public void log(@LogPriority int priority, @NonNull String tag, @NonNull String message) {
        if (priority < Constants.MIN_LOG_LEVEL || !Log.isLoggable(tag, priority)) {
            return;
        }

        switch (priority) {
            case LogPriority.VERBOSE:
                Log.v(tag, mLogSanitizer.sanitize(message));
                break;
            case LogPriority.DEBUG:
                Log.d(tag, mLogSanitizer.sanitize(message));
                break;
            case LogPriority.INFO:
                Log.i(tag, mLogSanitizer.sanitize(message));
                break;
            case LogPriority.WARNING:
                Log.w(tag, mLogSanitizer.sanitize(message));
                break;
            case LogPriority.ERROR:
                Log.e(tag, mLogSanitizer.sanitize(message));
                break;
            default:
                Log.v(tag, mLogSanitizer.sanitize(message));
        }

        if (mConfigManager.isFirebaseCrashlyticsEnabled()) {
            logToCrashlytics(priority, tag, message);
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

    private void logToCrashlytics(@LogPriority int priority, @NonNull String tag, @NonNull String message) {
        if (priority < Constants.MIN_LOG_LEVEL_CRASHLYTICS) {
            return;
        }

        switch (priority) {
            case LogPriority.VERBOSE:
                Crashlytics.log(String.format("V/%s: %s", tag, message));
                break;
            case LogPriority.DEBUG:
                Crashlytics.log(String.format("D/%s: %s", tag, message));
                break;
            case LogPriority.INFO:
                Crashlytics.log(String.format("I/%s: %s", tag, message));
                break;
            case LogPriority.WARNING:
                Crashlytics.log(String.format("W/%s: %s", tag, message));
                break;
            case LogPriority.ERROR:
                Crashlytics.log(String.format("E/%s: %s", tag, message));
                break;
            default:
                Crashlytics.log(String.format("?/%s: %s", tag, message));
        }
    }
}
