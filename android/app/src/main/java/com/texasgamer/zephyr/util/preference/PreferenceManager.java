package com.texasgamer.zephyr.util.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;
import com.texasgamer.zephyr.util.preference.migrations.IZephyrPreferenceMigration;
import com.texasgamer.zephyr.util.preference.migrations.PreferenceMigrationFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Preference manager.
 */
public class PreferenceManager implements IPreferenceManager {

    private static final String LOG_TAG = "PreferenceManager";

    private SharedPreferences mSharedPreferences;
    private ILogger mLogger;

    public PreferenceManager(@NonNull Context context, ILogger logger) {
        mSharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        mLogger = logger;
        checkForMigrations();
    }

    @Override
    public void putString(@NonNull String key, @NonNull String value) {
        getSharedPrefs().edit().putString(key, value).apply();
    }

    @Override
    public String getString(@NonNull String key) {
        return getString(key, null);
    }

    @Override
    public String getString(@NonNull String key, @Nullable String defaultValue) {
        return getSharedPrefs().getString(key, defaultValue);
    }

    @Override
    public void putInt(@NonNull String key, int value) {
        getSharedPrefs().edit().putInt(key, value).apply();
    }

    @Override
    public int getInt(@NonNull String key) {
        return getInt(key, 0);
    }

    @Override
    public int getInt(@NonNull String key, int defaultValue) {
        return getSharedPrefs().getInt(key, defaultValue);
    }

    @Override
    public void putBoolean(@NonNull String key, boolean value) {
        getSharedPrefs().edit().putBoolean(key, value).apply();
    }

    @Override
    public boolean getBoolean(@NonNull String key) {
        return getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return getSharedPrefs().getBoolean(key, defaultValue);
    }

    @Override
    public Object getObject(@NonNull String key) {
        return getObject(key, null);
    }

    @Override
    public Object getObject(@NonNull String key, @Nullable Object defaultValue) {
        Object result = getSharedPrefs().getAll().get(key);
        return result != null ? result : defaultValue;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getSharedPrefs().registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getSharedPrefs().unregisterOnSharedPreferenceChangeListener(listener);
    }

    private SharedPreferences getSharedPrefs() {
        return mSharedPreferences;
    }

    private void checkForMigrations() {
        int currentVersion = getInt(PreferenceKeys.PREF_VERSION, -1);

        if (currentVersion == -1) {
            mLogger.log(LogPriority.INFO, LOG_TAG, "Running initial migration for SharedPreferences.");
            PreferenceMigrationFactory.getInitialMigration().migrate(getSharedPrefs());
            putInt(PreferenceKeys.PREF_VERSION, Constants.PREFS_VERSION);
        } else if (currentVersion < Constants.PREFS_VERSION) {
            mLogger.log(LogPriority.INFO, LOG_TAG, "Preferences version: %1$d - Running migrations for %1$d to %2$d.", currentVersion, Constants.PREFS_VERSION);
            for (IZephyrPreferenceMigration migration : PreferenceMigrationFactory.getMigrations(currentVersion, Constants.PREFS_VERSION)) {
                migration.migrate(getSharedPrefs());
            }
            putInt(PreferenceKeys.PREF_VERSION, Constants.PREFS_VERSION);
            mLogger.log(LogPriority.INFO, LOG_TAG, "Migrations complete. New preferences version: %d", Constants.PREFS_VERSION);
        } else {
            mLogger.log(LogPriority.INFO, LOG_TAG, "Preferences version: %d - No migration needed.", currentVersion);
        }
    }
}
