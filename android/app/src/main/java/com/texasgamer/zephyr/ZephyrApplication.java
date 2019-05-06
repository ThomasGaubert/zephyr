package com.texasgamer.zephyr;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.distribute.Distribute;
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
import com.texasgamer.zephyr.util.privacy.IPrivacyManager;
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
    private static boolean sFabricInitialized = false;

    @Inject
    ILogger logger;
    @Inject
    IConfigManager configManager;
    @Inject
    IWorkManager workManager;
    @Inject
    IPreferenceManager preferenceManager;
    @Inject
    IPrivacyManager privacyManager;

    public static ApplicationComponent getApplicationComponent() {
        return sApplicationComponent;
    }

    public static ZephyrApplication getInstance() {
        return sInstance;
    }

    public static boolean isFabricInitialized() {
        return sFabricInitialized;
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

        if (privacyManager.isCrashReportingEnabled()) {
            Fabric.with(this, new Crashlytics());
            Crashlytics.setUserIdentifier(privacyManager.getUuid());
            sFabricInitialized = true;
        } else {
            logger.log(LogPriority.WARNING, LOG_TAG, "Crashlytics disabled.");
        }

        if (!BuildConfig.PROPS_SET) {
            logger.log(LogPriority.WARNING, LOG_TAG, "Secret properties not set! Some features will be limited or disabled.");
        }

        logger.log(LogPriority.DEBUG, LOG_TAG, "Zephyr %s (%s - %s) started.", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.GIT_HASH);

        sApplicationComponent.notificationsManager().createNotificationChannels();

        workManager.initWork();

        verifyConnectionStatus();

        // Check for updates from AppCenter if beta.
        // TODO: Refactor to be generic and to support other tracks.
        if (BuildConfig.PROPS_SET && configManager.isBeta()) {
            AppCenter.start(this, BuildConfig.APP_CENTER_SECRET, Distribute.class);
        } else if (!BuildConfig.PROPS_SET && configManager.isBeta()) {
            logger.log(LogPriority.WARNING, LOG_TAG, "AppCenter update check disabled -- APP_CENTER_SECRET not set!");
        }
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
