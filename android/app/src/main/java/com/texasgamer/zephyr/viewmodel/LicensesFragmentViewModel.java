package com.texasgamer.zephyr.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.ZephyrApplication;

/**
 * ViewModel for {@link com.texasgamer.zephyr.fragment.LicensesFragment}.
 */
public class LicensesFragmentViewModel extends BaseViewModel {

    public LicensesFragmentViewModel(Application application) {
        super(application);
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @NonNull
    public String getUrl() {
        return "file:///android_asset/open_source_licenses.html";
    }
}
