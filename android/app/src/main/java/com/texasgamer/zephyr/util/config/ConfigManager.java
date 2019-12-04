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
        return "debug".equals(BuildConfig.BUILD_TYPE);
    }

    @Override
    public boolean isRelease() {
        return "release".equals(BuildConfig.BUILD_TYPE);
    }

    @Override
    public boolean isDev() {
        return "dev".equals(BuildConfig.FLAVOR);
    }

    @Override
    public boolean isBeta() {
        return "beta".equals(BuildConfig.FLAVOR);
    }

    @Override
    public boolean isProduction() {
        return "production".equals(BuildConfig.FLAVOR);
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
        return isFirebaseEnabled() && Constants.FIREBASE_REMOTE_CONFIG_ENABLED && isRelease();
    }

    @Override
    public boolean isFirebasePerformanceMonitoringEnabled() {
        return isFirebaseEnabled() && Constants.FIREBASE_PERFORMANCE_MONITORING_ENABLED && isRelease();
    }

    @Override
    public boolean isQrCodeScanningEnabled() {
        return isFirebaseEnabled()
                && mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
                && mZephyrConfigProvider.getBoolean(ConfigKeys.ENABLE_SCAN_QR_CODE);
    }

    public boolean isQrCodeIndicatorsEnabled() {
        return isDebug() && isQrCodeScanningEnabled();
    }

    @Override
    public boolean isDebugMenuEnabled() {
        return isDev() || isDebug();
    }
}
