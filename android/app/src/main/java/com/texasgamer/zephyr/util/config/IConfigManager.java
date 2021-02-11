package com.texasgamer.zephyr.util.config;

/**
 * Config manager interface.
 */
public interface IConfigManager {

    /* Build */
    boolean isDebug();

    boolean isRelease();

    boolean isDev();

    boolean isBeta();

    boolean isProduction();

    /* Firebase */
    boolean isFirebaseEnabled();

    boolean isFirebaseAnalyticsEnabled();

    boolean isFirebaseCrashlyticsEnabled();

    boolean isFirebaseRemoteConfigEnabled();

    /* Features */
    boolean isQrCodeScanningEnabled();

    boolean isQrCodeIndicatorsEnabled();

    boolean isDiscoveryEnabled();

    boolean isThemingEnabled();

    boolean isDebugMenuEnabled();
}
