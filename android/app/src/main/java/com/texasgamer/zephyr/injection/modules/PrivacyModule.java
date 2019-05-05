package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.privacy.IPrivacyManager;
import com.texasgamer.zephyr.util.privacy.PrivacyManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Privacy module.
 */
@Module
public class PrivacyModule {
    @Provides
    @Singleton
    IPrivacyManager providePrivacyManager(@NonNull Context context, @NonNull IConfigManager configManager) {
        return new PrivacyManager(context, configManager);
    }
}
