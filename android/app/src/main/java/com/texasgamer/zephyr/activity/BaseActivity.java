package com.texasgamer.zephyr.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    }

    private void setupContent() {
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    protected abstract void injectDependencies();
}
