package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.View;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.adapter.HomeListAdapter;
import com.texasgamer.zephyr.provider.ZephyrCardProvider;
import com.texasgamer.zephyr.viewmodel.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Main fragment.
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.home_list)
    RecyclerView mHomeList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mHomeList.setLayoutManager(new LinearLayoutManager(getContext()));
        mHomeList.setAdapter(new HomeListAdapter(ZephyrCardProvider.getCards(this)));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeListAdapter) mHomeList.getAdapter()).setCards(ZephyrCardProvider.getCards(this));
        mHomeList.getAdapter().notifyDataSetChanged();
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
