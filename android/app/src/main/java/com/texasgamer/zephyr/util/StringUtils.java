package com.texasgamer.zephyr.util;

import androidx.annotation.Nullable;

/**
 * String utilities.
 */
public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isNullOrEmpty(@Nullable String str) {
        if (str == null) {
            return true;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
