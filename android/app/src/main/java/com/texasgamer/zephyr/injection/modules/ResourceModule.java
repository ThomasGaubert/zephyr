package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.resource.IResourceProvider;
import com.texasgamer.zephyr.util.resource.ResourceProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Network module.
 */
@Module
public class ResourceModule {
    @Provides
    @Singleton
    IResourceProvider provideResourceProvider(@NonNull Context context) {
        return new ResourceProvider(context);
    }
}
