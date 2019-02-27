package com.texasgamer.zephyr.db.migrations;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Database migration factory.
 */
public final class DatabaseMigrationFactory {

    private static final List<IZephyrDatabaseMigration> MIGRATIONS = new ArrayList<>();

    private DatabaseMigrationFactory() {
    }

    static {
        // Add migrations here
    }

    @NonNull
    public static Migration[] getMigrations() {
        Migration[] roomMigrations = new Migration[MIGRATIONS.size()];
        for (int x = 0; x < roomMigrations.length; x++) {
            IZephyrDatabaseMigration zephyrMigration = MIGRATIONS.get(x);
            roomMigrations[x] = new Migration(zephyrMigration.fromVersion(), zephyrMigration.toVersion()) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    zephyrMigration.migrate(database);
                }
            };
        }

        return roomMigrations;
    }
}
