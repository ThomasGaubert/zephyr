package com.texasgamer.zephyr.viewmodel;

import android.app.Application;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.SharedPreferenceLiveData;

import androidx.annotation.NonNull;

public class MainFragmentViewModel extends BaseViewModel {

    private final SharedPreferenceLiveData<Boolean> mIsConnected;
    private final SharedPreferenceLiveData<String> mJoinCode;

    public MainFragmentViewModel(Application application) {
        super(application);
        mIsConnected = new SharedPreferenceLiveData<>(PreferenceKeys.PREF_IS_CONNECTED, false);
        mJoinCode = new SharedPreferenceLiveData<>(PreferenceKeys.PREF_JOIN_CODE, "");
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @NonNull
    public SharedPreferenceLiveData<Boolean> getIsConnected() {
        return mIsConnected;
    }

    @NonNull
    public SharedPreferenceLiveData<String> getJoinCode() {
        return mJoinCode;
    }
}
