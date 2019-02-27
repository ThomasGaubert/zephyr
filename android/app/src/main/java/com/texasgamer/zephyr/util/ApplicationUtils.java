package com.texasgamer.zephyr.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

/**
 * Application utilities.
 */
public class ApplicationUtils {

    private Context mContext;
    private PreferenceManager mPreferencesManager;
    private PackageManager mPackageManager;

    public ApplicationUtils(@NonNull Context context, @NonNull PreferenceManager preferenceManager) {
        mContext = context;
        mPreferencesManager = preferenceManager;
        mPackageManager = context.getPackageManager();
    }

    public boolean isFre() {
        return mPreferencesManager.getBoolean(PreferenceKeys.PREF_FRE);
    }

    public boolean isUpgrade() {
        return mPreferencesManager.getInt(PreferenceKeys.PREF_LAST_KNOWN_APP_VERSION) < BuildConfigUtils.getVersionCode();
    }

    public boolean isUpgradeFromV1() {
        return mPreferencesManager.getInt(PreferenceKeys.PREF_LAST_KNOWN_APP_VERSION) < Constants.V2_VERSION_CODE;
    }

    public boolean hasNotificationAccess() {
        return NotificationManagerCompat.getEnabledListenerPackages(mContext).contains(BuildConfigUtils.getPackageName());
    }

    @NonNull
    public List<PackageInfo> getInstalledPackages() {
        List<PackageInfo> result = new ArrayList<>();
        for (PackageInfo packageInfo : mPackageManager.getInstalledPackages(PackageManager.GET_META_DATA)) {
            if (mPackageManager.getLaunchIntentForPackage(packageInfo.packageName) != null && !packageInfo.packageName.equals(BuildConfig.APPLICATION_ID)) {
                result.add(packageInfo);
            }
        }

        return result;
    }

    @NonNull
    public List<String> getInstalledPackageNames() {
        List<String> result = new ArrayList<>();
        for (PackageInfo packageInfo : mPackageManager.getInstalledPackages(PackageManager.GET_META_DATA)) {
            if (mPackageManager.getLaunchIntentForPackage(packageInfo.packageName) != null && !packageInfo.packageName.equals(BuildConfig.APPLICATION_ID)) {
                result.add(packageInfo.packageName);
            }
        }

        return result;
    }

    @NonNull
    public PackageInfo getApp(@NonNull String packageName) throws PackageManager.NameNotFoundException {
        return mPackageManager.getPackageInfo(packageName, 0);
    }

    @NonNull
    public String getAppName(@NonNull String packageName) {
        try {
            return getAppName(getApp(packageName));
        } catch (Exception e) {
            return mContext.getString(R.string.app_name_unknown);
        }
    }

    @NonNull
    public String getAppName(@NonNull PackageInfo packageInfo) {
        try {
            return mPackageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
        } catch (Exception e) {
            return mContext.getString(R.string.app_name_unknown);
        }
    }

    @Nullable
    public Drawable getAppIcon(@NonNull String packageName) {
        try {
            return getAppIcon(getApp(packageName));
        } catch (Exception e) {
            return mContext.getDrawable(R.drawable.ic_app_icon_unknown);
        }
    }

    @Nullable
    public Drawable getAppIcon(@NonNull PackageInfo packageInfo) {
        try {
            return mPackageManager.getApplicationIcon(packageInfo.applicationInfo);
        } catch (Exception e) {
            return mContext.getDrawable(R.drawable.ic_app_icon_unknown);
        }
    }

    public ApplicationInfo getAppInfo(@NonNull String packageName) {
        try {
            return mPackageManager.getApplicationInfo(packageName, 0);
        } catch (Exception e) {
            return null;
        }
    }
}
