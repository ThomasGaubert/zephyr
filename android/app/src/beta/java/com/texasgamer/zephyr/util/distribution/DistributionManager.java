package com.texasgamer.zephyr.util.distribution;

import androidx.annotation.NonNull;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.distribute.Distribute;
import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogLevel;

/**
 * AppCenter distribution manager.
 */
public class DistributionManager implements IDistributionManager {

    private static final String LOG_TAG = "DistributionManager";

    private final IConfigManager mConfigManager;
    private final ILogger mLogger;

    public DistributionManager(@NonNull IConfigManager configManager, @NonNull ILogger logger) {
        mConfigManager = configManager;
        mLogger = logger;
    }

    @Override
    public void start() {
        if (BuildConfig.PROPS_SET && mConfigManager.isBeta()) {
            AppCenter.start(ZephyrApplication.getInstance(), BuildConfig.APP_CENTER_SECRET, Distribute.class);
        } else if (!BuildConfig.PROPS_SET && mConfigManager.isBeta()) {
            mLogger.log(LogLevel.WARNING, LOG_TAG, "AppCenter update check disabled -- APP_CENTER_SECRET not set!");
        }
    }
}
