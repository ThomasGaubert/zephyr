package com.texasgamer.zephyr.injection.modules;

import com.texasgamer.zephyr.provider.IZephyrCardProvider;
import com.texasgamer.zephyr.provider.ZephyrCardProvider;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

/**
 * Provider module.
 */
@Module
public class ProviderModule {
    @Singleton
    @Provides
    IZephyrCardProvider getZephyrCardProvider(@NonNull ApplicationUtils applicationUtils,
                                              @NonNull IConfigManager configManager,
                                              @NonNull IPreferenceManager preferenceManager) {
        return new ZephyrCardProvider(applicationUtils, configManager, preferenceManager);
    }
}
