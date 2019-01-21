package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.activity.AboutActivity;
import com.texasgamer.zephyr.activity.NotificationActivity;
import com.texasgamer.zephyr.databinding.MenuFragmentBinding;
import com.texasgamer.zephyr.util.NavigationUtils;
import com.texasgamer.zephyr.viewmodel.MenuViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuFragment extends RoundedBottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_menu)
    NavigationView navigationView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.menu_fragment, container, false);
        ButterKnife.bind(this, root);
        MenuFragmentBinding binding = DataBindingUtil.bind(root);
        binding.setViewModel(createViewModel());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private MenuViewModel createViewModel() {
        return new MenuViewModel(ZephyrApplication.getInstance());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_manage_notifications:
                NavigationUtils.openActivity(getContext(), NotificationActivity.class);
                break;
            case R.id.action_help:
                NavigationUtils.openUrl(getContext(), Constants.ZEPHYR_HELP_URL);
                break;
            case R.id.action_about:
                NavigationUtils.openActivity(getContext(), AboutActivity.class);
                break;
        }

        dismiss();

        return false;
    }
}
