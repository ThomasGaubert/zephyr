package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import com.texasgamer.zephyr.util.config.ConfigManager;
import com.texasgamer.zephyr.util.config.IConfigManager;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

/**
 * Config module.
 */
@Module
public class ConfigModule {
    @Provides
    @Singleton
    IConfigManager provideLogger(@NonNull Context context) {
        return new ConfigManager(context);
    }
}
