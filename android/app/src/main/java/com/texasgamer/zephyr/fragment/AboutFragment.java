package com.texasgamer.zephyr.fragment;

import android.util.TypedValue;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.databinding.FragmentAboutBinding;
import com.texasgamer.zephyr.viewmodel.AboutFragmentViewModel;

/**
 * Fragment which displays version info, etc.
 */
public class AboutFragment extends BaseFragment<AboutFragmentViewModel, FragmentAboutBinding> {

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

    @Override
    protected WindowInsets onApplyWindowInsets(@NonNull View view, @NonNull WindowInsets windowInsets) {
        super.onApplyWindowInsets(view, windowInsets);
        TypedValue actionBarSizeTypedValue = new TypedValue();
        requireContext().getTheme().resolveAttribute(R.attr.actionBarSize, actionBarSizeTypedValue, true);
        view.setPadding(0, 0, 0, getResources().getDimensionPixelSize(actionBarSizeTypedValue.resourceId) + windowInsets.getSystemWindowInsetBottom());
        return windowInsets;
    }
}
