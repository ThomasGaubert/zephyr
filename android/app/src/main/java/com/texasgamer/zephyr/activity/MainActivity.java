package com.texasgamer.zephyr.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.fragment.MenuFragment;

import butterknife.BindView;

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
}
