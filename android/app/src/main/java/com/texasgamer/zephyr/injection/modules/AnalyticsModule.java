package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import com.texasgamer.zephyr.util.analytics.AnalyticsManager;
import com.texasgamer.zephyr.util.analytics.IAnalyticsManager;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

@Module
public class AnalyticsModule {
    @Provides
    @Singleton
    IAnalyticsManager provideLogger(@NonNull Context context, @NonNull ILogger logger, @NonNull IConfigManager configManager) {
        return new AnalyticsManager(context, logger, configManager);
    }
}
