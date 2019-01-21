package com.texasgamer.zephyr.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.model.ZephyrCardType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

public class NavigationUtils {

    public static void openActivity(@NonNull Context context, @NonNull Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void openUrl(@NonNull Context context, @NonNull String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.primary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
