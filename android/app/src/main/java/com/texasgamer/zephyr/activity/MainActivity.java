package com.texasgamer.zephyr.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.window.DisplayFeature;
import androidx.window.WindowManager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.fragment.ConnectFragment;
import com.texasgamer.zephyr.fragment.MenuFragment;
import com.texasgamer.zephyr.model.ConnectionStatus;
import com.texasgamer.zephyr.service.SocketService;
import com.texasgamer.zephyr.util.analytics.ZephyrEvent;
import com.texasgamer.zephyr.util.navigation.NavigationArgs;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.view.ZephyrServiceButton;
import com.texasgamer.zephyr.viewmodel.ConnectButtonViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Main activity.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.main_fragment)
    FragmentContainerView mMainFragment;
    @BindView(R.id.bottom_app_bar)
    BottomAppBar mBottomAppBar;
    @BindView(R.id.connect_button)
    ZephyrServiceButton mConnectButton;
    @BindView(R.id.spacer)
    View mSpacer;
    @BindView(R.id.secondary_fragment)
    FragmentContainerView mSecondaryFragment;

    private DrawerArrowDrawable mDrawerArrowDrawable;
    private ConnectButtonViewModel mConnectButtonViewModel;
    private MenuFragment mMenuFragment;
    private ConnectFragment mConnectFragment;
    private WindowManager mWindowManager;
    private NavController mNavController;

    private boolean mIsBackButtonEnabled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Zephyr);
        super.onCreate(savedInstanceState);

        mWindowManager = new WindowManager(this, null);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (navHostFragment != null) {
            mNavController = navHostFragment.getNavController();
        }

        mMenuFragment = new MenuFragment();
        mConnectFragment = new ConnectFragment();
        setupToolbar();

        mConnectButtonViewModel = new ConnectButtonViewModel(ZephyrApplication.getInstance());
        subscribeUi(mConnectButtonViewModel.getIsConnected());

        verifyConnectionStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLayoutManager.isPrimarySecondaryLayoutEnabled()) {
            setupSpacer(mLayoutManager.getPrimaryLayoutWidth(), mLayoutManager.getSpacerWidth());
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setupSpacer(mLayoutManager.getPrimaryLayoutWidth(), mLayoutManager.getSpacerWidth());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mIsBackButtonEnabled) {
                    onBackPressed();
                } else {
                    mAnalyticsManager.logEvent(ZephyrEvent.Action.OPEN_HAMBURGER_MENU);
                    mNavController.navigate(R.id.action_fragment_main_to_fragment_menu);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @ColorRes
    @Override
    protected int getNavigationBarColor() {
        return R.color.primaryDark;
    }

    @Override
    protected WindowInsets onApplyWindowInsets(@NonNull View view, @NonNull WindowInsets windowInsets) {
        super.onApplyWindowInsets(view, windowInsets);
        ConstraintLayout.LayoutParams mainFragmentLayoutParams = new ConstraintLayout.LayoutParams(mMainFragment.getLayoutParams());
        mainFragmentLayoutParams.setMargins(0, windowInsets.getSystemWindowInsetTop(), 0, 0);
        mMainFragment.setLayoutParams(mainFragmentLayoutParams);
        return windowInsets;
    }

    @OnClick(R.id.connect_button)
    public void onClickConnectButton() {
        Intent socketServiceIntent = new Intent(MainActivity.this, SocketService.class);
        boolean isJoinCodeSet = isJoinCodeSet();

        Bundle params = new Bundle();
        params.putBoolean(ZephyrEvent.Parameter.JOIN_CODE_SET, isJoinCodeSet);
        params.putBoolean(ZephyrEvent.Parameter.CONNECTED, mConnectButton.isChecked());
        mAnalyticsManager.logEvent(ZephyrEvent.Action.TAP_CONNECTION_BUTTON, params);

        if (!mConnectButton.isChecked()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(socketServiceIntent);
            } else {
                startService(socketServiceIntent);
            }

            if (!isJoinCodeSet) {
                mConnectFragment.show(getSupportFragmentManager(), mConnectFragment.getTag());
            }
        } else {
            stopService(socketServiceIntent);
        }
    }

    @OnLongClick(R.id.connect_button)
    public boolean onLongClickConnectButton() {
        mAnalyticsManager.logEvent(ZephyrEvent.Action.LONG_PRESS_CONNECTION_BUTTON);
        mConnectFragment.show(getSupportFragmentManager(), mConnectFragment.getTag());
        return true;
    }

    private void setupToolbar() {
        mDrawerArrowDrawable = new DrawerArrowDrawable(this);
        mDrawerArrowDrawable.setColor(ContextCompat.getColor(this, R.color.white));
        mBottomAppBar.setNavigationIcon(mDrawerArrowDrawable);
        setSupportActionBar(mBottomAppBar);

        mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            boolean isBackButtonEnabled = destination.getId() != R.id.fragment_main;

            if (arguments != null && isBackButtonEnabled) {
                isBackButtonEnabled = arguments.getBoolean(NavigationArgs.SHOW_BACK_ON_NAV, true);
            }

            if (isBackButtonEnabled == mIsBackButtonEnabled) {
                return;
            }

            mIsBackButtonEnabled = isBackButtonEnabled;

            mConnectButton.setVisibility(mIsBackButtonEnabled ? View.GONE : View.VISIBLE);

            // 0 - hamburger menu, 1 - back button
            ValueAnimator anim = mIsBackButtonEnabled ? ValueAnimator.ofFloat(0, 1) : ValueAnimator.ofFloat(1, 0);
            anim.addUpdateListener(valueAnimator -> {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                mDrawerArrowDrawable.setProgress(slideOffset);
            });
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(400);
            anim.start();
        });
    }

    private void subscribeUi(LiveData<Boolean> liveData) {
        liveData.observe(this, isServiceRunning -> mConnectButton.setServiceRunning(isServiceRunning));
    }

    private boolean isJoinCodeSet() {
        String joinCode = mPreferenceManager.getString(PreferenceKeys.PREF_JOIN_CODE);
        return joinCode != null && !joinCode.isEmpty();
    }

    private void verifyConnectionStatus() {
        if (!mPreferenceManager.getBoolean(PreferenceKeys.PREF_IS_SOCKET_SERVICE_RUNNING)) {
            mPreferenceManager.putInt(PreferenceKeys.PREF_CONNECTION_STATUS, ConnectionStatus.DISCONNECTED);
        } else if (!SocketService.instanceCreated) {
            mPreferenceManager.putBoolean(PreferenceKeys.PREF_IS_SOCKET_SERVICE_RUNNING, false);
            mPreferenceManager.putInt(PreferenceKeys.PREF_CONNECTION_STATUS, ConnectionStatus.DISCONNECTED);
        }
    }

    private void setupSpacer(@Px int spacerStart, @Px int spacerWidth) {
        ConstraintLayout.LayoutParams mainFragmentOriginalParams = (ConstraintLayout.LayoutParams) mMainFragment.getLayoutParams();
        ConstraintLayout.LayoutParams mainFragmentLayoutParams = new ConstraintLayout.LayoutParams(mMainFragment.getLayoutParams());
        mainFragmentLayoutParams.width = spacerStart;
        mainFragmentLayoutParams.startToStart = mainFragmentOriginalParams.startToStart;
        mainFragmentLayoutParams.endToStart = mainFragmentOriginalParams.endToStart;
        mainFragmentLayoutParams.topToTop = mainFragmentOriginalParams.topToTop;
        mainFragmentLayoutParams.bottomToBottom = mainFragmentOriginalParams.bottomToBottom;
        mMainFragment.setLayoutParams(mainFragmentLayoutParams);

        ConstraintLayout.LayoutParams spacerOriginalParams = (ConstraintLayout.LayoutParams) mSpacer.getLayoutParams();
        ConstraintLayout.LayoutParams spacerLayoutParams = new ConstraintLayout.LayoutParams(mSpacer.getLayoutParams());
        spacerLayoutParams.startToEnd = spacerOriginalParams.startToEnd;
        spacerLayoutParams.endToStart = spacerOriginalParams.endToStart;
        spacerLayoutParams.topToTop = spacerOriginalParams.topToTop;
        spacerLayoutParams.bottomToBottom = spacerOriginalParams.bottomToBottom;
        spacerLayoutParams.width = spacerWidth;
        mSpacer.setLayoutParams(spacerLayoutParams);
        mSpacer.setVisibility(spacerWidth == 0 ? View.GONE : View.VISIBLE);
    }
}
