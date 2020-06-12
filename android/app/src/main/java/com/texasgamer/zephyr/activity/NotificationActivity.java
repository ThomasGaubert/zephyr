package com.texasgamer.zephyr.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import butterknife.BindView;

/**
 * Notification activity.
 */
public class NotificationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setupEdgeToEdgeLayout();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_notifications;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
        if (!mPreferenceManager.getBoolean(PreferenceKeys.PREF_SEEN_MANAGE_NOTIFICATIONS)) {
            mPreferenceManager.putBoolean(PreferenceKeys.PREF_SEEN_MANAGE_NOTIFICATIONS, true);
        }
    }

    private void setupEdgeToEdgeLayout() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        decorView.setOnApplyWindowInsetsListener((v, insets) -> {
            AppBarLayout.LayoutParams toolbarLayoutParams = new AppBarLayout.LayoutParams(mToolbar.getLayoutParams());
            toolbarLayoutParams.setMargins(0, insets.getSystemWindowInsetTop(), 0, 0);
            mToolbar.setLayoutParams(toolbarLayoutParams);
            return insets;
        });
    }
}
