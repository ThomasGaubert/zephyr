package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.texasgamer.zephyr.util.analytics.IAnalyticsManager;
import com.texasgamer.zephyr.util.layout.ILayoutManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.navigation.INavigationManager;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.viewmodel.BaseViewModel;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Base fragment that performs common routines.
 * @param <T> ViewModel
 * @param <B> ViewDataBinding
 */
public abstract class BaseFragment<T extends BaseViewModel, B extends ViewDataBinding> extends Fragment {

    protected B mDataBinding;
    protected T mViewModel;

    @Inject
    protected ILogger mLogger;
    @Inject
    protected IPreferenceManager mPreferenceManager;
    @Inject
    protected IAnalyticsManager mAnalyticsManager;
    @Inject
    protected ILayoutManager mLayoutManager;
    @Inject
    protected INavigationManager mNavigationManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies();

        if (mLogger == null) {
            throw new IllegalStateException("Dependencies not fulfilled for this Fragment.");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root;
        mDataBinding = DataBindingUtil.inflate(inflater, getFragmentLayout(), container, false);

        if (mDataBinding != null) {
            mDataBinding.setLifecycleOwner(requireActivity());
            root = mDataBinding.getRoot();
            mViewModel = new ViewModelProvider(this).get(getViewModelClass());
            setViewBindings(root);
        } else {
            root = inflater.inflate(getFragmentLayout(), container, false);
        }

        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @LayoutRes
    protected abstract int getFragmentLayout();

    protected abstract void injectDependencies();

    @NonNull
    protected abstract Class<T> getViewModelClass();

    protected abstract void setViewBindings(@NonNull View view);
}
