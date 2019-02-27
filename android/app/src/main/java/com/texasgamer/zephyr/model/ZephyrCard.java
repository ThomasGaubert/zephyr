package com.texasgamer.zephyr.model;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * Represents card in MainFragment.
 */
public class ZephyrCard {

    @ZephyrCardType
    private int mType;
    @StringRes
    private int mTitle;
    @StringRes
    private int mBody;
    private View.OnClickListener mOnClickListener;

    public ZephyrCard(@ZephyrCardType int type, @StringRes int title, @StringRes int body) {
        mType = type;
        mTitle = title;
        mBody = body;
    }

    @ZephyrCardType
    public int getType() {
        return mType;
    }

    public void setType(@ZephyrCardType int type) {
        mType = type;
    }

    @StringRes
    public int getTitle() {
        return mTitle;
    }

    public void setTitle(@StringRes int title) {
        mTitle = title;
    }

    @StringRes
    public int getBody() {
        return mBody;
    }

    public void setBody(@StringRes int body) {
        mBody = body;
    }

    @Nullable
    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(@Nullable View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
