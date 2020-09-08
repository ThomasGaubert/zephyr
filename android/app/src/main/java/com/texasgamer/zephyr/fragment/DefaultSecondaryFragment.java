package com.texasgamer.zephyr.fragment;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.viewmodel.BaseViewModel;

/**
 * Fragment which displays version info, etc.
 */
public class DefaultSecondaryFragment extends BaseFragment {

    @Override
    @LayoutRes
    protected int getFragmentLayout() {
        return R.layout.fragment_default_secondary;
    }

    @Override
    protected void setViewBindings(@NonNull View view) {

    }

    @Override
    protected BaseViewModel onCreateViewModel() {
        return null;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }
}
