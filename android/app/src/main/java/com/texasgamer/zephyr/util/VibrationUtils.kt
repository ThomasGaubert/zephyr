package com.texasgamer.zephyr.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * Vibration helper functions.
 */
object VibrationUtils {
    @JvmStatic
    fun vibrate(context: Context?) {
        if (context == null) {
            return
        }
        vibrate(context, 100)
    }

    @JvmStatic
    fun vibrate(context: Context?, length: Int) {
        if (context == null) {
            return
        }
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(length.toLong(), VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(length.toLong())
            }
        }
    }
}