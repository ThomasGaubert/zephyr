package com.texasgamer.zephyr.service.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import javax.inject.Inject;

public class ZephyrLifecycleLogger implements Application.ActivityLifecycleCallbacks {

    private static final String LOG_TAG = "ZephyrLifecycleLogger";

    @Inject
    ILogger logger;

    public ZephyrLifecycleLogger() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        logger.log(LogPriority.INFO, LOG_TAG, "onActivityCreated: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        logger.log(LogPriority.INFO, LOG_TAG, "onActivityStarted: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        logger.log(LogPriority.INFO, LOG_TAG, "onActivityResumed: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        logger.log(LogPriority.INFO, LOG_TAG, "onActivityPaused: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        logger.log(LogPriority.INFO, LOG_TAG, "onActivityStopped: " + activity.getLocalClassName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        logger.log(LogPriority.INFO, LOG_TAG, "onActivitySaveInstanceState: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        logger.log(LogPriority.INFO, LOG_TAG, "onActivityDestroyed: " + activity.getLocalClassName());
    }
}
