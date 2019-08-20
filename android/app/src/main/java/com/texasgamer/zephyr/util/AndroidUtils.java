package com.texasgamer.zephyr.util;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.BuildConfig;

/**
 * Android utilities.
 */
public class AndroidUtils {

    public static boolean isAtLeastAndroidQ(@NonNull Context context) {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.P;
    }

    private AndroidUtils() {
    }
}
