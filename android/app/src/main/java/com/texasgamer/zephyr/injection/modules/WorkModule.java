package com.texasgamer.zephyr.injection.modules;

import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.worker.IWorkManager;
import com.texasgamer.zephyr.worker.ZephyrWorkManager;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

@Module
public class WorkModule {
    @Provides
    @Singleton
    IWorkManager providePreferenceManager(@NonNull ILogger logger) {
        return new ZephyrWorkManager(logger);
    }
}
