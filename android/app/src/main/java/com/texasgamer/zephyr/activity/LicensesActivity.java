package com.texasgamer.zephyr.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

/**
 * Open source licenses activity.
 */
public class LicensesActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.licenses_webview)
    WebView mWebView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mToolbar);

        mWebView.loadUrl(String.format("file:///android_asset/license%s%sReport.html",
                capitalizeFirstLetter(BuildConfig.FLAVOR),
                capitalizeFirstLetter(BuildConfig.BUILD_TYPE)));
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
        return R.layout.activity_licenses;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @NonNull
    private String capitalizeFirstLetter(@NonNull String original) {
        return original.length() == 0 ? original : original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
