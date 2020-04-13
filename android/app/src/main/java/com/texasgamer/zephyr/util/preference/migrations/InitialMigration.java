package com.texasgamer.zephyr.util.preference.migrations;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.preference.PreferenceKeys;

/**
 * Initial migration which always runs on fresh install.
 */
public class InitialMigration implements IZephyrPreferenceMigration {

    private static final String[] V1_PREFERENCES = {
            PreferenceKeys.PREF_V1_FIRST_RUN,
            PreferenceKeys.PREF_V1_LAST_ADDRESS,
            PreferenceKeys.PREF_V1_SMART_CONNECT,
            PreferenceKeys.PREF_V1_DEVICE_NAME,
            PreferenceKeys.PREF_V1_APP_NOTIF_BASE,
            PreferenceKeys.PREF_V1_VERSION,
            PreferenceKeys.PREF_V1_LICENSE,
            PreferenceKeys.PREF_V1_UUID,
            PreferenceKeys.PREF_V1_HIDE_LOGIN_CARD,
            PreferenceKeys.PREF_V1_ACCOUNT
    };

    @Override
    public int fromVersion() {
        return 0;
    }

    @Override
    public int toVersion() {
        return 1;
    }

    @Override
    public void migrate(@NonNull SharedPreferences sharedPreferences) {
        boolean didUpgradeFromV1 = false;
        for (String v1Preference : V1_PREFERENCES) {
            if (sharedPreferences.contains(v1Preference)) {
                didUpgradeFromV1 = true;
                break;
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (didUpgradeFromV1) {
            editor.clear();
        }

        editor.putBoolean(PreferenceKeys.PREF_DID_UPGRADE_FROM_V1, didUpgradeFromV1).apply();
    }
}
