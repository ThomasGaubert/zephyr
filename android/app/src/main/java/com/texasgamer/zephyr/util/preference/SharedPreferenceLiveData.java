package com.texasgamer.zephyr.util.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.texasgamer.zephyr.ZephyrApplication;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class SharedPreferenceLiveData<T> extends LiveData<T> {

    private String mKey;
    private T mDefaultValue;
    private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(mKey)) {
                setValue(getValueFromPreferences());
            }
        }
    };

    PreferenceManager preferenceManager;

    public SharedPreferenceLiveData(@NonNull String key) {
        this(key, null);
    }

    public SharedPreferenceLiveData(@NonNull String key, @Nullable T defaultValue) {
        preferenceManager = ZephyrApplication.getApplicationComponent().preferenceManager();
        mKey = key;
        mDefaultValue = defaultValue;
    }

    @Override
    protected void onActive() {
        super.onActive();
        setValue(getValueFromPreferences());
        preferenceManager.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    }

    @Override
    protected void onInactive() {
        preferenceManager.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
        super.onInactive();
    }

    private T getValueFromPreferences() {
        return (T) preferenceManager.getObject(mKey, mDefaultValue);
    }
}
