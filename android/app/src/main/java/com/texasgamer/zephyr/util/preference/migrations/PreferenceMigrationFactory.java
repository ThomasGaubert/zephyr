package com.texasgamer.zephyr.util.preference.migrations;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class PreferenceMigrationFactory {

    private static final List<IZephyrPreferenceMigration> MIGRATIONS = new ArrayList<>();

    static {
        // Add migrations here
    }

    @NonNull
    public static List<IZephyrPreferenceMigration> getMigrations(int fromVersion, int toVersion) {
        List<IZephyrPreferenceMigration> migrations = new ArrayList<>();

        for (IZephyrPreferenceMigration migration : MIGRATIONS) {
            if (migration.fromVersion() >= fromVersion && migration.toVersion() <= toVersion) {
                migrations.add(migration);
            }
        }

        return migrations;
    }
}
