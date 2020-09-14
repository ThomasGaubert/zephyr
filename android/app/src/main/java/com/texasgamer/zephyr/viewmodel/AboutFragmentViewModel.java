package com.texasgamer.zephyr.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.analytics.ZephyrEvent;
import com.texasgamer.zephyr.util.log.LogEntry;
import com.texasgamer.zephyr.util.log.LogLevel;
import com.texasgamer.zephyr.util.navigation.NavigationUtils;
import com.texasgamer.zephyr.util.threading.ZephyrExecutors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * ViewModel for {@link com.texasgamer.zephyr.fragment.AboutFragment}.
 */
public class AboutFragmentViewModel extends BaseViewModel {

    private static final String LOG_TAG = "AboutFragmentViewModel";

    private MutableLiveData<String> mVersionInfo = new MutableLiveData<>();
    private boolean mShowExtendedVersionInfo;

    public AboutFragmentViewModel(Application application) {
        super(application);
        updateVersionText();
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    public LiveData<String> getVersion() {
        return mVersionInfo;
    }

    public String getCopyright() {
        return mResourceProvider.getString(R.string.about_author, Calendar.getInstance().get(Calendar.YEAR));
    }

    public void onClickTitle() {
        mShowExtendedVersionInfo = !mShowExtendedVersionInfo;
        updateVersionText();
    }

    public void onClickExportLogsBtn(@NonNull View view) {
        mLogger.log(LogLevel.INFO, LOG_TAG, "Exporting logs...");
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            Context context = view.getContext();
            File file = new File(context.getCacheDir(), "logs/zephyr-logs.txt");

            boolean readyToExport = false;
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    readyToExport = file.getParentFile().mkdirs();
                } else {
                    mLogger.log(LogLevel.ERROR, LOG_TAG, "Unable to export logs: parent file is null");
                }
            } else {
                readyToExport = true;
            }

            if (!readyToExport) {
                mLogger.log(LogLevel.ERROR, LOG_TAG, "Unable to export logs: not ready to export");
                return;
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

            Intent intent = ShareCompat.IntentBuilder.from((Activity) context)
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

            context.startActivity(intent);
        });
    }

    public void onClickPrivacyBtn(@NonNull View view) {
        NavigationUtils.openUrl(view.getContext(), Constants.ZEPHYR_PRIVACY_URL);
    }

    public void onClickGitHubBtnk(@NonNull View view) {
        mAnalyticsManager.logEvent(ZephyrEvent.Navigation.GITHUB);
        NavigationUtils.openUrl(view.getContext(), Constants.ZEPHYR_GITHUB_URL);
    }

    public void onClickLicensesBtn(@NonNull View view) {
        mAnalyticsManager.logEvent(ZephyrEvent.Navigation.LICENSES);
        mNavigationManager.getCurrentNavController(view).navigate(R.id.action_fragment_about_to_fragment_licenses);
    }

    private void updateVersionText() {
        mVersionInfo.setValue(mShowExtendedVersionInfo
                ? String.format(Locale.getDefault(), "%s - API: %d - DB: %d", BuildConfig.VERSION_NAME, Constants.ZEPHYR_API_VERSION, Constants.DB_VERSION)
                : BuildConfig.VERSION_NAME);
    }
}
