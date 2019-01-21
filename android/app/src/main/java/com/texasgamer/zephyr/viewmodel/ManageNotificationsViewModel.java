package com.texasgamer.zephyr.viewmodel;

import android.app.Application;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.db.repository.NotificationPreferenceRepository;
import com.texasgamer.zephyr.model.NotificationPreference;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class ManageNotificationsViewModel extends BaseViewModel<NotificationPreferenceRepository> {

    private static final String LOG_TAG = "ManageNotificationsViewModel";

    private final MediatorLiveData<List<NotificationPreferenceEntity>> observableNotificationPreferences;

    @Inject
    ILogger logger;

    public ManageNotificationsViewModel(Application application) {
        super(application);

        observableNotificationPreferences = new MediatorLiveData<>();
        observableNotificationPreferences.setValue(null);

        LiveData<List<NotificationPreferenceEntity>> notificationPreferences = mDataRepository.getNotificationPreferences();
        observableNotificationPreferences.addSource(notificationPreferences, observableNotificationPreferences::setValue);
    }

    @Override
    protected void injectDependencies() {
//        ManageNotificationsViewModelComponent component = ZephyrApplication.getApplicationComponent().addModule(new ManageNotificationsViewModelModule(this.mContext));
//        component.inject(this);
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    public LiveData<List<NotificationPreferenceEntity>> getNotificationPreferences() {
        return observableNotificationPreferences;
    }

    public void enableAll() {
        logger.log(LogPriority.DEBUG, LOG_TAG, "Enabling all notification preferences...");
        mDataRepository.enableAll();
    }

    public void disableAll() {
        logger.log(LogPriority.DEBUG, LOG_TAG, "Disabling all notification preferences...");
        mDataRepository.disableAll();
    }

    public void updateNotificationPreference(@NonNull String packageName, boolean enabled) {
        //NotificationPreference pref = mViewData.getNotificationPreference(packageName);
//        if (pref.enabled != enabled) {
//            logger.log(LogPriority.DEBUG, LOG_TAG, "Updating notification preference for %s", packageName);
//            mViewData.getNotificationPreference(packageName).enabled = enabled;
//            mViewData.savePreferences();
//        }
    }
}
