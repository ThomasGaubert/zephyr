package com.texasgamer.zephyr.injection.components;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.db.ZephyrDatabase;
import com.texasgamer.zephyr.db.repository.NotificationPreferenceRepository;
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
    void inject(ZephyrApplication application);

    void inject(NotificationService notificationService);

    void inject(SocketService socketService);

    void inject(MenuViewModel menuViewModel);

    void inject(ManageNotificationsViewModel manageNotificationsViewModel);

    ApplicationUtils applicationUtilities();

    NotificationsManager notificationsManager();

    ZephyrDatabase database();

    NotificationPreferenceRepository notificationPreferenceRepository();

    ILogger logger();
}
