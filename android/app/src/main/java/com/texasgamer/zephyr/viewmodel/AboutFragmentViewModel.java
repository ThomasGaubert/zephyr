package com.texasgamer.zephyr.viewmodel;

import android.app.Application;

import com.texasgamer.zephyr.ZephyrApplication;

/**
 * ViewModel for {@link com.texasgamer.zephyr.fragment.AboutFragment}.
 */
public class AboutFragmentViewModel extends BaseViewModel {

    public AboutFragmentViewModel(Application application) {
        super(application);
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }
}
