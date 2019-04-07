package com.texasgamer.zephyr.viewmodel;

import android.app.Application;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.model.ConnectionStatus;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.SharedPreferenceLiveData;

import androidx.annotation.NonNull;

public class MainFragmentViewModel extends BaseViewModel {

    private final SharedPreferenceLiveData<Integer> mConnectionStatus;
    private final SharedPreferenceLiveData<String> mJoinCode;

    public MainFragmentViewModel(Application application) {
        super(application);
        mConnectionStatus = new SharedPreferenceLiveData<>(PreferenceKeys.PREF_CONNECTION_STATUS, ConnectionStatus.DISCONNECTED);
        mJoinCode = new SharedPreferenceLiveData<>(PreferenceKeys.PREF_JOIN_CODE, "");
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @NonNull
    public SharedPreferenceLiveData<Integer> getConnectionStatus() {
        return mConnectionStatus;
    }

    @NonNull
    public SharedPreferenceLiveData<String> getJoinCode() {
        return mJoinCode;
    }
}
