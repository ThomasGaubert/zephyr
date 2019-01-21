package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.texasgamer.zephyr.provider.AppProvider;
import com.texasgamer.zephyr.util.NotificationPreferenceManager;
import com.texasgamer.zephyr.util.PreferenceManager;
import com.texasgamer.zephyr.util.log.ILogger;

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

    @Provides
    @Singleton
    NotificationPreferenceManager provideNotificationPreferenceManager(Context context,
                                                                       ILogger logger,
                                                                       PreferenceManager preferenceManager,
                                                                       AppProvider appProvider,
                                                                       Gson gson) {
        return new NotificationPreferenceManager(context, logger, preferenceManager, appProvider, gson);
    }
}
