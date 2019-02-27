package com.texasgamer.zephyr.util;

import com.texasgamer.zephyr.BuildConfig;

/**
 * BuildConfig utilities.
 */
public final class BuildConfigUtils {

    private BuildConfigUtils() {
    }

    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static String getPackageName() {
        return BuildConfig.APPLICATION_ID;
    }

    public static String getFlavor() {
        return BuildConfig.FLAVOR;
    }

    public static String getBuildType() {
        return BuildConfig.BUILD_TYPE;
    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
