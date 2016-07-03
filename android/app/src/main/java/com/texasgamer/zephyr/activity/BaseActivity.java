package com.texasgamer.zephyr.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.texasgamer.zephyr.manager.MetricsManager;

public class BaseActivity extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();

    protected MetricsManager mMetricsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMetricsManager = new MetricsManager(this);
    }
}
