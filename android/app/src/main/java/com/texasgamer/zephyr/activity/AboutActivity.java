package com.texasgamer.zephyr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.NavigationUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * About activity.
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.about_version)
    TextView mVersionTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVersionTextView.setText(BuildConfig.VERSION_NAME);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @OnClick(R.id.about_github)
    public void onClickGitHubBtn(View view) {
        NavigationUtils.openUrl(view.getContext(), Constants.ZEPHYR_GITHUB_URL);
    }

    @OnClick(R.id.about_privacy)
    public void onClickPrivacyBtn(View view) {
        NavigationUtils.openUrl(view.getContext(), Constants.ZEPHYR_PRIVACY_URL);
    }

    @OnClick(R.id.about_licenses)
    public void onClickLicensesBtn(View view) {
        NavigationUtils.openActivity(view.getContext(), LicensesActivity.class);
    }
}
