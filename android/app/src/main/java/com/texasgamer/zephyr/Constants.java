package com.texasgamer.zephyr;

import com.texasgamer.zephyr.util.log.LogPriority;

public class Constants {

    /* Versioning */
    public static final int V2_VERSION_CODE = 10;

    /* URLS */
    public static final String ZEPHYR_GITHUB_URL = "https://github.com/ThomasGaubert/zephyr";
    public static final String ZEPHYR_HELP_URL = "https://github.com/ThomasGaubert/zephyr";

    /* Preferences */
    public static final int PREFS_VERSION = 1;

    /* Android */
    public static final String ANDROID_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    /* Logging */
    @LogPriority
    public static final int MIN_LOG_LEVEL = LogPriority.VERBOSE;

    /* Database */
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "zephyr";
}
