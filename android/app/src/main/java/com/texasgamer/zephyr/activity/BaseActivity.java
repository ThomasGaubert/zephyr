package com.texasgamer.zephyr.activity;

import android.os.Bundle;

import com.texasgamer.zephyr.util.analytics.IAnalyticsManager;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;

import javax.inject.Inject;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

/**
 * Base activity that handles common routines.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected IPreferenceManager mPreferenceManager;

    @Inject
    IAnalyticsManager analyticsManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies();

        if (analyticsManager == null || mPreferenceManager == null) {
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
