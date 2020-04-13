package com.texasgamer.zephyr.injection.modules;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.network.IOkHttpClientFactory;
import com.texasgamer.zephyr.network.OkHttpClientFactory;
import com.texasgamer.zephyr.util.flipper.IFlipperManager;

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
}
