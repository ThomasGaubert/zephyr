package com.texasgamer.zephyr;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.distribute.Distribute;
import com.texasgamer.zephyr.injection.components.ApplicationComponent;
import com.texasgamer.zephyr.injection.components.DaggerApplicationComponent;
import com.texasgamer.zephyr.injection.modules.ApplicationModule;
import com.texasgamer.zephyr.model.ConnectionStatus;
import com.texasgamer.zephyr.network.IDiscoveryManager;
import com.texasgamer.zephyr.service.QuickSettingService;
import com.texasgamer.zephyr.service.SocketService;
import com.texasgamer.zephyr.util.BuildConfigUtils;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.flipper.IFlipperManager;
import com.texasgamer.zephyr.util.lifecycle.ZephyrLifecycleLogger;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogLevel;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.privacy.IPrivacyManager;
import com.texasgamer.zephyr.util.theme.IThemeManager;
import com.texasgamer.zephyr.worker.IWorkManager;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

/**
 * Zephyr application.
 */
public class ZephyrApplication extends Application implements LifecycleObserver, Application.ActivityLifecycleCallbacks {

    private static final String LOG_TAG = "ZephyrApplication";
    private static ZephyrApplication sInstance;
    private static ApplicationComponent sApplicationComponent;
    private static boolean sCrashReportingInitialized = false;

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
    @Inject
    IFlipperManager flipperManager;
    @Inject
    IDiscoveryManager discoveryManager;
    @Inject
    IThemeManager themeManager;

    private WeakReference<Activity> mCurrentActivity;
    private boolean mIsInForeground;

    public static ApplicationComponent getApplicationComponent() {
        return sApplicationComponent;
    }

    public static ZephyrApplication getInstance() {
        return sInstance;
    }

    public static boolean isCrashReportingInitialized() {
        return sCrashReportingInitialized;
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

        registerActivityLifecycleCallbacks(new ZephyrLifecycleLogger(logger));
        registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        EventBus.builder().logNoSubscriberMessages(false).installDefaultEventBus();

        if (configManager.isFirebaseEnabled()) {
            FirebaseApp.initializeApp(this);
        } else {
            logger.log(LogLevel.WARNING, LOG_TAG, "Firebase disabled, some features will be limited or disabled.");
        }

        FirebaseCrashlytics firebaseCrashlytics = FirebaseCrashlytics.getInstance();
        if (privacyManager.isCrashReportingEnabled()) {
            firebaseCrashlytics.setCrashlyticsCollectionEnabled(true);
            firebaseCrashlytics.setUserId(privacyManager.getUuid());
            sCrashReportingInitialized = true;
        } else {
            firebaseCrashlytics.setCrashlyticsCollectionEnabled(false);
            logger.log(LogLevel.WARNING, LOG_TAG, "Crashlytics disabled.");
        }

        if (!BuildConfig.PROPS_SET) {
            logger.log(LogLevel.WARNING, LOG_TAG, "Secret properties not set! Some features will be limited or disabled.");
        }

        if (flipperManager.isInitialized()) {
            logger.log(LogLevel.INFO, LOG_TAG, "Flipper initialized.");
        }

        logger.log(LogLevel.DEBUG, LOG_TAG, "Zephyr %s (%s - %s) started.", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.GIT_HASH);

        themeManager.setDefaultNightMode();

        sApplicationComponent.notificationsManager().createNotificationChannels();

        workManager.initWork();

        verifyConnectionStatus();

        // Check for updates from AppCenter if beta.
        // TODO: Refactor to be generic and to support other tracks.
        if (BuildConfig.PROPS_SET && configManager.isBeta()) {
            AppCenter.start(this, BuildConfig.APP_CENTER_SECRET, Distribute.class);
        } else if (!BuildConfig.PROPS_SET && configManager.isBeta()) {
            logger.log(LogLevel.WARNING, LOG_TAG, "AppCenter update check disabled -- APP_CENTER_SECRET not set!");
        }

        if (configManager.isDiscoveryEnabled()) {
            logger.log(LogLevel.INFO, LOG_TAG, "Starting DiscoveryManager.");
            discoveryManager.start();
        }

        // Track version code
        preferenceManager.getInt(PreferenceKeys.PREF_LAST_KNOWN_APP_VERSION, BuildConfigUtils.getVersionCode());
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        mCurrentActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    public boolean isInForeground() {
        return mIsInForeground;
    }

    @Nullable
    public Activity getCurrentActivity() {
        return mCurrentActivity != null ? mCurrentActivity.get() : null;
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onAppForegrounded() {
        logger.log(LogLevel.VERBOSE, LOG_TAG, "App is in the foreground.");
        mIsInForeground = true;
        if (configManager.isDiscoveryEnabled()) {
            logger.log(LogLevel.INFO, LOG_TAG, "Starting DiscoveryManager.");
            discoveryManager.start();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            QuickSettingService.updateQuickSettingTile(this);
        }
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onAppBackgrounded() {
        logger.log(LogLevel.VERBOSE, LOG_TAG, "App is in the background.");
        mIsInForeground = false;
        discoveryManager.stop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            QuickSettingService.updateQuickSettingTile(this);
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
