package com.texasgamer.zephyr.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
import com.texasgamer.zephyr.util.theme.IThemeManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Menu fragment.
 */
public class MenuFragment extends RoundedBottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.theme_btn)
    View themeButton;
    @BindView(R.id.debug_menu_btn)
    View debugMenuButton;
    @BindView(R.id.nav_menu)
    NavigationView mNavigationView;

    @Inject
    IConfigManager configManager;
    @Inject
    IAnalyticsManager analyticsManager;
    @Inject
    IThemeManager themeManager;

    private NavController mMainNavController;

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
        themeButton.setVisibility(configManager.isThemingEnabled() ? View.VISIBLE : View.GONE);
        debugMenuButton.setVisibility(configManager.isDebugMenuEnabled() ? View.VISIBLE : View.GONE);

        NavHostFragment navHostFragment = (NavHostFragment) getParentFragmentManager().findFragmentById(R.id.main_fragment);
        if (navHostFragment != null) {
            mMainNavController = navHostFragment.getNavController();
//            NavigationUI.setupWithNavController(mNavigationView, mMainNavController);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_manage_notifications:
                analyticsManager.logEvent(ZephyrEvent.Navigation.MANAGE_NOTIFICATIONS);
//                NavigationUtils.openActivity(getContext(), NotificationActivity.class);
                mMainNavController.navigate(R.id.action_fragment_menu_to_fragment_notifications);
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
//                NavigationUtils.openActivity(getContext(), AboutActivity.class);
                mMainNavController.navigate(R.id.action_fragment_menu_to_fragment_about);
//                Navigation.findNavController(getActivity(), R.id.main_fragment).navigate(R.id.about_fragment);
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

    @OnClick(R.id.theme_btn)
    public void onClickThemeButton() {
        if (getActivity() == null) {
            return;
        }

        Resources resources = getActivity().getResources();
        final CharSequence[] themeChoices = {
                resources.getString(R.string.menu_theme_system),
                resources.getString(R.string.menu_theme_light),
                resources.getString(R.string.menu_theme_dark)};

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(getActivity())
                .setTitle(R.string.menu_theme_title)
                .setSingleChoiceItems(themeChoices, themeManager.getCurrentThemeSetting(), (dialog, which) -> {
                    themeManager.setCurrentThemeSetting(which);
                    dialog.dismiss();
                }).create();
        alertDialog.show();
    }

    @Override
    protected int getInitialBottomSheetState() {
        return BottomSheetBehavior.STATE_EXPANDED;
    }

    @Override
    protected boolean shouldSkipCollapsedState() {
        return true;
    }
}
