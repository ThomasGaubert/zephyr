package com.texasgamer.zephyr.fragment;

import android.view.View;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.databinding.FragmentLicensesBinding;
import com.texasgamer.zephyr.viewmodel.LicensesFragmentViewModel;

/**
 * Fragment to display open source licenses.
 */
public class LicensesFragment extends BaseFragment<LicensesFragmentViewModel, FragmentLicensesBinding> {

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_licenses;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @NonNull
    @Override
    protected Class<LicensesFragmentViewModel> getViewModelClass() {
        return LicensesFragmentViewModel.class;
    }

    @Override
    protected void setViewBindings(@NonNull View view) {
        mDataBinding.setViewModel(mViewModel);
    }
}
