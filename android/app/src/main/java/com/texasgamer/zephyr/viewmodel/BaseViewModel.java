package com.texasgamer.zephyr.viewmodel;

import android.app.Application;
import android.content.Context;

import com.texasgamer.zephyr.db.repository.IRepository;

import javax.inject.Inject;

import androidx.lifecycle.AndroidViewModel;

/**
 * Base view model that performs common routines.
 * @param <T>
 */
public abstract class BaseViewModel<T extends IRepository> extends AndroidViewModel {

    @Inject
    protected T mDataRepository;
    protected Context mContext;

    public BaseViewModel(Application application) {
        super(application);
        mContext = application.getApplicationContext();

        injectDependencies();

        if (mDataRepository == null) {
            throw new IllegalStateException("Dependencies not fulfilled for this ViewModel.");
        }
    }

    protected abstract void injectDependencies();
}
