package com.texasgamer.zephyr.util.preference;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Preference manager interface.
 */
public interface IPreferenceManager {

    void putString(@NonNull String key, @NonNull String value);

    String getString(@NonNull String key);

    String getString(@NonNull String key, @Nullable String defaultValue);

    void putInt(@NonNull String key, int value);

    int getInt(@NonNull String key);

    int getInt(@NonNull String key, int defaultValue);

    void putBoolean(@NonNull String key, boolean value);

    boolean getBoolean(@NonNull String key);

    boolean getBoolean(@NonNull String key, boolean defaultValue);

    Object getObject(@NonNull String key);

    Object getObject(@NonNull String key, @Nullable Object defaultValue);

    void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener);

    void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener);
}
