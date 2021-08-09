package com.texasgamer.zephyr.network;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;

/**
 * OkHttpClient factory.
 */
public class OkHttpClientFactory implements IOkHttpClientFactory {

    @NonNull
    public OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient()
                .newBuilder();

        return builder.build();
    }
}
