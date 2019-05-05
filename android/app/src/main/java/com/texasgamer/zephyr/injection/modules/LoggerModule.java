package com.texasgamer.zephyr.injection.modules;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogSanitizer;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogSanitizer;
import com.texasgamer.zephyr.util.log.Logger;
import com.texasgamer.zephyr.util.privacy.IPrivacyManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Logger module.
 */
@Module
public class LoggerModule {
    @Provides
    @Singleton
    ILogger provideLogger(@NonNull ILogSanitizer logSanitizer, @NonNull IPrivacyManager privacyManager) {
        return new Logger(logSanitizer, privacyManager);
    }

    @Provides
    @Singleton
    ILogSanitizer provideLogSanitizer(IConfigManager configManager) {
        return new LogSanitizer(configManager);
    }
}
