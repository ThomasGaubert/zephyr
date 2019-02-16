package com.texasgamer.zephyr.util.log;

import androidx.annotation.NonNull;

public interface ILogSanitizer {
    @NonNull
    String sanitize(@NonNull String stringToSanitize);
}
