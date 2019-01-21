package com.texasgamer.zephyr.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class PreferenceManager {

    private Context mContext;

    public PreferenceManager(@NonNull Context context) {
        mContext = context;
    }

    public void putString(@NonNull Context context, @NonNull String key, @NonNull String value) {
        getSharedPrefs(context).edit().putString(key, value).apply();
    }

    public String getString(@NonNull Context context, @NonNull String key) {
        return getString(context, key, null);
    }

    public String getString(@NonNull Context context, @NonNull String key, @NonNull String defaultValue) {
        return getSharedPrefs(context).getString(key, defaultValue);
    }

    public void putInt(@NonNull Context context, @NonNull String key, int value) {
        getSharedPrefs(context).edit().putInt(key, value).apply();
    }

    public int getInt(@NonNull Context context, @NonNull String key) {
        return getInt(context, key, 0);
    }

    public int getInt(@NonNull Context context, @NonNull String key, int defaultValue) {
        return getSharedPrefs(context).getInt(key, defaultValue);
    }

    public void putBoolean(@NonNull Context context, @NonNull String key, boolean value) {
        getSharedPrefs(context).edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(@NonNull Context context, @NonNull String key) {
        return getBoolean(context, key, false);
    }

    public boolean getBoolean(@NonNull Context context, @NonNull String key, boolean defaultValue) {
        return getSharedPrefs(context).getBoolean(key, defaultValue);
    }

    private SharedPreferences getSharedPrefs(@NonNull Context context) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
    }
}
