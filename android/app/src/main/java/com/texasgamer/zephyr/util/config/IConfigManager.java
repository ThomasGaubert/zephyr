package com.texasgamer.zephyr.util.config;

public interface IConfigManager {

    /* Firebase */
    boolean isFirebaseEnabled();

    boolean isFirebaseAnalyticsEnabled();

    boolean isFirebaseCrashlyticsEnabled();

    boolean isFirebaseRemoteConfigEnabled();

    /* Features */
    boolean isQrCodeScanningEnabled();
}
