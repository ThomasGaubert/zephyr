package com.texasgamer.zephyr.util

/**
 * String utilities.
 */
object StringUtils {
    const val EMPTY_STRING = ""

    @JvmStatic
    fun isNullOrEmpty(str: String?): Boolean {
        if (str == null) {
            return true
        }
        for (i in 0 until str.length) {
            if (!Character.isWhitespace(str[i])) {
                return false
            }
        }
        return true
    }
}