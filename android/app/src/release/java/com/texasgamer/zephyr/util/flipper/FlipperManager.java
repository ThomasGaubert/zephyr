package com.texasgamer.zephyr.util.flipper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import okhttp3.Interceptor;

/**
 * Flipper manager for release builds.
 */
public class FlipperManager implements IFlipperManager {

    public FlipperManager(@NonNull Context context) {
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    @Nullable
    public Interceptor getOkHttpInterceptor() {
        return null;
    }
}
