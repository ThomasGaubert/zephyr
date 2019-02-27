package com.texasgamer.zephyr.util.config;

/**
 * Config manager interface.
 */
public interface IConfigManager {

    /* Build */
    boolean isDev();

    boolean isProduction();

    /* Firebase */
    boolean isFirebaseEnabled();

    boolean isFirebaseAnalyticsEnabled();

    boolean isFirebaseCrashlyticsEnabled();

    boolean isFirebaseRemoteConfigEnabled();

    /* Features */
    boolean isQrCodeScanningEnabled();
}
