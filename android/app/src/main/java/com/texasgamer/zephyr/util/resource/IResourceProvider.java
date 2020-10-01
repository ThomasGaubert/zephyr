package com.texasgamer.zephyr.util.resource;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * Resource provider interface.
 */
public interface IResourceProvider {
    @NonNull
    String getString(@StringRes int stringRes);

    @NonNull
    String getString(@StringRes int stringRes, Object... formatArgs);

    @Nullable
    Drawable getDrawable(@DrawableRes int drawableRes);
}
