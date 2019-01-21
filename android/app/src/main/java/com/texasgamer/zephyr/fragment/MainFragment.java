package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.View;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.adapter.HomeListAdapter;
import com.texasgamer.zephyr.provider.ZephyrCardProvider;
import com.texasgamer.zephyr.viewmodel.MenuViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class MainFragment extends BaseFragment<MenuViewModel> {

    @BindView(R.id.home_list)
    RecyclerView homeList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        homeList.setLayoutManager(new LinearLayoutManager(getContext()));
        homeList.setAdapter(new HomeListAdapter(ZephyrCardProvider.getCards(getContext())));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeListAdapter) homeList.getAdapter()).setCards(ZephyrCardProvider.getCards(getContext()));
        homeList.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void setViewBindings(View view) {

    }

    @Override
    protected MenuViewModel onCreateViewModel() {
        return new MenuViewModel(ZephyrApplication.getInstance());
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }
}
