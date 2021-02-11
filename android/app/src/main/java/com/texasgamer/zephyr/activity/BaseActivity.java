package com.texasgamer.zephyr.activity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.texasgamer.zephyr.util.analytics.IAnalyticsManager;
import com.texasgamer.zephyr.util.layout.ILayoutManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.navigation.INavigationManager;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.theme.IThemeManager;

import javax.inject.Inject;

/**
 * Base activity that handles common routines.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected ILogger mLogger;
    @Inject
    protected IPreferenceManager mPreferenceManager;
    @Inject
    protected IAnalyticsManager mAnalyticsManager;
    @Inject
    protected IThemeManager mThemeManager;
    @Inject
    protected ILayoutManager mLayoutManager;
    @Inject
    protected INavigationManager mNavigationManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies();

        if (mAnalyticsManager == null || mPreferenceManager == null || mThemeManager == null) {
            throw new IllegalStateException("Dependencies not fulfilled for this Activity.");
        }

        mLayoutManager.setCurrentActivity(this);

        setupContent();
        setupEdgeToEdgeNavigation();
    }

    @Override
    @CallSuper
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        mLayoutManager.onConfigurationChanged(configuration);
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    protected abstract void injectDependencies();

    @ColorRes
    protected int getNavigationBarColor() {
        return -1;
    }

    protected boolean supportsEdgeToEdgeNavigation() {
        return true;
    }

    @CallSuper
    protected WindowInsets onApplyWindowInsets(@NonNull View view, @NonNull WindowInsets windowInsets) {
        // Some devices don't support edge to edge navigation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && supportsEdgeToEdgeNavigation()) {
            int colorRes = windowInsets.getSystemWindowInsetBottom() == 0 ? getNavigationBarColor() : android.R.color.transparent;
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, colorRes));
        }

        return windowInsets;
    }

    private void setupContent() {
        setContentView(getLayoutResource());
    }

    private void setupEdgeToEdgeNavigation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && supportsEdgeToEdgeNavigation()) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            decorView.setOnApplyWindowInsetsListener(this::onApplyWindowInsets);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getNavigationBarColor() != -1) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, getNavigationBarColor()));
        }
    }
}
