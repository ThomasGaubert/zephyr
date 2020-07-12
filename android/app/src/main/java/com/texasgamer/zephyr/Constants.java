package com.texasgamer.zephyr;

import com.texasgamer.zephyr.util.log.LogLevel;

/**
 * Zephyr constants.
 */
@SuppressWarnings("PMD.AvoidUsingHardCodedIP")
public final class Constants {

    /* Versioning */
    public static final int V2_VERSION_CODE = 10;
    public static final int ZEPHYR_API_VERSION = 1;
    public static final int WHATS_NEW_VERSION = 0;

    /* URLS */
    public static final String ZEPHYR_FEEDBACK_URL = "https://github.com/ThomasGaubert/zephyr/issues/new/choose";
    public static final String ZEPHYR_GITHUB_URL = "https://github.com/ThomasGaubert/zephyr";
    public static final String ZEPHYR_HELP_URL = "https://zephyrvr.gitbook.io/docs/";
    public static final String ZEPHYR_CONNECTION_HELP_URL = "https://zephyrvr.gitbook.io/docs/troubleshooting/connection-issues";
    public static final String ZEPHYR_STEAM_URL = "https://store.steampowered.com/app/495000";
    public static final String ZEPHYR_PRIVACY_URL = "https://github.com/ZephyrVR/documents/blob/master/privacy-policy.md";
    public static final String ZEPHYR_WHATS_NEW_URL = "https://github.com/ThomasGaubert/zephyr/blob/master/CHANGELOG.md#200---wip";

    /* Server */
    public static final String ZEPHYR_SERVER_PORT = "3753";

    /* Preferences */
    public static final int PREFS_VERSION = 1;
    public static final String DEFAULT_UUID = "00000000-0000-0000-0000-000000000000";

    /* Android */
    public static final String ANDROID_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    /* Logging */
    @LogLevel
    public static final int MIN_LOG_LEVEL = LogLevel.VERBOSE;
    @LogLevel
    public static final int MIN_LOG_LEVEL_CRASHLYTICS = LogLevel.INFO;
    public static final int LOG_BUFFER_SIZE = 200;

    /* Database */
    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "zephyr";

    /* Firebase */
    public static final boolean FIREBASE_ENABLED = true;
    public static final boolean FIREBASE_ANALYTICS_ENABLED = true;
    public static final boolean FIREBASE_CRASHLYTICS_ENABLED = true;
    public static final boolean FIREBASE_REMOTE_CONFIG_ENABLED = true;
    public static final boolean FIREBASE_PERFORMANCE_MONITORING_ENABLED = true;
    public static final int FIREBASE_REMOTE_CONFIG_CACHE_EXPIRY_IN_SECONDS = 10800; // 3 hours

    /* Discovery */
    public static final String DISCOVERY_BROADCAST_ADDRESS = "230.186.100.28";
    public static final int DISCOVERY_BROADCAST_PORT = 3752;
    public static final int DISCOVERY_BROADCAST_INTERVAL_IN_MS = 3000; // 3 seconds
    public static final int DISCOVERY_BROADCAST_TIMEOUT_IN_MS = 4000; // 4 seconds

    private Constants() {
    }
}
