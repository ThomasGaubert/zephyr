package com.texasgamer.zephyr.util.flipper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.flipper.android.AndroidFlipperClient;
import com.facebook.flipper.core.FlipperClient;
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin;
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin;
import com.facebook.flipper.plugins.inspector.DescriptorMapping;
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin;
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin;
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor;
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin;
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin;
import com.facebook.soloader.SoLoader;

import okhttp3.Interceptor;

/**
 * Flipper manager for debug builds.
 */
public class FlipperManager implements IFlipperManager {

    private boolean mIsInitialized;
    private Interceptor mOkHttpInterceptor;

    public FlipperManager(@NonNull Context context) {
        SoLoader.init(context, false);

        final FlipperClient flipperClient = AndroidFlipperClient.getInstance(context);
        final DescriptorMapping descriptorMapping = DescriptorMapping.withDefaults();

        final NetworkFlipperPlugin networkPlugin = new NetworkFlipperPlugin();
        mOkHttpInterceptor = new FlipperOkhttpInterceptor(networkPlugin, true);

        flipperClient.addPlugin(new InspectorFlipperPlugin(context, descriptorMapping));
        flipperClient.addPlugin(networkPlugin);
        flipperClient.addPlugin(new SharedPreferencesFlipperPlugin(context));
        flipperClient.addPlugin(CrashReporterPlugin.getInstance());
        flipperClient.addPlugin(new DatabasesFlipperPlugin(context));
        flipperClient.addPlugin(NavigationFlipperPlugin.getInstance());
        flipperClient.start();

        mIsInitialized = true;
    }

    @Override
    public boolean isInitialized() {
        return mIsInitialized;
    }

    @Override
    @Nullable
    public Interceptor getOkHttpInterceptor() {
        return mOkHttpInterceptor;
    }
}
