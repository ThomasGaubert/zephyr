package com.texasgamer.zephyr.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.activity.MainActivity;
import com.texasgamer.zephyr.adapter.ZephyrCardViewPagerAdapter;
import com.texasgamer.zephyr.model.ConnectionStatus;
import com.texasgamer.zephyr.provider.IZephyrCardProvider;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.NetworkUtils;
import com.texasgamer.zephyr.util.navigation.NavigationUtils;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.SharedPreferenceLiveData;

import javax.inject.Inject;

/**
 * MainFragment view model.
 */
public class MainFragmentViewModel extends BaseViewModel {

    @Inject
    ApplicationUtils applicationUtils;
    @Inject
    IZephyrCardProvider zephyrCardProvider;

    private final MediatorLiveData<Drawable> mConnectionStatusIcon = new MediatorLiveData<>();
    private final MediatorLiveData<String> mConnectionStatusText = new MediatorLiveData<>();
    private final MediatorLiveData<String> mJoinCodeSummary = new MediatorLiveData<>();
    private final MediatorLiveData<Integer> mConnectedSectionVisibility = new MediatorLiveData<>();
    private final MediatorLiveData<Integer> mUnsupportedApiSectionVisibility = new MediatorLiveData<>();

    private ZephyrCardViewPagerAdapter mZephyrCardViewPagerAdapter;

    public MainFragmentViewModel(Application application) {
        super(application);

        SharedPreferenceLiveData<Integer> mConnectionStatus = new SharedPreferenceLiveData<>(PreferenceKeys.PREF_CONNECTION_STATUS, ConnectionStatus.DISCONNECTED);
        SharedPreferenceLiveData<String> mJoinCode = new SharedPreferenceLiveData<>(PreferenceKeys.PREF_JOIN_CODE, "");

        mConnectionStatusIcon.addSource(mConnectionStatus, connectionStatus ->
                mConnectionStatusIcon.setValue(mResourceProvider.getDrawable(NetworkUtils.connectionStatusToIsConnected(connectionStatus)
                        ? R.drawable.ic_check
                        : R.drawable.ic_error)));

        mConnectionStatusText.addSource(mConnectionStatus, connectionStatus ->
                mConnectionStatusText.setValue(mResourceProvider.getString(NetworkUtils.connectionStatusToString(connectionStatus))));

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

        mZephyrCardViewPagerAdapter = new ZephyrCardViewPagerAdapter(getApplication(),
                zephyrCardProvider.getCards(getApplication(), mNavigationManager));
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
    public LiveData<String> getConnectionStatusTextRes() {
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

    @NonNull
    public ZephyrCardViewPagerAdapter getCardPagerAdapter() {
        return mZephyrCardViewPagerAdapter;
    }

    public int getOffscreenPageLimit() {
        return 5;
    }

    public void refreshCards() {
        mZephyrCardViewPagerAdapter.setItems(zephyrCardProvider.getCards(getApplication(), mNavigationManager));
    }

    public void onClickJoinCodeSummary() {
        if (applicationUtils.hasNotificationAccess()) {
            mNavigationManager.navigate(R.id.action_fragment_main_to_fragment_connect);
        } else {
            Context context = ZephyrApplication.getInstance().getCurrentActivity();
            AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.permission_dialog_notifications_title)
                    .setMessage(R.string.permission_dialog_notifications_body)
                    .setPositiveButton(R.string.permission_dialog_notifications_enable,
                            (dialog, which) -> NavigationUtils.openNotificationAccessSettings(context))
                    .setNegativeButton(R.string.permission_dialog_notifications_cancel,
                            (dialog, which) -> dialog.cancel()).create();
            alertDialog.show();
        }
    }
}
