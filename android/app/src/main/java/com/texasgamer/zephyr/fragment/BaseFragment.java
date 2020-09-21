package com.texasgamer.zephyr.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.texasgamer.zephyr.R;
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
    @CallSuper
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupEdgeToEdgeNavigation();
    }

    @CallSuper
    protected WindowInsets onApplyWindowInsets(@NonNull View view, @NonNull WindowInsets windowInsets) {
        return windowInsets;
    }

    @LayoutRes
    protected abstract int getFragmentLayout();

    protected abstract void injectDependencies();

    @NonNull
    protected abstract Class<T> getViewModelClass();

    protected abstract void setViewBindings(@NonNull View view);

    private void setupEdgeToEdgeNavigation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return;
        }

        requireView().setOnApplyWindowInsetsListener(this::onApplyWindowInsets);
    }
}
