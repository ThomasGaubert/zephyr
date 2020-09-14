package com.texasgamer.zephyr.fragment;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.databinding.FragmentDefaultSecondaryBinding;
import com.texasgamer.zephyr.viewmodel.DefaultSecondaryFragmentViewModel;

/**
 * Fragment which displays version info, etc.
 */
public class DefaultSecondaryFragment extends BaseFragment<DefaultSecondaryFragmentViewModel, FragmentDefaultSecondaryBinding> {

    @Override
    @LayoutRes
    protected int getFragmentLayout() {
        return R.layout.fragment_default_secondary;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @NonNull
    @Override
    protected Class<DefaultSecondaryFragmentViewModel> getViewModelClass() {
        return DefaultSecondaryFragmentViewModel.class;
    }

    @Override
    protected void setViewBindings(@NonNull View view) {
        mDataBinding.setViewModel(mViewModel);
    }
}
