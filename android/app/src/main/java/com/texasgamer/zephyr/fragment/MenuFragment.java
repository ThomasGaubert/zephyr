package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationView;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.activity.AboutActivity;
import com.texasgamer.zephyr.activity.NotificationActivity;
import com.texasgamer.zephyr.util.NavigationUtils;
import com.texasgamer.zephyr.util.analytics.IAnalyticsManager;
import com.texasgamer.zephyr.util.analytics.ZephyrEvent;
import com.texasgamer.zephyr.util.config.IConfigManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Menu fragment.
 */
public class MenuFragment extends RoundedBottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.debug_menu_btn)
    View debugMenuButton;
    @BindView(R.id.nav_menu)
    NavigationView mNavigationView;

    @Inject
    IConfigManager configManager;
    @Inject
    IAnalyticsManager analyticsManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);
        mNavigationView.setNavigationItemSelectedListener(this);

        if (configManager.isDebugMenuEnabled()) {
            debugMenuButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_manage_notifications:
                analyticsManager.logEvent(ZephyrEvent.Navigation.MANAGE_NOTIFICATIONS);
                NavigationUtils.openActivity(getContext(), NotificationActivity.class);
                break;
            case R.id.action_privacy:
                PrivacyFragment privacyFragment = new PrivacyFragment();
                privacyFragment.show(getParentFragmentManager(), privacyFragment.getTag());
                break;
            case R.id.action_help:
                analyticsManager.logEvent(ZephyrEvent.Navigation.HELP);
                NavigationUtils.openUrl(getContext(), Constants.ZEPHYR_HELP_URL);
                break;
            case R.id.action_about:
                analyticsManager.logEvent(ZephyrEvent.Navigation.ABOUT);
                NavigationUtils.openActivity(getContext(), AboutActivity.class);
                break;
            default:
                break;
        }

        dismiss();

        return false;
    }

    @OnClick(R.id.debug_menu_btn)
    public void onClickDebugMenuButton() {
        DebugFragment debugFragment = new DebugFragment();
        debugFragment.show(getFragmentManager(), debugFragment.getTag());

        dismiss();
    }
}
