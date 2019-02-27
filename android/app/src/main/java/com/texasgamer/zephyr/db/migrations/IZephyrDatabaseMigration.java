package com.texasgamer.zephyr.db.migrations;

import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Database migration interface.
 */
public interface IZephyrDatabaseMigration {
    int fromVersion();
    int toVersion();
    void migrate(@NonNull SupportSQLiteDatabase database);
}
