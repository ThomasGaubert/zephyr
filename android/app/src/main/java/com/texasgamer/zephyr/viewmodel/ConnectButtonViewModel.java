package com.texasgamer.zephyr.viewmodel;

import android.app.Application;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.SharedPreferenceLiveData;

import androidx.lifecycle.LiveData;

/**
 * Connect button view model.
 */
public class ConnectButtonViewModel extends BaseViewModel {

    private final SharedPreferenceLiveData<Boolean> mIsConnected;

    public ConnectButtonViewModel(Application application) {
        super(application);
        mIsConnected = new SharedPreferenceLiveData<>(PreferenceKeys.PREF_IS_CONNECTED, false);
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    public LiveData<Boolean> getIsConnected() {
        return mIsConnected;
    }
}
