package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferenceModule {
    @Provides
    @Singleton
    PreferenceManager providePreferenceManager(Context context, ILogger logger) {
        return new PreferenceManager(context, logger);
    }
}
