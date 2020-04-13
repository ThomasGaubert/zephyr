package com.texasgamer.zephyr.injection.components;

import androidx.annotation.Nullable;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.activity.AboutActivity;
import com.texasgamer.zephyr.activity.LicensesActivity;
import com.texasgamer.zephyr.activity.MainActivity;
import com.texasgamer.zephyr.activity.NotificationActivity;
import com.texasgamer.zephyr.db.ZephyrDatabase;
import com.texasgamer.zephyr.fragment.ConnectFragment;
import com.texasgamer.zephyr.fragment.DebugFragment;
import com.texasgamer.zephyr.fragment.JoinCodeFragment;
import com.texasgamer.zephyr.fragment.MainFragment;
import com.texasgamer.zephyr.fragment.MenuFragment;
import com.texasgamer.zephyr.fragment.NotificationsFragment;
import com.texasgamer.zephyr.fragment.PrivacyFragment;
import com.texasgamer.zephyr.fragment.ScanCodeFragment;
import com.texasgamer.zephyr.fragment.WhatsNewFragment;
import com.texasgamer.zephyr.fragment.whatsnew.WhatsNewItemFragment;
import com.texasgamer.zephyr.injection.modules.AnalyticsModule;
import com.texasgamer.zephyr.injection.modules.ApplicationModule;
import com.texasgamer.zephyr.injection.modules.ConfigModule;
import com.texasgamer.zephyr.injection.modules.DatabaseModule;
import com.texasgamer.zephyr.injection.modules.EagerModule;
import com.texasgamer.zephyr.injection.modules.FlipperModule;
import com.texasgamer.zephyr.injection.modules.LoggerModule;
import com.texasgamer.zephyr.injection.modules.NetworkModule;
import com.texasgamer.zephyr.injection.modules.NotificationsModule;
import com.texasgamer.zephyr.injection.modules.PreferenceModule;
import com.texasgamer.zephyr.injection.modules.PrivacyModule;
import com.texasgamer.zephyr.injection.modules.ProviderModule;
import com.texasgamer.zephyr.injection.modules.WorkModule;
import com.texasgamer.zephyr.service.NotificationService;
import com.texasgamer.zephyr.service.SocketService;
import com.texasgamer.zephyr.service.lifecycle.ZephyrLifecycleLogger;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.notification.INotificationsManager;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.viewmodel.ConnectButtonViewModel;
import com.texasgamer.zephyr.viewmodel.MainFragmentViewModel;
import com.texasgamer.zephyr.viewmodel.ManageNotificationsViewModel;
import com.texasgamer.zephyr.worker.AppSyncWorker;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Application component.
 */
@Singleton
@Component(
        modules = {
            AnalyticsModule.class,
            ApplicationModule.class,
            ConfigModule.class,
            DatabaseModule.class,
            EagerModule.class,
            FlipperModule.class,
            LoggerModule.class,
            NetworkModule.class,
            PreferenceModule.class,
            NotificationsModule.class,
            WorkModule.class,
            ProviderModule.class,
            PrivacyModule.class
        })
public interface ApplicationComponent {
    /* Init */
    @Nullable
    Void init();

    /* Getters */
    ApplicationUtils applicationUtilities();

    ILogger logger();

    ZephyrDatabase database();

    INotificationsManager notificationsManager();

    IPreferenceManager preferenceManager();

    /* Application */
    void inject(ZephyrApplication application);

    /* Services */
    void inject(NotificationService notificationService);

    void inject(SocketService socketService);

    /* ViewModels */
    void inject(ManageNotificationsViewModel manageNotificationsViewModel);

    void inject(ConnectButtonViewModel connectButtonViewModel);

    void inject(MainFragmentViewModel onboardingViewModel);

    /* Activities */
    void inject(MainActivity mainActivity);

    void inject(AboutActivity aboutActivity);

    void inject(LicensesActivity licensesActivity);

    void inject(NotificationActivity notificationActivity);

    /* Fragments */
    void inject(MainFragment mainFragment);

    void inject(NotificationsFragment notificationsFragment);

    void inject(MenuFragment menuFragment);

    void inject(PrivacyFragment privacyFragment);

    void inject(ConnectFragment connectFragment);

    void inject(ScanCodeFragment scanCodeFragment);

    void inject(JoinCodeFragment joinCodeFragment);

    void inject(WhatsNewFragment whatsNewFragment);

    void inject(WhatsNewItemFragment whatsNewItemFragment);

    void inject(DebugFragment debugFragment);

    /* Miscellaneous */
    void inject(ZephyrLifecycleLogger zephyrLifecycleLogger);

    void inject(AppSyncWorker appSyncWorker);
}
