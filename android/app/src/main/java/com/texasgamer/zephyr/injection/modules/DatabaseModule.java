package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import com.texasgamer.zephyr.db.ZephyrDatabase;
import com.texasgamer.zephyr.db.dao.NotificationPreferenceDao;
import com.texasgamer.zephyr.db.repository.EmptyDataSource;
import com.texasgamer.zephyr.db.repository.IRepository;
import com.texasgamer.zephyr.db.repository.NotificationPreferenceDataSource;
import com.texasgamer.zephyr.db.repository.NotificationPreferenceRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Database module.
 */
@Module
public class DatabaseModule {

    @Singleton
    @Provides
    ZephyrDatabase providesDatabase(Context context) {
        return ZephyrDatabase.buildDatabase(context);
    }

    @Singleton
    @Provides
    IRepository providesEmptyRepository() {
        return new EmptyDataSource();
    }

    @Singleton
    @Provides
    NotificationPreferenceDao providesNotificationPreferenceDao(ZephyrDatabase zephyrDatabase) {
        return zephyrDatabase.notificationPreferenceDao();
    }

    @Singleton
    @Provides
    NotificationPreferenceRepository providesNotificationPreferenceRepository(NotificationPreferenceDao notificationPreferenceDao) {
        return new NotificationPreferenceDataSource(notificationPreferenceDao);
    }
}
