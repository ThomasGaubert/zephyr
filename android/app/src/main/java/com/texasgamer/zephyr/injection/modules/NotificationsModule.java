package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.notification.NotificationsManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Notifications module.
 */
@Module
public class NotificationsModule {
    @Provides
    @Singleton
    NotificationsManager providePreferenceManager(Context context, ILogger logger) {
        return new NotificationsManager(context, logger);
    }
}
