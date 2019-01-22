package com.texasgamer.zephyr.injection.components;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.activity.AboutActivity;
import com.texasgamer.zephyr.activity.MainActivity;
import com.texasgamer.zephyr.activity.NotificationActivity;
import com.texasgamer.zephyr.db.ZephyrDatabase;
import com.texasgamer.zephyr.db.repository.NotificationPreferenceRepository;
import com.texasgamer.zephyr.fragment.JoinCodeFragment;
import com.texasgamer.zephyr.fragment.MainFragment;
import com.texasgamer.zephyr.fragment.NotificationsFragment;
import com.texasgamer.zephyr.injection.modules.ApplicationModule;
import com.texasgamer.zephyr.injection.modules.DatabaseModule;
import com.texasgamer.zephyr.injection.modules.LoggerModule;
import com.texasgamer.zephyr.injection.modules.NotificationsModule;
import com.texasgamer.zephyr.injection.modules.PreferenceModule;
import com.texasgamer.zephyr.service.NotificationService;
import com.texasgamer.zephyr.service.SocketService;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.notification.NotificationsManager;
import com.texasgamer.zephyr.util.preference.PreferenceManager;
import com.texasgamer.zephyr.util.preference.SharedPreferenceLiveData;
import com.texasgamer.zephyr.viewmodel.ConnectButtonViewModel;
import com.texasgamer.zephyr.viewmodel.ManageNotificationsViewModel;
import com.texasgamer.zephyr.viewmodel.MenuViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
            ApplicationModule.class,
            DatabaseModule.class,
            LoggerModule.class,
            PreferenceModule.class,
            NotificationsModule.class
        })
public interface ApplicationComponent {
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
    void inject(MenuViewModel menuViewModel);

    void inject(ManageNotificationsViewModel manageNotificationsViewModel);

    void inject(ConnectButtonViewModel connectButtonViewModel);

    /* Activities */
    void inject(MainActivity mainActivity);

    void inject(AboutActivity aboutActivity);

    void inject(NotificationActivity notificationActivity);

    /* Fragments */
    void inject(MainFragment mainFragment);

    void inject(NotificationsFragment notificationsFragment);

    void inject(JoinCodeFragment joinCodeFragment);
}
