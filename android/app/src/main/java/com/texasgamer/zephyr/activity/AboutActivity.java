package com.texasgamer.zephyr.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.util.NavigationUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.about_version)
    TextView versionTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        versionTextView.setText(BuildConfig.VERSION_NAME);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }

    @OnClick(R.id.about_github)
    public void onClickGitHubBtn() {
        NavigationUtils.openUrl(getBaseContext(), Constants.ZEPHYR_GITHUB_URL);
    }

    @OnClick(R.id.about_licenses)
    public void onClickLicensesBtn() {
        NavigationUtils.openUrl(getBaseContext(), Constants.ZEPHYR_GITHUB_URL);
    }
}
