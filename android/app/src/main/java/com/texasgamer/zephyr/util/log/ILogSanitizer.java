package com.texasgamer.zephyr.util.log;

import androidx.annotation.NonNull;

/**
 * Log sanitizer interface.
 */
public interface ILogSanitizer {
    @NonNull
    String sanitize(@NonNull String stringToSanitize);
}
