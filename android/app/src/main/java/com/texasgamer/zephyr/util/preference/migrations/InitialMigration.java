package com.texasgamer.zephyr.util.preference.migrations;

import android.content.SharedPreferences;

import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import androidx.annotation.NonNull;

/**
 * Initial migration which always runs on fresh install.
 */
public class InitialMigration implements IZephyrPreferenceMigration {
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
        String deviceName = sharedPreferences.getString(PreferenceKeys.PREF_DEVICE_NAME, "");
        boolean firstRun = sharedPreferences.getBoolean(PreferenceKeys.PREF_FIRST_RUN, true);

        boolean didUpgradeFromV1 = firstRun && !deviceName.isEmpty();

        sharedPreferences.edit().putBoolean(PreferenceKeys.PREF_DID_UPGRADE_FROM_V1, didUpgradeFromV1).apply();
    }
}
