package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Preference module.
 */
@Module
public class PreferenceModule {
    @Provides
    @Singleton
    IPreferenceManager providePreferenceManager(@NonNull Context context, @NonNull ILogger logger) {
        return new PreferenceManager(context, logger);
    }
}
