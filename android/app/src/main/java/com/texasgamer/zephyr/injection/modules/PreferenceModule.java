package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import com.texasgamer.zephyr.util.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferenceModule {
    @Provides
    @Singleton
    PreferenceManager providePreferenceManager(Context context) {
        return new PreferenceManager(context);
    }
}
