package com.texasgamer.zephyr.util.navigation;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;

/**
 * Navigation utilities.
 */
public final class NavigationUtils {

    private NavigationUtils() {
    }

    public static void openUrl(@NonNull Context context, @NonNull String url) {
        Context finalContext = context;
        if (!(context instanceof Activity)) {
            Context activityContext = ZephyrApplication.getInstance().getCurrentActivity();
            if (activityContext != null) {
                finalContext = activityContext;
            }
        }

        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(finalContext, R.color.primary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(finalContext, Uri.parse(url));
        } catch (ActivityNotFoundException e) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            finalContext.startActivity(browserIntent);
        }
    }

    public static void openZephyrAppInfo(@NonNull Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
