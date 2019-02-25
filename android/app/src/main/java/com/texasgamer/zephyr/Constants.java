package com.texasgamer.zephyr;

import com.texasgamer.zephyr.util.log.LogPriority;

public class Constants {

    /* Versioning */
    public static final int V2_VERSION_CODE = 10;
    public static final int ZEPHYR_API_VERSION = 1;

    /* URLS */
    public static final String ZEPHYR_GITHUB_URL = "https://github.com/ThomasGaubert/zephyr";
    public static final String ZEPHYR_HELP_URL = "https://zephyrvr.gitbook.io/docs/";

    /* Server */
    public static final String ZEPHYR_SERVER_PORT = "3753";

    /* Preferences */
    public static final int PREFS_VERSION = 1;

    /* Android */
    public static final String ANDROID_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    /* Logging */
    @LogPriority
    public static final int MIN_LOG_LEVEL = LogPriority.VERBOSE;
    @LogPriority
    public static final int MIN_LOG_LEVEL_CRASHLYTICS = LogPriority.INFO;

    /* Database */
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "zephyr";

    /* Firebase */
    public static final boolean FIREBASE_ENABLED = true;
    public static final boolean FIREBASE_ANALYTICS_ENABLED = true;
    public static final boolean FIREBASE_CRASHLYTICS_ENABLED = true;
    public static final boolean FIREBASE_REMOTE_CONFIG_ENABLED = true;
    public static final int FIREBASE_REMOTE_CONFIG_CACHE_EXPIRY_IN_SECONDS = 10800; // 3 hours
}
