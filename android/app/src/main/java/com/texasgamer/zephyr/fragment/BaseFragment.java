package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.texasgamer.zephyr.util.PreferenceManager;
import com.texasgamer.zephyr.viewmodel.BaseViewModel;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

public abstract class BaseFragment<T extends BaseViewModel> extends Fragment {

    protected T mViewModel;
    protected PreferenceManager mPreferenceManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceManager = new PreferenceManager(getContext());
        mViewModel = onCreateViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(getFragmentLayout(), container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViewBindings(view);
    }

    @LayoutRes
    protected abstract int getFragmentLayout();

    protected abstract void setViewBindings(View view);

    protected abstract T onCreateViewModel();
}
