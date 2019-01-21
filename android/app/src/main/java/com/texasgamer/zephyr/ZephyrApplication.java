package com.texasgamer.zephyr;

import android.app.Application;

import com.texasgamer.zephyr.injection.components.ApplicationComponent;
import com.texasgamer.zephyr.injection.components.DaggerApplicationComponent;
import com.texasgamer.zephyr.injection.modules.ApplicationModule;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import javax.inject.Inject;

public class ZephyrApplication extends Application {

    private static final String LOG_TAG = "ZephyrApplication";
    private static ZephyrApplication sInstance;
    private static ApplicationComponent sApplicationComponent;

    @Inject
    ILogger logger;

    public static ApplicationComponent getApplicationComponent() {
        return sApplicationComponent;
    }

    public static ZephyrApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        sApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        sApplicationComponent.inject(ZephyrApplication.this);
        logger.log(LogPriority.DEBUG, LOG_TAG, "Zephyr started.");

        sApplicationComponent.notificationsManager().createNotificationChannels();
    }
}
