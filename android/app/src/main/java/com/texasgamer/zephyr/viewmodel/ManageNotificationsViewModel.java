package com.texasgamer.zephyr.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.adapter.NotificationPreferenceListAdapter;
import com.texasgamer.zephyr.db.repository.NotificationPreferenceRepository;
import com.texasgamer.zephyr.model.ZephyrNotificationPreference;
import com.texasgamer.zephyr.util.StringUtils;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogLevel;
import com.texasgamer.zephyr.view.NotificationPreferenceView;

import java.util.List;

import javax.inject.Inject;

/**
 * Manage notifications view model.
 */
public class ManageNotificationsViewModel extends BaseViewModel<NotificationPreferenceRepository> implements NotificationPreferenceView.OnPreferenceChangeListener {

    private static final String LOG_TAG = "ManageNotificationsViewModel";

    @Inject
    ILogger logger;

    private final MutableLiveData<String> mSearchQuery = new MutableLiveData<>();
    private final MediatorLiveData<Integer> mAppListVisibility = new MediatorLiveData<>();
    private final MediatorLiveData<Integer> mSpinnerVisibility = new MediatorLiveData<>();
    private final MediatorLiveData<Integer> mNoResultsVisibility = new MediatorLiveData<>();
    private NotificationPreferenceListAdapter mAdapter;

    public ManageNotificationsViewModel(Application application) {
        super(application);

        LiveData<List<ZephyrNotificationPreference>> notificationPreferences = Transformations.switchMap(mSearchQuery, input -> {
            if (StringUtils.isNullOrEmpty(input)) {
                return mDataRepository.getNotificationPreferences();
            } else {
                return mDataRepository.getNotificationPreferencesByName(input);
            }
        });

        mAppListVisibility.postValue(View.GONE);
        mAppListVisibility.addSource(notificationPreferences, preferences -> {
            if (preferences != null && !preferences.isEmpty()) {
                mAdapter.setNotificationPreferences(preferences);
                mAppListVisibility.setValue(View.VISIBLE);
            } else {
                mAppListVisibility.setValue(View.GONE);
            }
        });

        mSpinnerVisibility.postValue(View.VISIBLE);
        mSpinnerVisibility.addSource(notificationPreferences, preferences -> {
            mSpinnerVisibility.setValue(StringUtils.isNullOrEmpty(mSearchQuery.getValue())
                        && (preferences == null || preferences.isEmpty())
                    ? View.VISIBLE
                    : View.GONE);
        });

        mNoResultsVisibility.postValue(View.GONE);
        mNoResultsVisibility.addSource(notificationPreferences, preferences -> {
            mNoResultsVisibility.setValue(!StringUtils.isNullOrEmpty(mSearchQuery.getValue())
                        && (preferences == null || preferences.isEmpty())
                    ? View.VISIBLE
                    : View.GONE);
        });

        mAdapter = new NotificationPreferenceListAdapter(this);
        mSearchQuery.setValue(null);
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void onPreferenceChange(@NonNull String packageName, boolean newValue) {
        updateNotificationPreference(packageName, newValue);
    }

    public void enableAll() {
        String searchQuery = mSearchQuery.getValue();
        if (StringUtils.isNullOrEmpty(searchQuery)) {
            logger.log(LogLevel.DEBUG, LOG_TAG, "Enabling all notification preferences...");
            mDataRepository.enableAll();
        } else {
            logger.log(LogLevel.DEBUG, LOG_TAG, "Enabling all visible notification preferences...");
            mDataRepository.enableAll(searchQuery);
        }
    }

    public void disableAll() {
        String searchQuery = mSearchQuery.getValue();
        if (StringUtils.isNullOrEmpty(searchQuery)) {
            logger.log(LogLevel.DEBUG, LOG_TAG, "Disabling all notification preferences...");
            mDataRepository.disableAll();
        } else {
            logger.log(LogLevel.DEBUG, LOG_TAG, "Disabling all visible notification preferences...");
            mDataRepository.disableAll(searchQuery);
        }
    }

    public void updateNotificationPreference(@NonNull String packageName, boolean enabled) {
        mDataRepository.updateNotificationPreference(packageName, enabled);
    }

    public void setSearchQuery(@Nullable String searchQuery) {
        mSearchQuery.setValue(searchQuery);
    }

    @NonNull
    public LiveData<String> getSearchQuery() {
        return mSearchQuery;
    }

    public boolean hasFixedSize() {
        return true;
    }

    @NonNull
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getApplication());
    }

    @NonNull
    public NotificationPreferenceListAdapter getAdapter() {
        return mAdapter;
    }

    @NonNull
    public LiveData<Integer> getAppListVisibility() {
        return mAppListVisibility;
    }

    @NonNull
    public LiveData<Integer> getSpinnerVisibility() {
        return mSpinnerVisibility;
    }

    @NonNull
    public LiveData<Integer> getNoResultsVisibility() {
        return mNoResultsVisibility;
    }
}
