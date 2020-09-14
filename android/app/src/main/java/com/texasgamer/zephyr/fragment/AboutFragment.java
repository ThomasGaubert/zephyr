package com.texasgamer.zephyr.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.databinding.FragmentAboutBinding;
import com.texasgamer.zephyr.viewmodel.AboutFragmentViewModel;

/**
 * Fragment which displays version info, etc.
 */
public class AboutFragment extends BaseFragment<AboutFragmentViewModel, FragmentAboutBinding> {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setupEdgeToEdgeLayout();
        }
    }

    @Override
    @LayoutRes
    protected int getFragmentLayout() {
        return R.layout.fragment_about;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @NonNull
    @Override
    protected Class<AboutFragmentViewModel> getViewModelClass() {
        return AboutFragmentViewModel.class;
    }

    @Override
    protected void setViewBindings(@NonNull View view) {
        mDataBinding.setViewModel(mViewModel);
    }

    private void setupEdgeToEdgeLayout() {
        requireView().setOnApplyWindowInsetsListener((v, insets) -> {
            TypedValue actionBarSizeTypedValue = new TypedValue();
            requireContext().getTheme().resolveAttribute(R.attr.actionBarSize, actionBarSizeTypedValue, true);
            v.setPadding(0, 0, 0, getResources().getDimensionPixelSize(actionBarSizeTypedValue.resourceId) + insets.getSystemWindowInsetBottom());
            return insets;
        });
    }
}
