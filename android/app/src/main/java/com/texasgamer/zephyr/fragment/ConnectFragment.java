package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.texasgamer.zephyr.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectFragment extends RoundedBottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_menu)
    NavigationView navigationView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_connect, container, false);
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
            case R.id.action_scan_code:
                ScanCodeFragment scanCodeFragment = new ScanCodeFragment();
                scanCodeFragment.show(getFragmentManager(), scanCodeFragment.getTag());
                break;
            case R.id.action_enter_code:
                JoinCodeFragment joinCodeFragment = new JoinCodeFragment();
                joinCodeFragment.show(getFragmentManager(), joinCodeFragment.getTag());
                break;
        }

        dismiss();

        return false;
    }
}
