package com.texasgamer.zephyr.util.config;

import android.content.Context;
import android.content.pm.PackageManager;

import com.texasgamer.zephyr.Constants;

import androidx.annotation.NonNull;

public class ConfigManager implements IConfigManager {

    private static final String LOG_TAG = "ConfigManager";

    private Context context;
    private ZephyrConfigProvider zephyrConfigProvider;

    public ConfigManager(@NonNull Context context) {
        this.context = context;
        this.zephyrConfigProvider = new ZephyrConfigProvider(context, this);
    }

    @Override
    public boolean isFirebaseEnabled() {
        return Constants.FIREBASE_ENABLED;
    }

    @Override
    public boolean isFirebaseAnalyticsEnabled() {
        return isFirebaseEnabled() && Constants.FIREBASE_ANALYTICS_ENABLED;
    }

    @Override
    public boolean isFirebaseCrashlyticsEnabled() {
        return isFirebaseEnabled() && Constants.FIREBASE_CRASHLYTICS_ENABLED;
    }

    @Override
    public boolean isFirebaseRemoteConfigEnabled() {
        return isFirebaseEnabled() && Constants.FIREBASE_REMOTE_CONFIG_ENABLED;
    }

    @Override
    public boolean isQrCodeScanningEnabled() {
        return isFirebaseEnabled()
                && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
                && zephyrConfigProvider.getBoolean(ConfigKeys.ENABLE_SCAN_QR_CODE);
    }
}
