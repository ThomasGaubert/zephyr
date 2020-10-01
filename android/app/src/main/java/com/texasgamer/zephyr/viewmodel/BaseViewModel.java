package com.texasgamer.zephyr.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.texasgamer.zephyr.db.repository.IRepository;
import com.texasgamer.zephyr.util.analytics.IAnalyticsManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.navigation.INavigationManager;
import com.texasgamer.zephyr.util.resource.IResourceProvider;

import javax.inject.Inject;

/**
 * Base view model that performs common routines.
 * @param <T>
 */
public abstract class BaseViewModel<T extends IRepository> extends AndroidViewModel {

    @Inject
    protected T mDataRepository;
    @Inject
    protected IResourceProvider mResourceProvider;
    @Inject
    protected ILogger mLogger;
    @Inject
    protected INavigationManager mNavigationManager;
    @Inject
    protected IAnalyticsManager mAnalyticsManager;

    public BaseViewModel(Application application) {
        super(application);

        injectDependencies();

        if (mDataRepository == null) {
            throw new IllegalStateException("Dependencies not fulfilled for this ViewModel.");
        }
    }

    protected abstract void injectDependencies();
}
