package com.texasgamer.zephyr;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.texasgamer.zephyr.injection.components.ApplicationComponent;
import com.texasgamer.zephyr.injection.components.DaggerApplicationComponent;
import com.texasgamer.zephyr.injection.modules.ApplicationModule;
import com.texasgamer.zephyr.model.ConnectionStatus;
import com.texasgamer.zephyr.service.SocketService;
import com.texasgamer.zephyr.service.lifecycle.ZephyrLifecycleLogger;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.worker.IWorkManager;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

/**
 * Zephyr application.
 */
public class ZephyrApplication extends Application {

    private static final String LOG_TAG = "ZephyrApplication";
    private static ZephyrApplication sInstance;
    private static ApplicationComponent sApplicationComponent;

    @Inject
    ILogger logger;
    @Inject
    IConfigManager configManager;
    @Inject
    IWorkManager workManager;
    @Inject
    IPreferenceManager preferenceManager;

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
        sApplicationComponent.init();

        registerActivityLifecycleCallbacks(new ZephyrLifecycleLogger());

        if (configManager.isFirebaseEnabled()) {
            FirebaseApp.initializeApp(this);
        } else {
            logger.log(LogPriority.WARNING, LOG_TAG, "Firebase disabled, some features will be limited or disabled.");
        }

        if (configManager.isFirebaseCrashlyticsEnabled()) {
            Fabric.with(this, new Crashlytics());
        } else {
            logger.log(LogPriority.WARNING, LOG_TAG, "Crashlytics disabled.");
        }

        logger.log(LogPriority.DEBUG, LOG_TAG, "Zephyr started.");

        sApplicationComponent.notificationsManager().createNotificationChannels();

        workManager.initWork();

        verifyConnectionStatus();
    }

    private void verifyConnectionStatus() {
        if (!preferenceManager.getBoolean(PreferenceKeys.PREF_IS_SOCKET_SERVICE_RUNNING)) {
            preferenceManager.putInt(PreferenceKeys.PREF_CONNECTION_STATUS, ConnectionStatus.DISCONNECTED);
        } else if (!SocketService.instanceCreated) {
            preferenceManager.putBoolean(PreferenceKeys.PREF_IS_SOCKET_SERVICE_RUNNING, false);
            preferenceManager.putInt(PreferenceKeys.PREF_CONNECTION_STATUS, ConnectionStatus.DISCONNECTED);
        }
    }
}
