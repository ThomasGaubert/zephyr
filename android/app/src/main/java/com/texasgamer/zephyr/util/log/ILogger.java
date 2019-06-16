package com.texasgamer.zephyr.util.log;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Logger interface.
 */
public interface ILogger {

    void log(@LogPriority int priority, @NonNull String tag, @NonNull String message);

    void log(@LogPriority int priority, @NonNull String tag, @NonNull String message, @NonNull Object... args);

    void log(@LogPriority int priority, @NonNull String tag, @NonNull Throwable throwable, @NonNull String message, @NonNull Object... args);

    void log(@LogPriority int priority, @NonNull String tag, @NonNull Throwable throwable);

    List<LogEntry> getLogs();
}
