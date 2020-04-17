package com.texasgamer.zephyr.util.config;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.threading.ZephyrExecutors;
import com.texasgamer.zephyr.util.log.LogPriority;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Config provider that sources from either Firebase or local configuration file.
 */
public class ZephyrConfigProvider {

    private static final String LOG_TAG = "ZephyrConfigProvider";

    private Context mContext;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private Map<String, String> mLocalConfig;

    public ZephyrConfigProvider(@NonNull Context context, @NonNull IConfigManager configManager) {
        this.mContext = context;

        if (configManager.isFirebaseRemoteConfigEnabled()) {
            initFirebaseRemoteConfig();
        } else {
            try {
                initLocalConfig();
            } catch (Exception e) {
                ZephyrApplication.getApplicationComponent().logger().log(LogPriority.ERROR, LOG_TAG, e);
            }
        }
    }

    public boolean getBoolean(@NonNull String key) {
        if (mFirebaseRemoteConfig != null) {
            return mFirebaseRemoteConfig.getBoolean(key);
        } else {
            return Boolean.parseBoolean(mLocalConfig.get(key));
        }
    }

    public String getString(@NonNull String key) {
        if (mFirebaseRemoteConfig != null) {
            return mFirebaseRemoteConfig.getString(key);
        } else {
            return mLocalConfig.get(key);
        }
    }

    private void initFirebaseRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.config_defaults);
        mFirebaseRemoteConfig.fetch(Constants.FIREBASE_REMOTE_CONFIG_CACHE_EXPIRY_IN_SECONDS).addOnCompleteListener(ZephyrExecutors.getNetworkExecutor(), task -> {
            if (task.isSuccessful()) {
                mFirebaseRemoteConfig.activateFetched();
            }
        });
    }

    private void initLocalConfig() throws Exception {
        XmlResourceParser xmlResourceParser = mContext.getResources().getXml(R.xml.config_defaults);

        int eventType = -1;
        String key = null;
        String value = null;
        mLocalConfig = new HashMap<>();

        while (eventType != xmlResourceParser.END_DOCUMENT) {
            if (eventType == xmlResourceParser.START_TAG) {
                if (xmlResourceParser.getName().equals("key")) {
                    // Key and value should be empty
                    if (key != null || value != null) {
                        throw new IllegalStateException("Invalid local config: bad key-value state");
                    }

                    // Go to next element and verify
                    eventType = xmlResourceParser.next();
                    if (eventType != xmlResourceParser.TEXT) {
                        throw new IllegalStateException("Invalid local config: error when getting key");
                    }

                    // Get key and validate
                    key = xmlResourceParser.getText();
                    if (key == null) {
                        throw new IllegalStateException("Invalid local config: null key");
                    }

                    continue;
                }

                if (xmlResourceParser.getName().equals("value")) {
                    // Key should be set, value should be empty
                    if (key == null || value != null) {
                        throw new IllegalStateException("Invalid local config: bad key-value state");
                    }

                    // Go to next element and verify
                    eventType = xmlResourceParser.next();
                    if (eventType != xmlResourceParser.TEXT) {
                        throw new IllegalStateException("Invalid local config: error when getting value");
                    }

                    // Get value and validate
                    value = xmlResourceParser.getText();
                    if (value == null) {
                        throw new IllegalStateException("Invalid local config: null value");
                    }

                    // Store key-value pair
                    mLocalConfig.put(key, xmlResourceParser.getText());

                    // Reset key and value
                    key = null;
                    value = null;
                }
            }

            eventType = xmlResourceParser.next();
        }
    }
}
