package com.texasgamer.zephyr.util.resource;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * Implementation of {@link IResourceProvider} which uses {@link Context} to resolve resources.
 */
public class ResourceProvider implements IResourceProvider {

    private Context mContext;

    public ResourceProvider(@NonNull Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public String getString(@StringRes int stringRes) {
        return mContext.getString(stringRes);
    }

    @NonNull
    @Override
    public String getString(@StringRes int stringRes, Object... formatArgs) {
        return mContext.getString(stringRes, formatArgs);
    }

    @Nullable
    @Override
    public Drawable getDrawable(@DrawableRes int drawableRes) {
        return ContextCompat.getDrawable(mContext, drawableRes);
    }
}
