package com.texasgamer.zephyr.util.preference

/**
 * Preference keys.
 */
object PreferenceKeys {
    const val PREF_VERSION = "version"
    const val PREF_COMPLETED_FRE = "completedFre"
    const val PREF_LAST_KNOWN_APP_VERSION = "lastKnownAppVersion"
    const val PREF_DID_UPGRADE_FROM_V1 = "didUpgradeFromV1"
    const val PREF_IS_SOCKET_SERVICE_RUNNING = "isSocketServiceRunning"
    const val PREF_CONNECTION_STATUS = "connectionStatus"
    const val PREF_JOIN_CODE = "joinCode"

    /* Privacy */
    const val PREF_ENABLE_USAGE_DATA = "enableUsageData"
    const val PREF_ENABLE_CRASH_REPORTS = "enableCrashReports"
    const val PREF_ENABLE_UUID = "enableUuid"
    const val PREF_UUID = "uuid"

    /* Onboarding */
    const val PREF_EVER_CONNECTED_TO_SERVER = "everConnectedToServer"
    const val PREF_SHOW_CONNECTION_HELP_CARD = "showConnectionHelpCard"
    const val PREF_SEEN_MANAGE_NOTIFICATIONS = "seenManageNotifications"
    const val PREF_SEEN_V2_PROMO = "seenV2Promo"

    /* What's new */
    const val PREF_LAST_SEEN_WHATS_NEW_VERSION = "lastSeenWhatsNewVersion"

    /* Theme */
    const val PREF_THEME = "theme"

    /* Migration */
    const val PREF_NOTIFICATION_PREF_MIGRATIONS_TO_COMPLETE = "notificationPrefMigrationsToComplete"

    /* Zephyr v1 */
    const val PREF_V1_FIRST_RUN = "firstRun"
    const val PREF_V1_APP_NOTIF_BASE = "appNotif"

    /* Debug */
    const val PREF_DEBUG_SHOW_ALL_CARDS = "debugShowAllCards"
    const val PREF_DEBUG_ENABLE_MOCK_DATA = "debugEnableMockData"
}