package com.texasgamer.zephyr.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.NavigationUtils;
import com.texasgamer.zephyr.util.analytics.ZephyrEvent;
import com.texasgamer.zephyr.util.log.LogEntry;
import com.texasgamer.zephyr.util.log.LogLevel;
import com.texasgamer.zephyr.util.threading.ZephyrExecutors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * About activity.
 */
public class AboutActivity extends BaseActivity {

    private static final String LOG_TAG = "AboutActivity";

    @BindView(R.id.about_version)
    TextView mVersionTextView;
    @BindView(R.id.about_author)
    TextView mAuthorTextView;

    private boolean mExtendedVersionEnabled;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateVersionText(false);
        updateAuthorText();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @OnClick(R.id.about_title)
    public void onClickTitle(View view) {
        updateVersionText(!mExtendedVersionEnabled);
    }

    @OnClick(R.id.about_export_logs)
    public void onClickExportLogsBtn(View view) {
        mLogger.log(LogLevel.INFO, LOG_TAG, "Exporting logs...");
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            File file = new File(getCacheDir(), "logs/zephyr-logs.txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }

            FileWriter writer;
            try {
                writer = new FileWriter(file);

                for (LogEntry logEntry : mLogger.getLogs()) {
                    writer.write(logEntry.toString() + "\n");
                }

                writer.close();
            } catch (IOException e) {
                mLogger.log(LogLevel.ERROR, LOG_TAG, "Error while exporting logs!", e);
                return;
            }

            Uri contentUri = FileProvider.getUriForFile(AboutActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

            Intent intent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setStream(contentUri)
                    .setChooserTitle(R.string.about_export_logs)
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            List<ResolveInfo> resolvedIntentActivities = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                String packageName = resolvedIntentInfo.activityInfo.packageName;
                grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            startActivity(intent);
        });
    }

    @OnClick(R.id.about_github)
    public void onClickGitHubBtn(View view) {
        mAnalyticsManager.logEvent(ZephyrEvent.Navigation.GITHUB);
        NavigationUtils.openUrl(view.getContext(), Constants.ZEPHYR_GITHUB_URL);
    }

    @OnClick(R.id.about_privacy)
    public void onClickPrivacyBtn(View view) {
        NavigationUtils.openUrl(view.getContext(), Constants.ZEPHYR_PRIVACY_URL);
    }

    @OnClick(R.id.about_licenses)
    public void onClickLicensesBtn(View view) {
        mAnalyticsManager.logEvent(ZephyrEvent.Navigation.LICENSES);
        NavigationUtils.openActivity(view.getContext(), LicensesActivity.class);
    }

    private void updateVersionText(boolean enableExtendedVersion) {
        mExtendedVersionEnabled = enableExtendedVersion;
        String versionText = mExtendedVersionEnabled
                ? String.format(Locale.getDefault(), "%s - API: %d - DB: %d", BuildConfig.VERSION_NAME, Constants.ZEPHYR_API_VERSION, Constants.DB_VERSION)
                : BuildConfig.VERSION_NAME;
        mVersionTextView.setText(versionText);
    }

    private void updateAuthorText() {
        mAuthorTextView.setText(getString(R.string.about_author, Calendar.getInstance().get(Calendar.YEAR)));
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
