package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.theme.IThemeManager;
import com.texasgamer.zephyr.util.theme.ThemeManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Theme module.
 */
@Module
public class ThemeModule {
    @Provides
    @Singleton
    IThemeManager provideThemeManager(@NonNull Context context, @NonNull IPreferenceManager preferenceManager) {
        return new ThemeManager(context, preferenceManager);
    }
}
