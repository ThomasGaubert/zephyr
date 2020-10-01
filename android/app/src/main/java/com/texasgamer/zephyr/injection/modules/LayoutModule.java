package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.layout.ILayoutManager;
import com.texasgamer.zephyr.util.layout.LayoutManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Layout module.
 */
@Module
public class LayoutModule {
    @Provides
    @Singleton
    ILayoutManager provideLayoutManager(@NonNull Context context) {
        return new LayoutManager(context);
    }
}
