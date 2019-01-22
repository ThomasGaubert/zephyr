package com.texasgamer.zephyr.injection.modules;

import com.texasgamer.zephyr.db.ZephyrDatabase;

import androidx.annotation.Nullable;
import dagger.Module;
import dagger.Provides;

@Module
public class EagerModule {

    @Provides
    @Nullable
    Void provideEager(ZephyrDatabase database) {
        // Parameters are build on start, use with caution!
        return null;
    }
}
