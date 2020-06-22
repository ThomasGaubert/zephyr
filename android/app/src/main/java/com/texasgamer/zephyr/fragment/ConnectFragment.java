package com.texasgamer.zephyr.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.model.discovery.DiscoveredServer;
import com.texasgamer.zephyr.network.IDiscoveryManager;
import com.texasgamer.zephyr.service.QuickSettingService;
import com.texasgamer.zephyr.util.StringUtils;
import com.texasgamer.zephyr.util.analytics.IAnalyticsManager;
import com.texasgamer.zephyr.util.analytics.ZephyrEvent;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogLevel;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Connect fragment.
 */
public class ConnectFragment extends RoundedBottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    public static final String LOG_TAG = "ConnectFragment";

    @BindView(R.id.nav_menu)
    NavigationView mNavigationView;

    @Inject
    ILogger logger;
    @Inject
    IConfigManager configManager;
    @Inject
    IAnalyticsManager analyticsManager;
    @Inject
    IPreferenceManager preferenceManager;
    @Inject
    IDiscoveryManager discoveryManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_connect, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);

        analyticsManager.logEvent(ZephyrEvent.Action.OPEN_CONNECTION_MENU);

        // Skip to entering code is QR scanning isn't enabled
        if (!configManager.isQrCodeScanningEnabled()) {
            logger.log(LogLevel.INFO, LOG_TAG, "QR code scanning is disabled, skipping to entering code.");
            JoinCodeFragment joinCodeFragment = new JoinCodeFragment();
            joinCodeFragment.show(getParentFragmentManager(), joinCodeFragment.getTag());
            dismiss();
        }

        mNavigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderContainer = mNavigationView.getRootView().findViewById(R.id.navigation_header_container);
        if (navigationHeaderContainer != null) {
            navigationHeaderContainer.setVisibility(View.GONE);
        }

        Menu menu = mNavigationView.getMenu();
        SubMenu subMenu = menu.addSubMenu(R.string.menu_connect_discovered_servers);
        discoveryManager.getDiscoveredServers().observe(getViewLifecycleOwner(), discoveredServers -> {
            subMenu.clear();
            for (DiscoveredServer discoveredServer : discoveredServers) {
                String hostName = StringUtils.isNullOrEmpty(discoveredServer.getHostName())
                        ? getString(R.string.menu_connect_discovered_server_default_name)
                        : discoveredServer.getHostName();
                MenuItem menuItem = subMenu.add(hostName + " (" + discoveredServer.getIpAddress() + ")");
                menuItem.setOnMenuItemClickListener(item -> {
                    connectToDiscoveredServer(discoveredServer);
                    return true;
                });
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_scan_code:
                analyticsManager.logEvent(ZephyrEvent.Action.TAP_QR_CODE);
                ScanCodeFragment scanCodeFragment = new ScanCodeFragment();
                scanCodeFragment.show(getParentFragmentManager(), scanCodeFragment.getTag());
                break;
            case R.id.action_enter_code:
                analyticsManager.logEvent(ZephyrEvent.Action.TAP_ENTER_CODE);
                JoinCodeFragment joinCodeFragment = new JoinCodeFragment();
                joinCodeFragment.show(getParentFragmentManager(), joinCodeFragment.getTag());
                break;
            default:
                break;
        }

        dismiss();

        return false;
    }

    @Override
    protected int getInitialBottomSheetState() {
        return BottomSheetBehavior.STATE_EXPANDED;
    }

    @Override
    protected boolean shouldSkipCollapsedState() {
        return true;
    }

    private void connectToDiscoveredServer(@NonNull DiscoveredServer discoveredServer) {
        analyticsManager.logEvent(ZephyrEvent.Action.TAP_DISCOVERED_SERVER);
        preferenceManager.putString(PreferenceKeys.PREF_JOIN_CODE, discoveredServer.getIpAddress());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            QuickSettingService.updateQuickSettingTile(getContext());
        }
        dismiss();
    }
}
