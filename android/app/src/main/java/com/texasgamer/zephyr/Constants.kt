package com.texasgamer.zephyr

import com.texasgamer.zephyr.util.log.LogLevel

/**
 * Zephyr constants.
 */
object Constants {
    /* Versioning */
    const val V2_VERSION_CODE = 10
    const val ZEPHYR_API_VERSION = 1
    const val WHATS_NEW_VERSION = 0

    /* URLS */
    const val ZEPHYR_FEEDBACK_URL = "https://github.com/ThomasGaubert/zephyr/issues/new/choose"
    const val ZEPHYR_GITHUB_URL = "https://github.com/ThomasGaubert/zephyr"
    const val ZEPHYR_HELP_URL = "https://zephyrvr.gitbook.io/docs/"
    const val ZEPHYR_CONNECTION_HELP_URL = "https://zephyrvr.gitbook.io/docs/troubleshooting/connection-issues"
    const val ZEPHYR_STEAM_URL = "https://store.steampowered.com/app/495000"
    const val ZEPHYR_PRIVACY_URL = "https://github.com/ZephyrVR/documents/blob/master/privacy-policy.md"
    const val ZEPHYR_WHATS_NEW_URL = "https://github.com/ThomasGaubert/zephyr/blob/master/CHANGELOG.md#200---wip"

    /* Server */
    const val ZEPHYR_SERVER_PORT = "3753"

    /* Preferences */
    const val PREFS_VERSION = 1
    const val DEFAULT_UUID = "00000000-0000-0000-0000-000000000000"

    /* Android */
    const val ANDROID_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

    /* Logging */
    @LogLevel
    const val MIN_LOG_LEVEL = LogLevel.VERBOSE

    @LogLevel
    const val MIN_LOG_LEVEL_CRASHLYTICS = LogLevel.INFO
    const val LOG_BUFFER_SIZE = 200

    /* Database */
    const val DB_VERSION = 2
    const val DB_NAME = "zephyr"

    /* Firebase */
    const val FIREBASE_ENABLED = true
    const val FIREBASE_ANALYTICS_ENABLED = true
    const val FIREBASE_CRASHLYTICS_ENABLED = true
    const val FIREBASE_REMOTE_CONFIG_ENABLED = true
    const val FIREBASE_PERFORMANCE_MONITORING_ENABLED = true

    /* Discovery */
    const val DISCOVERY_BROADCAST_ADDRESS = "230.186.100.28"
    const val DISCOVERY_BROADCAST_PORT = 3752
    const val DISCOVERY_BROADCAST_INTERVAL_IN_MS = 3000 // 3 seconds
    const val DISCOVERY_BROADCAST_TIMEOUT_IN_MS = 4000 // 4 seconds
}