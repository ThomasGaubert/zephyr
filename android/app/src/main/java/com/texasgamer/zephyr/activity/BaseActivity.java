package com.texasgamer.zephyr.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.texasgamer.zephyr.manager.ConfigManager;
import com.texasgamer.zephyr.manager.MetricsManager;

public class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected ConfigManager mConfigManager;
    protected MetricsManager mMetricsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfigManager = new ConfigManager(this);
        mMetricsManager = new MetricsManager(this);
    }
}
