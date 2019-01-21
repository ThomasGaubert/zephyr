package com.texasgamer.zephyr.viewmodel;

import android.app.Application;

import com.texasgamer.zephyr.ZephyrApplication;

public class MenuViewModel extends BaseViewModel {

    public MenuViewModel(Application application) {
        super(application);
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }
}
