package com.texasgamer.zephyr.util.preference;

/**
 * Preference keys.
 */
public final class PreferenceKeys {
    public static final String PREF_VERSION = "version";
    public static final String PREF_COMPLETED_FRE = "completedFre";
    public static final String PREF_LAST_KNOWN_APP_VERSION = "lastKnownAppVersion";
    public static final String PREF_DID_UPGRADE_FROM_V1 = "didUpgradeFromV1";
    public static final String PREF_IS_SOCKET_SERVICE_RUNNING = "isSocketServiceRunning";
    public static final String PREF_CONNECTION_STATUS = "connectionStatus";
    public static final String PREF_JOIN_CODE = "joinCode";

    /* Privacy */
    public static final String PREF_ENABLE_USAGE_DATA = "enableUsageData";
    public static final String PREF_ENABLE_CRASH_REPORTS = "enableCrashReports";
    public static final String PREF_ENABLE_UUID = "enableUuid";
    public static final String PREF_UUID = "uuid";

    /* Zephyr v1 */
    public static final String PREF_DEVICE_NAME = "deviceName";
    public static final String PREF_FIRST_RUN = "firstRun";

    private PreferenceKeys() {
    }
}
