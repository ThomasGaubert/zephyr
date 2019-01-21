package com.texasgamer.zephyr.provider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.texasgamer.zephyr.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppProvider {

    private PackageManager mPackageManager;

    public AppProvider(@NonNull Context context) {
        mPackageManager = context.getPackageManager();
    }

    @NonNull
    public List<PackageInfo> getApps() {
        List<PackageInfo> result = new ArrayList<>();
        for (PackageInfo packageInfo : mPackageManager.getInstalledPackages(PackageManager.GET_META_DATA)) {
            if (mPackageManager.getLaunchIntentForPackage(packageInfo.packageName) != null && !packageInfo.packageName.equals(BuildConfig.APPLICATION_ID)) {
                result.add(packageInfo);
            }
        }

        return result;
    }

    public boolean isAppInstalled(@NonNull String packageName) {
        Intent intent = mPackageManager.getLaunchIntentForPackage(packageName);

        if (intent == null) {
            return false;
        }

        List<ResolveInfo> list = mPackageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public PackageInfo getApp(@NonNull String packageName) throws PackageManager.NameNotFoundException {
        return mPackageManager.getPackageInfo(packageName, 0);
    }

    @Nullable
    public CharSequence getAppName(@NonNull String packageName) {
        try {
            return mPackageManager.getApplicationLabel(getApp(packageName).applicationInfo);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public Drawable getAppIcon(@NonNull String packageName) {
        try{
            return mPackageManager.getApplicationIcon(getApp(packageName).applicationInfo);
        } catch (Exception e) {
            return null;
        }
    }
}
