package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.texasgamer.zephyr.util.preference.PreferenceManager;
import com.texasgamer.zephyr.viewmodel.BaseViewModel;

import javax.inject.Inject;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

/**
 * Base fragment that performs common routines.
 * @param <T>
 */
public abstract class BaseFragment<T extends BaseViewModel> extends Fragment {

    protected T mViewModel;

    @Inject
    protected PreferenceManager mPreferenceManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = onCreateViewModel();

        injectDependencies();

        if (mPreferenceManager == null) {
            throw new IllegalStateException("Dependencies not fulfilled for this Fragment.");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(getFragmentLayout(), container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViewBindings(view);
    }

    @LayoutRes
    protected abstract int getFragmentLayout();

    protected abstract void setViewBindings(View view);

    protected abstract T onCreateViewModel();

    protected abstract void injectDependencies();
}
