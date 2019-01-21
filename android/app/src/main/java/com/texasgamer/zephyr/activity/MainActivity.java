package com.texasgamer.zephyr.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.fragment.MenuFragment;
import com.texasgamer.zephyr.service.SocketService;
import com.texasgamer.zephyr.view.CheckableMaterialButton;

import androidx.room.OnConflictStrategy;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_app_bar)
    BottomAppBar bottomAppBar;

    private MenuFragment mMenuFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMenuFragment = new MenuFragment();
        setSupportActionBar(bottomAppBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMenuFragment.show(getSupportFragmentManager(), mMenuFragment.getTag());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.connect_button)
    public void onClickConnectButton(View view) {
        Intent socketServiceIntent = new Intent(view.getContext(), SocketService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(socketServiceIntent);
        } else {
            startService(socketServiceIntent);
        }
    }
}
