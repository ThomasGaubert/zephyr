package com.texasgamer.zephyr.db.migrations;

import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * v1 to v2 migration. Adds app_info table which stores the AppInfoEntity.
 */
public class V1ToV2AddAppInfoTable implements IZephyrDatabaseMigration {

    public int fromVersion() {
        return 1;
    }

    public int toVersion() {
        return 2;
    }

    public void migrate(@NonNull SupportSQLiteDatabase database) {
        // Create app_info table
        database.execSQL("CREATE TABLE IF NOT EXISTS app_info (packageName TEXT NOT NULL, title TEXT, color INTEGER NOT NULL, PRIMARY KEY(packageName))");
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_app_info_packageName ON app_info (packageName)");

        // Populate app_info table with data from notification_preferences
        database.execSQL("INSERT INTO `app_info` (packageName, title, color) SELECT packageName, title, color FROM `notification_preferences`");

        // Re-create notification_preferences table with updated columns
        database.execSQL("CREATE TABLE notification_preferences_migrated (packageName TEXT NOT NULL, enabled INTEGER NOT NULL, PRIMARY KEY(packageName))");
        database.execSQL("INSERT INTO notification_preferences_migrated (packageName, enabled) SELECT packageName, enabled FROM notification_preferences");
        database.execSQL("DROP TABLE notification_preferences");
        database.execSQL("ALTER TABLE notification_preferences_migrated RENAME TO notification_preferences");
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_notification_preferences_packageName ON notification_preferences (packageName)");
    }
}