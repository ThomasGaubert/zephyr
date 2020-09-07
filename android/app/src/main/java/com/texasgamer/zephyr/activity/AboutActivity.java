package com.texasgamer.zephyr.activity;

import androidx.annotation.ColorRes;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;

/**
 * About activity.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @ColorRes
    @Override
    protected int getNavigationBarColor() {
        return R.color.primary;
    }

    @Override
    protected boolean supportsEdgeToEdgeNavigation() {
        return false;
    }
}
