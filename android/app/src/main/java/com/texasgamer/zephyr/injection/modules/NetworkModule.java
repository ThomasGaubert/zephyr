package com.texasgamer.zephyr.injection.modules;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.texasgamer.zephyr.network.DiscoveryManager;
import com.texasgamer.zephyr.network.IDiscoveryManager;
import com.texasgamer.zephyr.network.IOkHttpClientFactory;
import com.texasgamer.zephyr.network.OkHttpClientFactory;
import com.texasgamer.zephyr.util.flipper.IFlipperManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Network module.
 */
@Module
public class NetworkModule {
    @Provides
    @Singleton
    IOkHttpClientFactory provideOkHttpClientFactory(@NonNull IFlipperManager flipperManager) {
        return new OkHttpClientFactory(flipperManager);
    }

    @Provides
    @Singleton
    IDiscoveryManager provideDiscoveryManager(@NonNull Gson gson, @NonNull ILogger logger, @NonNull IPreferenceManager preferenceManager) {
        return new DiscoveryManager(gson, logger, preferenceManager);
    }
}
