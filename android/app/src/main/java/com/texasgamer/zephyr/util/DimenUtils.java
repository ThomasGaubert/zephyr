package com.texasgamer.zephyr.util;

import android.content.res.Resources;

import androidx.annotation.Dimension;
import androidx.annotation.Px;

public class DimenUtils {

    @Px
    public static int dpToPx(@Dimension(unit = Dimension.DP) int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Dimension(unit = Dimension.DP)
    public static int pxToDp(@Px int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
