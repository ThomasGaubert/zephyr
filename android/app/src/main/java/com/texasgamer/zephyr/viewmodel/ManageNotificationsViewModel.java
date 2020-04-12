package com.texasgamer.zephyr.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.db.repository.NotificationPreferenceRepository;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import java.util.List;

import javax.inject.Inject;

/**
 * Manage notifications view model.
 */
public class ManageNotificationsViewModel extends BaseViewModel<NotificationPreferenceRepository> {

    private static final String LOG_TAG = "ManageNotificationsViewModel";

    @Inject
    ILogger logger;

    private final LiveData<List<NotificationPreferenceEntity>> mObservableNotificationPreferences;
    private final MutableLiveData<String> mSearchQuery;

    public ManageNotificationsViewModel(Application application) {
        super(application);

        mSearchQuery = new MutableLiveData<>();
        mSearchQuery.setValue(null);

        mObservableNotificationPreferences = Transformations.switchMap(mSearchQuery, input -> {
            if (input == null || input.trim().isEmpty()) {
                return mDataRepository.getNotificationPreferences();
            } else {
                return mDataRepository.getNotificationPreferencesByName(input);
            }
        });
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    public LiveData<List<NotificationPreferenceEntity>> getNotificationPreferences() {
        return mObservableNotificationPreferences;
    }

    public void enableAll() {
        String searchQuery = mSearchQuery.getValue();
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Enabling all notification preferences...");
            mDataRepository.enableAll();
        } else {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Enabling all visible notification preferences...");
            mDataRepository.enableAll(searchQuery);
        }
    }

    public void disableAll() {
        String searchQuery = mSearchQuery.getValue();
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Disabling all notification preferences...");
            mDataRepository.disableAll();
        } else {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Disabling all visible notification preferences...");
            mDataRepository.disableAll(searchQuery);
        }
    }

    public void updateNotificationPreference(@NonNull String packageName, boolean enabled) {
        mDataRepository.updateNotificationPreference(packageName, enabled);
    }

    public void setSearchQuery(@Nullable String searchQuery) {
        mSearchQuery.setValue(searchQuery);
    }
}
