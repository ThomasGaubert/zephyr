package com.texasgamer.zephyr.db.repository;

import com.texasgamer.zephyr.db.entity.AppInfoEntity;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;

import java.util.List;

/**
 * Repository for {@link com.texasgamer.zephyr.worker.AppSyncWorker}.
 */
public interface AppSyncWorkerRepository extends IRepository {
    void insert(List<AppInfoEntity> appInfoEntities, List<NotificationPreferenceEntity> notificationPreferenceEntities);

    void pruneOrphans(String... installedPackageNames);
}
