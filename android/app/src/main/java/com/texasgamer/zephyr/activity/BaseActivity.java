package com.texasgamer.zephyr.activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.texasgamer.zephyr.util.analytics.IAnalyticsManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.theme.IThemeManager;

import javax.inject.Inject;

import butterknife.ButterKnife;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        injectDependencies();

        if (mAnalyticsManager == null || mPreferenceManager == null || mThemeManager == null) {
            throw new IllegalStateException("Dependencies not fulfilled for this Activity.");
        }

        setupContent();
        setupEdgeToEdgeNavigation();
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

    protected WindowInsets onApplyWindowInsets(@NonNull View view, @NonNull WindowInsets windowInsets) {
        return windowInsets;
    }

    private void setupContent() {
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
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
