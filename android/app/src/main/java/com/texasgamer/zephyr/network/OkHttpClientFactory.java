package com.texasgamer.zephyr.network;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.flipper.IFlipperManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * OkHttpClient factory.
 */
public class OkHttpClientFactory implements IOkHttpClientFactory {

    private IFlipperManager mFlipperManager;

    public OkHttpClientFactory(IFlipperManager flipperManager) {
        mFlipperManager = flipperManager;
    }

    @NonNull
    public OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient()
                .newBuilder();

        Interceptor flipperInterceptor = mFlipperManager.getOkHttpInterceptor();
        if (flipperInterceptor != null) {
            builder.addInterceptor(flipperInterceptor);
        }

        return builder.build();
    }
}
