package com.texasgamer.zephyr.network;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;

/**
 * Interface for OkHttpClient factory.
 */
public interface IOkHttpClientFactory {
    @NonNull
    OkHttpClient getClient();
}
