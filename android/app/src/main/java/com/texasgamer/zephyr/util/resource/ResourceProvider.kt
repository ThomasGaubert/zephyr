package com.texasgamer.zephyr.util.resource

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * Implementation of [IResourceProvider] which uses [Context] to resolve resources.
 */
class ResourceProvider(private val mContext: Context) : IResourceProvider {
    override fun getString(@StringRes stringRes: Int): String {
        return mContext.getString(stringRes)
    }

    override fun getString(@StringRes stringRes: Int, vararg formatArgs: Any): String {
        return mContext.getString(stringRes, *formatArgs)
    }

    override fun getDrawable(@DrawableRes drawableRes: Int): Drawable? {
        return ContextCompat.getDrawable(mContext, drawableRes)
    }
}