package com.texasgamer.zephyr.util.preference;

import android.content.SharedPreferences;

import com.texasgamer.zephyr.ZephyrApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

/**
 * LiveData wrapper for SharedPreferences.
 * @param <T>
 */
public class SharedPreferenceLiveData<T> extends LiveData<T>
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final String mKey;
    private final T mDefaultValue;
    private final IPreferenceManager mPreferenceManager;

    public SharedPreferenceLiveData(@NonNull String key) {
        this(key, null);
    }

    public SharedPreferenceLiveData(@NonNull String key, @Nullable T defaultValue) {
        mPreferenceManager = ZephyrApplication.getApplicationComponent().preferenceManager();
        mKey = key;
        mDefaultValue = defaultValue;
    }

    @Override
    protected void onActive() {
        super.onActive();
        setValue(getValueFromPreferences());
        mPreferenceManager.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onInactive() {
        mPreferenceManager.unregisterOnSharedPreferenceChangeListener(this);
        super.onInactive();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(mKey)) {
            setValue(getValueFromPreferences());
        }
    }

    private T getValueFromPreferences() {
        return (T) mPreferenceManager.getObject(mKey, mDefaultValue);
    }
}
