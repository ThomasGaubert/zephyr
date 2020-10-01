package com.texasgamer.zephyr.fragment

import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import androidx.annotation.LayoutRes
import com.texasgamer.zephyr.R
import com.texasgamer.zephyr.ZephyrApplication
import com.texasgamer.zephyr.databinding.FragmentAboutBinding
import com.texasgamer.zephyr.viewmodel.AboutFragmentViewModel

/**
 * Fragment which displays version info, etc.
 */
class AboutFragment : BaseFragment<AboutFragmentViewModel, FragmentAboutBinding>() {
    @LayoutRes
    override fun getFragmentLayout(): Int {
        return R.layout.fragment_about
    }

    override fun injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this)
    }

    override fun getViewModelClass(): Class<AboutFragmentViewModel> {
        return AboutFragmentViewModel::class.java
    }

    override fun setViewBindings(view: View) {
        mDataBinding.viewModel = mViewModel
    }

    override fun onApplyWindowInsets(view: View, windowInsets: WindowInsets): WindowInsets {
        super.onApplyWindowInsets(view, windowInsets)
        val actionBarSizeTypedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.actionBarSize, actionBarSizeTypedValue, true)
        view.setPadding(0, 0, 0, resources.getDimensionPixelSize(actionBarSizeTypedValue.resourceId) + windowInsets.systemWindowInsetBottom)
        return windowInsets
    }
}