package com.texasgamer.zephyr.fragment

import android.view.View
import androidx.annotation.LayoutRes
import com.texasgamer.zephyr.R
import com.texasgamer.zephyr.ZephyrApplication
import com.texasgamer.zephyr.databinding.FragmentDefaultSecondaryBinding
import com.texasgamer.zephyr.viewmodel.DefaultSecondaryFragmentViewModel

/**
 * Fragment which displays version info, etc.
 */
class DefaultSecondaryFragment : BaseFragment<DefaultSecondaryFragmentViewModel, FragmentDefaultSecondaryBinding>() {
    @LayoutRes
    override fun getFragmentLayout(): Int {
        return R.layout.fragment_default_secondary
    }

    override fun injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this)
    }

    override fun getViewModelClass(): Class<DefaultSecondaryFragmentViewModel> {
        return DefaultSecondaryFragmentViewModel::class.java
    }

    override fun setViewBindings(view: View) {
        mDataBinding.viewModel = mViewModel
    }
}