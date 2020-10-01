package com.texasgamer.zephyr.injection.modules;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.layout.ILayoutManager;
import com.texasgamer.zephyr.util.navigation.INavigationManager;
import com.texasgamer.zephyr.util.navigation.NavigationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Network module.
 */
@Module
public class NavigationModule {
    @Provides
    @Singleton
    INavigationManager provideNavigationManager(@NonNull ILayoutManager layoutManager) {
        return new NavigationManager(layoutManager);
    }
}
