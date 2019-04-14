package com.texasgamer.zephyr.util.preference.migrations;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Preference migration factory.
 */
public final class PreferenceMigrationFactory {

    private static final List<IZephyrPreferenceMigration> MIGRATIONS = new ArrayList<>();

    private PreferenceMigrationFactory() {
    }

    static {
        // Add migrations here in ascending order
    }

    public static IZephyrPreferenceMigration getInitialMigration() {
        return new InitialMigration();
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
