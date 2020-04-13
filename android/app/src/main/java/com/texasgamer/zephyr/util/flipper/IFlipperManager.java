package com.texasgamer.zephyr.util.flipper;

import okhttp3.Interceptor;

/**
 * Flipper manager interface.
 */
public interface IFlipperManager {

    boolean isInitialized();

    Interceptor getOkHttpInterceptor();
}
