package com.texasgamer.zephyr.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.Nullable;

/**
 * Vibration helper functions.
 */
public final class VibrationUtils {

    private VibrationUtils() {
    }

    public static void vibrate(@Nullable Context context) {
        if (context == null) {
            return;
        }

        vibrate(context, 100);
    }

    public static void vibrate(@Nullable Context context, int length) {
        if (context == null) {
            return;
        }

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(length, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(length);
            }
        }
    }
}
