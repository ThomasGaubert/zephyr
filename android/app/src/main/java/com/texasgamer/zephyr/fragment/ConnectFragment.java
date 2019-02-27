package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_connect, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);

        // Skip to entering code is QR scanning isn't enabled
        if (!configManager.isQrCodeScanningEnabled()) {
            logger.log(LogPriority.INFO, LOG_TAG, "QR code scanning is disabled, skipping to entering code.");
            JoinCodeFragment joinCodeFragment = new JoinCodeFragment();
            joinCodeFragment.show(getFragmentManager(), joinCodeFragment.getTag());
            dismiss();
        }

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_scan_code:
                ScanCodeFragment scanCodeFragment = new ScanCodeFragment();
                scanCodeFragment.show(getFragmentManager(), scanCodeFragment.getTag());
                break;
            case R.id.action_enter_code:
                JoinCodeFragment joinCodeFragment = new JoinCodeFragment();
                joinCodeFragment.show(getFragmentManager(), joinCodeFragment.getTag());
                break;
            default:
                break;
        }

        dismiss();

        return false;
    }
}
