package com.texasgamer.zephyr.db;

import android.content.Context;

import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.db.dao.NotificationPreferenceDao;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.db.migrations.DatabaseMigrationFactory;
import com.texasgamer.zephyr.service.threading.ZephyrExecutors;
import com.texasgamer.zephyr.util.log.ILogger;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {NotificationPreferenceEntity.class}, version = Constants.DB_VERSION, exportSchema = false)
public abstract class ZephyrDatabase extends RoomDatabase {

    public abstract NotificationPreferenceDao notificationPreferenceDao();

    public static ZephyrDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, ZephyrDatabase.class, Constants.DB_NAME)
                .addCallback(new OnDbOpenCallback())
                .addMigrations(DatabaseMigrationFactory.getMigrations())
                .build();
    }

    private static class OnDbOpenCallback extends Callback {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            List<String> packageNames = ZephyrApplication.getApplicationComponent().applicationUtilities().getInstalledPackageNames();
            List<NotificationPreferenceEntity> notificationPreferences = new ArrayList<>();

            for (String packageName : packageNames) {
                notificationPreferences.add(new NotificationPreferenceEntity(packageName, true));
            }

            ZephyrExecutors.getDiskExecutor().execute(() -> {
                ZephyrDatabase database = ZephyrApplication.getApplicationComponent().database();
                database.runInTransaction(() -> {
                    database.notificationPreferenceDao().insertNotificationPreferences(notificationPreferences);
                    database.notificationPreferenceDao().removeOrphanedNotificationPreferences(packageNames.toArray(new String[0]));
                });
            });
        }
    }
}
