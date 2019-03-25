package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.View;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.adapter.ZephyrCardViewPagerAdapter;
import com.texasgamer.zephyr.provider.ZephyrCardProvider;
import com.texasgamer.zephyr.view.ZephyrCardViewPager;
import com.texasgamer.zephyr.viewmodel.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * Main fragment.
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.main_carousel)
    ZephyrCardViewPager mZephyrCardViewPager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mZephyrCardViewPager.setAdapter(new ZephyrCardViewPagerAdapter(getContext(), ZephyrCardProvider.getCards(this)));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void setViewBindings(View view) {

    }

    @Override
    protected BaseViewModel onCreateViewModel() {
        return null;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }
}
