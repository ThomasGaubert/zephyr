package com.texasgamer.zephyr.viewmodel;

import android.app.Application;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.db.repository.NotificationPreferenceRepository;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

/**
 * Manage notifications view model.
 */
public class ManageNotificationsViewModel extends BaseViewModel<NotificationPreferenceRepository> {

    private static final String LOG_TAG = "ManageNotificationsViewModel";

    @Inject
    ILogger logger;

    private final MediatorLiveData<List<NotificationPreferenceEntity>> mObservableNotificationPreferences;


    public ManageNotificationsViewModel(Application application) {
        super(application);

        mObservableNotificationPreferences = new MediatorLiveData<>();
        mObservableNotificationPreferences.setValue(null);

        LiveData<List<NotificationPreferenceEntity>> notificationPreferences = mDataRepository.getNotificationPreferences();
        mObservableNotificationPreferences.addSource(notificationPreferences, mObservableNotificationPreferences::setValue);
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    public LiveData<List<NotificationPreferenceEntity>> getNotificationPreferences() {
        return mObservableNotificationPreferences;
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
        mDataRepository.updateNotificationPreference(packageName, enabled);
    }
}
