package com.texasgamer.zephyr.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.Navigation;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.activity.LicensesActivity;
import com.texasgamer.zephyr.util.NavigationUtils;
import com.texasgamer.zephyr.util.analytics.ZephyrEvent;
import com.texasgamer.zephyr.util.log.LogEntry;
import com.texasgamer.zephyr.util.log.LogLevel;
import com.texasgamer.zephyr.util.threading.ZephyrExecutors;
import com.texasgamer.zephyr.viewmodel.AboutFragmentViewModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Fragment which displays version info, etc.
 */
public class AboutFragment extends BaseFragment<AboutFragmentViewModel, ViewDataBinding> {

    private static final String LOG_TAG = "AboutFragment";

    @BindView(R.id.about_version)
    TextView mVersionTextView;
    @BindView(R.id.about_author)
    TextView mAuthorTextView;

    private boolean mExtendedVersionEnabled;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        updateVersionText(false);
        updateAuthorText();
    }

    @Override
    @LayoutRes
    protected int getFragmentLayout() {
        return R.layout.fragment_about;
    }

    @Override
    protected void setViewBindings(@NonNull View view) {

    }

    @Override
    protected AboutFragmentViewModel onCreateViewModel() {
        return new AboutFragmentViewModel(getActivity().getApplication());
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
            Context context = Objects.requireNonNull(getContext());
            File file = new File(context.getCacheDir(), "logs/zephyr-logs.txt");
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

            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);

            Intent intent = ShareCompat.IntentBuilder.from(Objects.requireNonNull(getActivity()))
                    .setType("text/plain")
                    .setStream(contentUri)
                    .setChooserTitle(R.string.about_export_logs)
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            List<ResolveInfo> resolvedIntentActivities = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                String packageName = resolvedIntentInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
//        NavigationUtils.openActivity(view.getContext(), LicensesActivity.class);
        Navigation.findNavController(view).navigate(R.id.licenses_fragment);
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
}
