package com.texasgamer.zephyr.util.preference.migrations;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import com.google.gson.Gson;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import java.util.Map;

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
        boolean didUpgradeFromV1 = sharedPreferences.contains(PreferenceKeys.PREF_V1_FIRST_RUN);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (didUpgradeFromV1) {
            Map<String, Boolean> notificationPreferencesToMigrate = new ArrayMap<>();
            for (String key : sharedPreferences.getAll().keySet()) {
                if (key.startsWith(PreferenceKeys.PREF_V1_APP_NOTIF_BASE)) {
                    String packageName = key.substring(key.indexOf('-') + 1);
                    boolean enabled = sharedPreferences.getBoolean(key, true);
                    notificationPreferencesToMigrate.put(packageName, enabled);
                }
            }
            editor.clear();
            editor.putString(PreferenceKeys.PREF_NOTIFICATION_PREF_MIGRATIONS_TO_COMPLETE, new Gson().toJson(notificationPreferencesToMigrate));
        }

        editor.putBoolean(PreferenceKeys.PREF_DID_UPGRADE_FROM_V1, didUpgradeFromV1);
        editor.apply();
    }
}
