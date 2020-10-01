package com.texasgamer.zephyr.util

import com.texasgamer.zephyr.BuildConfig

/**
 * BuildConfig utilities.
 */
object BuildConfigUtils {
    @JvmStatic
    val versionCode: Int
        get() = BuildConfig.VERSION_CODE

    val versionName: String
        get() = BuildConfig.VERSION_NAME

    @JvmStatic
    val packageName: String
        get() = BuildConfig.APPLICATION_ID

    val flavor: String
        get() = BuildConfig.FLAVOR

    val buildType: String
        get() = BuildConfig.BUILD_TYPE

    val isDebug: Boolean
        get() = BuildConfig.DEBUG
}