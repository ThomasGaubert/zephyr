package com.texasgamer.zephyr.injection.modules;

import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogSanitizer;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogSanitizer;
import com.texasgamer.zephyr.util.log.Logger;

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
    ILogger provideLogger(IConfigManager configManager, ILogSanitizer logSanitizer) {
        return new Logger(configManager, logSanitizer);
    }

    @Provides
    @Singleton
    ILogSanitizer provideLogSanitizer(IConfigManager configManager) {
        return new LogSanitizer(configManager);
    }
}
