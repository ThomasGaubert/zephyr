package com.texasgamer.zephyr.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.fragment.ConnectFragment;
import com.texasgamer.zephyr.fragment.MenuFragment;
import com.texasgamer.zephyr.service.SocketService;
import com.texasgamer.zephyr.util.log.LogPriority;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.PreferenceManager;
import com.texasgamer.zephyr.util.preference.SharedPreferenceLiveData;
import com.texasgamer.zephyr.view.CheckableMaterialButton;
import com.texasgamer.zephyr.viewmodel.ConnectButtonViewModel;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.OnConflictStrategy;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class MainActivity extends BaseActivity {

    private ConnectButtonViewModel mConnectButtonViewModel;

    @BindView(R.id.bottom_app_bar)
    BottomAppBar bottomAppBar;
    @BindView(R.id.connect_button)
    CheckableMaterialButton connectButton;

    private MenuFragment mMenuFragment;
    private ConnectFragment mConnectFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMenuFragment = new MenuFragment();
        mConnectFragment = new ConnectFragment();
        setSupportActionBar(bottomAppBar);

        mConnectButtonViewModel = new ConnectButtonViewModel(ZephyrApplication.getInstance());
        subscribeUi(mConnectButtonViewModel.getIsConnected());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMenuFragment.show(getSupportFragmentManager(), mMenuFragment.getTag());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @OnClick(R.id.connect_button)
    public void onClickConnectButton() {
        Intent socketServiceIntent = new Intent(MainActivity.this, SocketService.class);

        if (!connectButton.isChecked()) {
            if (isJoinCodeSet()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(socketServiceIntent);
                } else {
                    startService(socketServiceIntent);
                }
            } else {
                mConnectFragment.show(getSupportFragmentManager(), mConnectFragment.getTag());
                // TODO: Start service after showing fragment
            }
        } else {
            stopService(socketServiceIntent);
        }
    }

    @OnLongClick(R.id.connect_button)
    public boolean onLongClickConnectButton() {
        mConnectFragment.show(getSupportFragmentManager(), mConnectFragment.getTag());
        return true;
    }

    private void subscribeUi(LiveData<Boolean> liveData) {
        liveData.observe(this, isServiceRunning -> {
            connectButton.setChecked(!isServiceRunning);
        });
    }

    private boolean isJoinCodeSet() {
        String joinCode = mPreferenceManager.getString(PreferenceKeys.PREF_JOIN_CODE);
        return joinCode != null && !joinCode.isEmpty();
    }
}
