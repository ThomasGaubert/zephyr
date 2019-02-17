package com.texasgamer.zephyr.worker;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.db.dao.NotificationPreferenceDao;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AppSyncWorker extends Worker {

    private static String LOG_TAG = "AppSyncWorker";

    private Context mContext;

    @Inject
    ILogger logger;
    @Inject
    ApplicationUtils applicationUtils;
    @Inject
    NotificationPreferenceDao notificationPreferenceDao;

    public AppSyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        mContext = context;
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @Override
    @NonNull
    public Result doWork() {
        logger.log(LogPriority.DEBUG, LOG_TAG, "Starting app sync...");

        List<NotificationPreferenceEntity> notificationPreferenceEntities = new ArrayList<>();

        for (PackageInfo packageInfo : applicationUtils.getInstalledPackages()) {
            Drawable appIcon = applicationUtils.getAppIcon(packageInfo);
            int defaultColor = ContextCompat.getColor(mContext, R.color.primaryDark);
            int appColor = appIcon != null ? getAppColor(Palette.from(convertToBitmap(appIcon, 100, 100)).generate(), defaultColor) : defaultColor;

            notificationPreferenceEntities.add(new NotificationPreferenceEntity(packageInfo.packageName,
                    applicationUtils.getAppName(packageInfo),
                    appColor,
                    true));
        }

        notificationPreferenceDao.insertNotificationPreferences(notificationPreferenceEntities);

        notificationPreferenceDao.removeOrphanedNotificationPreferences(applicationUtils.getInstalledPackageNames().toArray(new String[0]));

        logger.log(LogPriority.DEBUG, LOG_TAG, "Finished app sync. Synced %d apps.", notificationPreferenceEntities.size());
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
