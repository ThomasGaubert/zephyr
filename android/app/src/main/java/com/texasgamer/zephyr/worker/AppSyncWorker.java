package com.texasgamer.zephyr.worker;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.db.dao.NotificationPreferenceDao;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Worker that syncs apps to notification preference table.
 */
public class AppSyncWorker extends Worker {

    private static final String LOG_TAG = "AppSyncWorker";

    @Inject
    ILogger logger;
    @Inject
    ApplicationUtils applicationUtils;
    @Inject
    NotificationPreferenceDao notificationPreferenceDao;
    @Inject
    IPreferenceManager preferenceManager;
    @Inject
    Gson gson;

    private Context mContext;

    public AppSyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        mContext = context;
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @Override
    @NonNull
    public Result doWork() {
        logger.log(LogPriority.INFO, LOG_TAG, "Starting app sync...");

        boolean shouldMigratePreferences = false;
        Map<String, Boolean> notificationPreferencesToMigrate = new ArrayMap<>();
        if (preferenceManager.hasKey(PreferenceKeys.PREF_NOTIFICATION_PREF_MIGRATIONS_TO_COMPLETE)) {
            logger.log(LogPriority.INFO, LOG_TAG, "Found notification preferences to migrate");
            String migrationsToCompleteString = preferenceManager.getString(PreferenceKeys.PREF_NOTIFICATION_PREF_MIGRATIONS_TO_COMPLETE);
            Type mapType = new TypeToken<Map<String, Boolean>>() { }.getType();
            notificationPreferencesToMigrate = gson.fromJson(migrationsToCompleteString, mapType);
            shouldMigratePreferences = true;
        }

        List<NotificationPreferenceEntity> notificationPreferenceEntities = new ArrayList<>();
        for (PackageInfo packageInfo : applicationUtils.getInstalledPackages()) {
            Drawable appIcon = applicationUtils.getAppIcon(packageInfo);
            int defaultColor = ContextCompat.getColor(mContext, R.color.primaryDark);
            int appColor = appIcon != null ? getAppColor(Palette.from(convertToBitmap(appIcon, 100, 100)).generate(), defaultColor) : defaultColor;
            boolean enabled = true;

            if (shouldMigratePreferences && notificationPreferencesToMigrate.containsKey(packageInfo.packageName)) {
                Boolean enabledBoolean = notificationPreferencesToMigrate.get(packageInfo.packageName);
                if (enabledBoolean != null) {
                    enabled = enabledBoolean;
                }
            }

            notificationPreferenceEntities.add(new NotificationPreferenceEntity(packageInfo.packageName,
                    applicationUtils.getAppName(packageInfo),
                    appColor,
                    enabled));
        }

        notificationPreferenceDao.insertNotificationPreferences(notificationPreferenceEntities);

        notificationPreferenceDao.removeOrphanedNotificationPreferences(applicationUtils.getInstalledPackageNames().toArray(new String[0]));

        if (shouldMigratePreferences) {
            preferenceManager.remove(PreferenceKeys.PREF_NOTIFICATION_PREF_MIGRATIONS_TO_COMPLETE);
        }

        logger.log(LogPriority.INFO, LOG_TAG, "Finished app sync. Synced %d apps.", notificationPreferenceEntities.size());
        return Result.success();
    }

    @NonNull
    private Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    @ColorInt
    private int getAppColor(@NonNull Palette palette, @ColorInt int defaultColor) {
        if (palette.getDarkVibrantSwatch() != null) {
            return palette.getDarkVibrantColor(defaultColor);
        }

        if (palette.getVibrantSwatch() != null) {
            return palette.getVibrantColor(defaultColor);
        }

        if (palette.getMutedSwatch() != null) {
            return palette.getMutedColor(defaultColor);
        }

        if (palette.getDarkMutedSwatch() != null) {
            return palette.getDarkMutedColor(defaultColor);
        }

        if (palette.getLightMutedSwatch() != null) {
            return palette.getLightMutedColor(defaultColor);
        }

        if (palette.getLightVibrantSwatch() != null) {
            return palette.getLightVibrantColor(defaultColor);
        }

        return defaultColor;
    }
}
