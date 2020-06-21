package com.texasgamer.zephyr.db.repository;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.db.ZephyrDatabase;
import com.texasgamer.zephyr.db.dao.AppInfoDao;
import com.texasgamer.zephyr.db.dao.NotificationPreferenceDao;
import com.texasgamer.zephyr.db.entity.AppInfoEntity;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;

import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of {@link AppSyncWorkerRepository}.
 */
public class AppSyncWorkerDataSource implements AppSyncWorkerRepository {

    private ZephyrDatabase mZephyrDatabase;
    private AppInfoDao mAppInfoDao;
    private NotificationPreferenceDao mNotificationPreferenceDao;

    @Inject
    public AppSyncWorkerDataSource(@NonNull ZephyrDatabase zephyrDatabase) {
        mZephyrDatabase = zephyrDatabase;
        mAppInfoDao = zephyrDatabase.appInfoDao();
        mNotificationPreferenceDao = zephyrDatabase.notificationPreferenceDao();
    }

    @Override
    public void insert(final List<AppInfoEntity> appInfoEntities, final List<NotificationPreferenceEntity> notificationPreferenceEntities) {
        mZephyrDatabase.runInTransaction(() -> {
            mAppInfoDao.insertAppInfo(appInfoEntities);
            mNotificationPreferenceDao.insertNotificationPreferences(notificationPreferenceEntities);
        });
    }

    @Override
    public void pruneOrphans(String... installedPackageNames) {
        mZephyrDatabase.runInTransaction(() -> {
            mAppInfoDao.removeOrphanedAppInfo(installedPackageNames);
            mNotificationPreferenceDao.removeOrphanedNotificationPreferences(installedPackageNames);
        });
    }
}
