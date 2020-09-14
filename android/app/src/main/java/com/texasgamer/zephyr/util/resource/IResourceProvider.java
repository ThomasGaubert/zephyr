package com.texasgamer.zephyr.util.resource;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * Resource provider interface.
 */
public interface IResourceProvider {
    @NonNull
    String getString(@StringRes int stringRes);

    @NonNull
    String getString(@StringRes int stringRes, Object... formatArgs);
}
