package com.texasgamer.zephyr.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.db.dao.AppInfoDao;
import com.texasgamer.zephyr.db.dao.NotificationPreferenceDao;
import com.texasgamer.zephyr.db.entity.AppInfoEntity;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.db.migrations.DatabaseMigrationFactory;

/**
 * Zephyr database.
 */
@Database(entities = {AppInfoEntity.class, NotificationPreferenceEntity.class}, version = Constants.DB_VERSION, exportSchema = false)
public abstract class ZephyrDatabase extends RoomDatabase {

    public abstract AppInfoDao appInfoDao();

    public abstract NotificationPreferenceDao notificationPreferenceDao();

    public static ZephyrDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, ZephyrDatabase.class, Constants.DB_NAME)
                .addMigrations(DatabaseMigrationFactory.getMigrations())
                .fallbackToDestructiveMigration()
                .build();
    }
}
