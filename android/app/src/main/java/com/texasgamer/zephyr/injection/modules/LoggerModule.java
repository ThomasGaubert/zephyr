package com.texasgamer.zephyr.injection.modules;

import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.Logger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LoggerModule {
    @Provides
    @Singleton
    ILogger provideLogger() {
        return new Logger();
    }
}
