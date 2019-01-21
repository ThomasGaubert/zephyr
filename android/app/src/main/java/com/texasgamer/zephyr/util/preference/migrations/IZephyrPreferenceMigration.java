package com.texasgamer.zephyr.util.preference.migrations;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public interface IZephyrPreferenceMigration {
    int fromVersion();
    int toVersion();
    void migrate(@NonNull SharedPreferences sharedPreferences);
}
