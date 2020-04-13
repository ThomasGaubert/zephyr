package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.flipper.FlipperManager;
import com.texasgamer.zephyr.util.flipper.IFlipperManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Flipper module.
 */
@Module
public class FlipperModule {
    @Provides
    @Singleton
    IFlipperManager provideFlipperManager(@NonNull Context context) {
        return new FlipperManager(context);
    }
}
