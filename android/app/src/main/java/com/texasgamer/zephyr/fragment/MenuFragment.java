package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.activity.AboutActivity;
import com.texasgamer.zephyr.activity.NotificationActivity;
import com.texasgamer.zephyr.util.NavigationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuFragment extends RoundedBottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_menu)
    NavigationView navigationView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navigationView.setNavigationItemSelectedListener(this);
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
