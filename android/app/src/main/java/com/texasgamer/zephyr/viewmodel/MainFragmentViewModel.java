package com.texasgamer.zephyr.viewmodel;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.model.ConnectionStatus;
import com.texasgamer.zephyr.util.NetworkUtils;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.SharedPreferenceLiveData;

/**
 * MainFragment view model.
 */
public class MainFragmentViewModel extends BaseViewModel {

    private final MediatorLiveData<Drawable> mConnectionStatusIcon = new MediatorLiveData<>();
    private final MediatorLiveData<Integer> mConnectionStatusText = new MediatorLiveData<>();
    private final MediatorLiveData<String> mJoinCodeSummary = new MediatorLiveData<>();
    private final MediatorLiveData<Integer> mConnectedSectionVisibility = new MediatorLiveData<>();
    private final MediatorLiveData<Integer> mUnsupportedApiSectionVisibility = new MediatorLiveData<>();

    public MainFragmentViewModel(Application application) {
        super(application);

        SharedPreferenceLiveData<Integer> mConnectionStatus = new SharedPreferenceLiveData<>(PreferenceKeys.PREF_CONNECTION_STATUS, ConnectionStatus.DISCONNECTED);
        SharedPreferenceLiveData<String> mJoinCode = new SharedPreferenceLiveData<>(PreferenceKeys.PREF_JOIN_CODE, "");

        mConnectionStatusIcon.addSource(mConnectionStatus, connectionStatus ->
                mConnectionStatusIcon.setValue(mResourceProvider.getDrawable(NetworkUtils.connectionStatusToIsConnected(connectionStatus)
                        ? R.drawable.ic_check
                        : R.drawable.ic_error)));

        mConnectionStatusText.addSource(mConnectionStatus, connectionStatus ->
                mConnectionStatusText.setValue(NetworkUtils.connectionStatusToString(connectionStatus)));

        mJoinCodeSummary.addSource(mJoinCode, joinCode -> mJoinCodeSummary.setValue(joinCode == null || joinCode.isEmpty()
                ? mResourceProvider.getString(R.string.join_code_none)
                : mResourceProvider.getString(R.string.join_code_saved, joinCode)));

        mConnectedSectionVisibility.addSource(mConnectionStatus, connectionStatus ->
                mConnectedSectionVisibility.setValue(NetworkUtils.connectionStatusToIsConnected(connectionStatus)
                        ? View.VISIBLE
                        : View.GONE));

        mUnsupportedApiSectionVisibility.addSource(mConnectionStatus, connectionStatus ->
                mUnsupportedApiSectionVisibility.setValue(connectionStatus == ConnectionStatus.UNSUPPORTED_API
                        ? View.VISIBLE
                        : View.GONE));
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @NonNull
    public LiveData<String> getJoinCodeSummary() {
        return mJoinCodeSummary;
    }

    @NonNull
    public LiveData<Drawable> getConnectionStatusIcon() {
        return mConnectionStatusIcon;
    }

    @NonNull
    public LiveData<Integer> getConnectionStatusTextRes() {
        return mConnectionStatusText;
    }

    @NonNull
    public LiveData<Integer> getConnectedSectionVisibility() {
        return mConnectedSectionVisibility;
    }

    @NonNull
    public LiveData<Integer> getUnsupportedApiSectionVisibility() {
        return mUnsupportedApiSectionVisibility;
    }

    public void onClickJoinCodeSummary() {
        mNavigationManager.navigate(R.id.action_fragment_main_to_fragment_connect);
    }
}
