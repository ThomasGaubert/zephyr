package com.texasgamer.zephyr.util.log;

import android.util.Log;

import androidx.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.privacy.IPrivacyManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Logger.
 */
public class Logger implements ILogger {

    private ILogSanitizer mLogSanitizer;
    private IPrivacyManager mPrivacyManager;
    private final Deque<LogEntry> mEntries = new ArrayDeque<>(Constants.LOG_BUFFER_SIZE + 1);

    public Logger(@NonNull ILogSanitizer logSanitizer, @NonNull IPrivacyManager privacyManager) {
        mLogSanitizer = logSanitizer;
        mPrivacyManager = privacyManager;
    }

    @Override
    public void log(@LogPriority int priority, @NonNull String tag, @NonNull String message) {
        if (priority < Constants.MIN_LOG_LEVEL) {
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

        LogEntry logEntry = new LogEntry(priority, tag, message);
        logToBuffer(logEntry);

        if (mPrivacyManager.isCrashReportingEnabled() && ZephyrApplication.isFabricInitialized()) {
            logToCrashlytics(logEntry);
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

    @Override
    public List<LogEntry> getLogs() {
        return new ArrayList<>(mEntries);
    }

    private void logToBuffer(@NonNull LogEntry logEntry) {
        synchronized (mEntries) {
            mEntries.addLast(logEntry);
            if (mEntries.size() > Constants.LOG_BUFFER_SIZE) {
                mEntries.removeFirst();
            }
        }
    }

    private void logToCrashlytics(@NonNull LogEntry logEntry) {
        if (logEntry.getPriority() < Constants.MIN_LOG_LEVEL_CRASHLYTICS) {
            return;
        }

        Crashlytics.log(logEntry.toString());
    }
}
