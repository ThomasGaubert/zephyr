package com.texasgamer.zephyr.activity;

import android.os.Bundle;

import com.texasgamer.zephyr.util.PreferenceManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected PreferenceManager mPreferenceManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceManager = new PreferenceManager(this);
        setupContent();
    }

    private void setupContent() {
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
    }

    @LayoutRes
    protected abstract int getLayoutResource();
}
