package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.provider.AppProvider;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.PreferenceManager;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

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
    ApplicationUtils provideAppUtilities(Context context, PreferenceManager preferenceManager) {
        return new ApplicationUtils(context, preferenceManager);
    }

    @Provides
    @Singleton
    AppProvider provideAppProvider(Context context) {
        return new AppProvider(context);
    }
}
