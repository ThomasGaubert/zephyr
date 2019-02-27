package com.texasgamer.zephyr.injection.modules;

import android.content.Context;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

/**
 * BaseViewModel module.
 */
@Module
public class BaseViewModelModule {

    private final Context mContext;

    public BaseViewModelModule(@NonNull Context context) {
        mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }
}
