package com.texasgamer.zephyr.worker;

import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class ZephyrWorkManager implements IWorkManager {

    private static String LOG_TAG = "ZephyrWorkManager";
    private static String SYNC_WORK_TAG = "sync";

    private ILogger mLogger;

    public ZephyrWorkManager(@NonNull ILogger logger) {
        this.mLogger = logger;
    }

    @Override
    public void initWork() {
        mLogger.log(LogPriority.DEBUG, LOG_TAG, "Setting up work items...");
        setupAppSyncWork();
        mLogger.log(LogPriority.DEBUG, LOG_TAG, "Done setting up work items.");
    }

    @Override
    public void cancelWork() {
        mLogger.log(LogPriority.DEBUG, LOG_TAG, "Canceling work items.");
        WorkManager.getInstance().cancelAllWorkByTag(SYNC_WORK_TAG);
    }

    private void setupAppSyncWork() {
        mLogger.log(LogPriority.DEBUG, LOG_TAG, "Setting up app sync work...");

        PeriodicWorkRequest.Builder appSyncWorkBuilder = new PeriodicWorkRequest.Builder(AppSyncWorker.class, 6, TimeUnit.HOURS);

        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        appSyncWorkBuilder.setConstraints(constraints)
                .addTag(SYNC_WORK_TAG);

        PeriodicWorkRequest appSyncWork = appSyncWorkBuilder.build();
        WorkManager.getInstance().enqueueUniquePeriodicWork(SYNC_WORK_TAG, ExistingPeriodicWorkPolicy.REPLACE, appSyncWork);
        mLogger.log(LogPriority.DEBUG, LOG_TAG, "Done setting up app sync work.");
    }
}
