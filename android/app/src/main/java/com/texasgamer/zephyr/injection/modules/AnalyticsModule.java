package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.analytics.AnalyticsManager;
import com.texasgamer.zephyr.util.analytics.IAnalyticsManager;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.privacy.IPrivacyManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Analytics module.
 */
@Module
public class AnalyticsModule {
    @Provides
    @Singleton
    IAnalyticsManager provideLogger(@NonNull Context context,
                                    @NonNull ILogger logger,
                                    @NonNull IConfigManager configManager,
                                    @NonNull IPrivacyManager privacyManager) {
        return new AnalyticsManager(context, logger, configManager, privacyManager);
    }
}
