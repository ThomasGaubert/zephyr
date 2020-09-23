package com.texasgamer.zephyr.util

import android.content.res.Resources
import androidx.annotation.Dimension
import androidx.annotation.Px

/**
 * Dimension utilities.
 */
object DimenUtils {
    @JvmStatic
    @Px
    fun dpToPx(@Dimension(unit = Dimension.DP) dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    @Dimension(unit = Dimension.DP)
    fun pxToDp(@Px px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }
}