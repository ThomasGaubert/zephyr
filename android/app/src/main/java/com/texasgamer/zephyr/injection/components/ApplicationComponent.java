package com.texasgamer.zephyr.injection.components;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.activity.AboutActivity;
import com.texasgamer.zephyr.activity.LicensesActivity;
import com.texasgamer.zephyr.activity.MainActivity;
import com.texasgamer.zephyr.activity.NotificationActivity;
import com.texasgamer.zephyr.db.ZephyrDatabase;
import com.texasgamer.zephyr.fragment.ConnectFragment;
import com.texasgamer.zephyr.fragment.JoinCodeFragment;
import com.texasgamer.zephyr.fragment.MainFragment;
import com.texasgamer.zephyr.fragment.NotificationsFragment;
import com.texasgamer.zephyr.fragment.ScanCodeFragment;
import com.texasgamer.zephyr.fragment.WhatsNewFragment;
import com.texasgamer.zephyr.fragment.whatsnew.WhatsNewItemFragment;
import com.texasgamer.zephyr.injection.modules.AnalyticsModule;
import com.texasgamer.zephyr.injection.modules.ApplicationModule;
import com.texasgamer.zephyr.injection.modules.ConfigModule;
import com.texasgamer.zephyr.injection.modules.DatabaseModule;
import com.texasgamer.zephyr.injection.modules.EagerModule;
import com.texasgamer.zephyr.injection.modules.LoggerModule;
import com.texasgamer.zephyr.injection.modules.NotificationsModule;
import com.texasgamer.zephyr.injection.modules.PreferenceModule;
import com.texasgamer.zephyr.injection.modules.WorkModule;
import com.texasgamer.zephyr.service.NotificationService;
import com.texasgamer.zephyr.service.SocketService;
import com.texasgamer.zephyr.service.lifecycle.ZephyrLifecycleLogger;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.notification.NotificationsManager;
import com.texasgamer.zephyr.util.preference.PreferenceManager;
import com.texasgamer.zephyr.viewmodel.ConnectButtonViewModel;
import com.texasgamer.zephyr.viewmodel.ManageNotificationsViewModel;
import com.texasgamer.zephyr.worker.AppSyncWorker;

import javax.inject.Singleton;

import androidx.annotation.Nullable;
import dagger.Component;

@Singleton
@Component(
        modules = {
            AnalyticsModule.class,
            ApplicationModule.class,
            ConfigModule.class,
            DatabaseModule.class,
            EagerModule.class,
            LoggerModule.class,
            PreferenceModule.class,
            NotificationsModule.class,
            WorkModule.class
        })
public interface ApplicationComponent {
    /* Init */
    @Nullable
    Void init();

    /* Getters */
    ApplicationUtils applicationUtilities();

    ILogger logger();

    ZephyrDatabase database();

    NotificationsManager notificationsManager();

    PreferenceManager preferenceManager();

    /* Application */
    void inject(ZephyrApplication application);

    /* Services */
    void inject(NotificationService notificationService);

    void inject(SocketService socketService);

    /* ViewModels */
    void inject(ManageNotificationsViewModel manageNotificationsViewModel);

    void inject(ConnectButtonViewModel connectButtonViewModel);

    /* Activities */
    void inject(MainActivity mainActivity);

    void inject(AboutActivity aboutActivity);

    void inject(LicensesActivity licensesActivity);

    void inject(NotificationActivity notificationActivity);

    /* Fragments */
    void inject(MainFragment mainFragment);

    void inject(NotificationsFragment notificationsFragment);

    void inject(ConnectFragment connectFragment);

    void inject(ScanCodeFragment scanCodeFragment);

    void inject(JoinCodeFragment joinCodeFragment);

    void inject(WhatsNewFragment whatsNewFragment);

    void inject(WhatsNewItemFragment whatsNewItemFragment);

    /* Miscellaneous */
    void inject(ZephyrLifecycleLogger zephyrLifecycleLogger);

    void inject(AppSyncWorker appSyncWorker);
}
