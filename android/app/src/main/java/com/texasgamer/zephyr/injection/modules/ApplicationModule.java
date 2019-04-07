package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

/**
 * Application module.
 */
@Module
public class ApplicationModule {
    private final ZephyrApplication mApplication;

    public ApplicationModule(@NonNull ZephyrApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    ApplicationUtils provideAppUtilities(Context context, IPreferenceManager preferenceManager) {
        return new ApplicationUtils(context, preferenceManager);
    }
}
