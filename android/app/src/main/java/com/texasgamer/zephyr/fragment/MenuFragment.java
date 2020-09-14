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
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavType;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.analytics.IAnalyticsManager;
import com.texasgamer.zephyr.util.analytics.ZephyrEvent;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.layout.ILayoutManager;
import com.texasgamer.zephyr.util.navigation.INavigationManager;
import com.texasgamer.zephyr.util.navigation.NavigationArgs;
import com.texasgamer.zephyr.util.navigation.NavigationUtils;
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
    @Inject
    ILayoutManager layoutManager;
    @Inject
    INavigationManager navigationManager;

    private NavController mSecondaryNavController;

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

        mSecondaryNavController = navigationManager.getSecondaryNavController();

        checkActiveItem();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.isChecked()) {
            dismiss();
            return false;
        }

        switch (menuItem.getItemId()) {
            case R.id.action_manage_notifications:
                analyticsManager.logEvent(ZephyrEvent.Navigation.MANAGE_NOTIFICATIONS);
                navigationManager.navigate(R.id.action_fragment_menu_to_fragment_notifications, R.id.fragment_notifications);
                break;
            case R.id.action_privacy:
                navigationManager.navigate(R.id.action_fragment_menu_to_fragment_privacy);
                break;
            case R.id.action_help:
                analyticsManager.logEvent(ZephyrEvent.Navigation.HELP);
                NavigationUtils.openUrl(requireContext(), Constants.ZEPHYR_HELP_URL);
                break;
            case R.id.action_about:
                analyticsManager.logEvent(ZephyrEvent.Navigation.ABOUT);
                navigationManager.navigate(R.id.action_fragment_menu_to_fragment_about, R.id.fragment_about);
                break;
            default:
                break;
        }

        dismiss();

        return false;
    }

    @OnClick(R.id.debug_menu_btn)
    public void onClickDebugMenuButton() {
        navigationManager.navigate(R.id.action_fragment_menu_to_fragment_debug);
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

    private void checkActiveItem() {
        if (layoutManager.isPrimarySecondaryLayoutEnabled()) {
            NavDestination navDestination = mSecondaryNavController.getCurrentDestination();
            if (navDestination != null) {
                NavArgument arg = navDestination.getArguments().get(NavigationArgs.MENU_CHECKED_ITEM);
                if (arg != null && arg.getType() == NavType.ReferenceType && arg.isDefaultValuePresent()) {
                    int ref = (int) arg.getDefaultValue();
                    MenuItem menuItem = mNavigationView.getMenu().findItem(ref);
                    if (menuItem != null) {
                        menuItem.setCheckable(true);
                        menuItem.setChecked(true);
                    }
                }
            }
        }
    }
}
