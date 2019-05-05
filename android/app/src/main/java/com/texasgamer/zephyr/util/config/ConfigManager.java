package com.texasgamer.zephyr.util.config;

import android.content.Context;
import android.content.pm.PackageManager;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;

import androidx.annotation.NonNull;

/**
 * Config manager.
 */
public class ConfigManager implements IConfigManager {

    private Context mContext;
    private ZephyrConfigProvider mZephyrConfigProvider;

    public ConfigManager(@NonNull Context context) {
        this.mContext = context;
        this.mZephyrConfigProvider = new ZephyrConfigProvider(context, this);
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.BUILD_TYPE.equals("debug");
    }

    @Override
    public boolean isRelease() {
        return BuildConfig.BUILD_TYPE.equals("release");
    }

    @Override
    public boolean isDev() {
        return BuildConfig.FLAVOR.equals("dev");
    }

    @Override
    public boolean isBeta() {
        return BuildConfig.FLAVOR.equals("beta");
    }

    @Override
    public boolean isProduction() {
        return BuildConfig.FLAVOR.equals("production");
    }

    @Override
    public boolean isFirebaseEnabled() {
        return Constants.FIREBASE_ENABLED;
    }

    @Override
    public boolean isFirebaseAnalyticsEnabled() {
        return isFirebaseEnabled() && Constants.FIREBASE_ANALYTICS_ENABLED && isRelease();
    }

    @Override
    public boolean isFirebaseCrashlyticsEnabled() {
        return isFirebaseEnabled() && Constants.FIREBASE_CRASHLYTICS_ENABLED && isRelease();
    }

    @Override
    public boolean isFirebaseRemoteConfigEnabled() {
        return isFirebaseEnabled() && Constants.FIREBASE_REMOTE_CONFIG_ENABLED;
    }

    @Override
    public boolean isQrCodeScanningEnabled() {
        return isFirebaseEnabled()
                && mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
                && mZephyrConfigProvider.getBoolean(ConfigKeys.ENABLE_SCAN_QR_CODE);
    }

    @Override
    public boolean isSettingsMenuEnabled() {
        return isDev();
    }
}
