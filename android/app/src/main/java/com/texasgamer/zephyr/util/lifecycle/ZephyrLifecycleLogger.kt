package com.texasgamer.zephyr.util.lifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.texasgamer.zephyr.ZephyrApplication
import com.texasgamer.zephyr.util.log.ILogger
import com.texasgamer.zephyr.util.log.LogLevel
import javax.inject.Inject

/**
 * Logs lifecycle events.
 */

private const val LOG_TAG = "ZephyrLifecycleLogger"

class ZephyrLifecycleLogger(private val logger: ILogger) : ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        logger.log(LogLevel.INFO, LOG_TAG, "onActivityCreated: " + activity.localClassName)
    }

    override fun onActivityStarted(activity: Activity) {
        logger.log(LogLevel.INFO, LOG_TAG, "onActivityStarted: " + activity.localClassName)
    }

    override fun onActivityResumed(activity: Activity) {
        logger.log(LogLevel.INFO, LOG_TAG, "onActivityResumed: " + activity.localClassName)
    }

    override fun onActivityPaused(activity: Activity) {
        logger.log(LogLevel.INFO, LOG_TAG, "onActivityPaused: " + activity.localClassName)
    }

    override fun onActivityStopped(activity: Activity) {
        logger.log(LogLevel.INFO, LOG_TAG, "onActivityStopped: " + activity.localClassName)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        logger.log(LogLevel.INFO, LOG_TAG, "onActivitySaveInstanceState: " + activity.localClassName)
    }

    override fun onActivityDestroyed(activity: Activity) {
        logger.log(LogLevel.INFO, LOG_TAG, "onActivityDestroyed: " + activity.localClassName)
    }
}