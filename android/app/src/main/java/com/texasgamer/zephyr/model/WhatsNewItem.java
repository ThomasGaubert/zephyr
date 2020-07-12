package com.texasgamer.zephyr.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

/**
 * Represents item in "What's new" list.
 */
public class WhatsNewItem {

    @DrawableRes
    private int mIconRes;
    @StringRes
    private int mTitleRes;
    @StringRes
    private int mBodyRes;

    public WhatsNewItem(@DrawableRes int iconRes, @StringRes int titleRes, @StringRes int bodyRes) {
        mIconRes = iconRes;
        mTitleRes = titleRes;
        mBodyRes = bodyRes;
    }

    @DrawableRes
    public int getIconRes() {
        return mIconRes;
    }

    @StringRes
    public int getTitleRes() {
        return mTitleRes;
    }

    @StringRes
    public int getBodyRes() {
        return mBodyRes;
    }
}
