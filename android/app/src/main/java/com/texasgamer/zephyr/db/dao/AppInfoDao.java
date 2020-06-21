package com.texasgamer.zephyr.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.texasgamer.zephyr.db.entity.AppInfoEntity;

import java.util.List;

/**
 * DAO for {@link AppInfoEntity}.
 */
@Dao
public interface AppInfoDao {
    @Query("DELETE FROM app_info WHERE packageName NOT IN (:installedPackageNames)")
    void removeOrphanedAppInfo(String... installedPackageNames);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppInfo(List<AppInfoEntity> appInfoEntities);
}
