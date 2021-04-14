package com.texasgamer.zephyr.injection.modules;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.distribution.DistributionManager;
import com.texasgamer.zephyr.util.distribution.IDistributionManager;
import com.texasgamer.zephyr.util.log.ILogger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Distribution module.
 */
@Module
public class DistributionModule {
    @Provides
    @Singleton
    IDistributionManager provideDistributionManager(@NonNull IConfigManager configManager, @NonNull ILogger logger) {
        return new DistributionManager(configManager, logger);
    }
}
