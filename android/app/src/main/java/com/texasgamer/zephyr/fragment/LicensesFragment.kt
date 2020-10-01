package com.texasgamer.zephyr.fragment

import android.view.View
import com.texasgamer.zephyr.R
import com.texasgamer.zephyr.ZephyrApplication
import com.texasgamer.zephyr.databinding.FragmentLicensesBinding
import com.texasgamer.zephyr.viewmodel.LicensesFragmentViewModel

/**
 * Fragment to display open source licenses.
 */
class LicensesFragment : BaseFragment<LicensesFragmentViewModel, FragmentLicensesBinding>() {
    override fun getFragmentLayout(): Int {
        return R.layout.fragment_licenses
    }

    override fun injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this)
    }

    override fun getViewModelClass(): Class<LicensesFragmentViewModel> {
        return LicensesFragmentViewModel::class.java
    }

    override fun setViewBindings(view: View) {
        mDataBinding.viewModel = mViewModel
    }
}