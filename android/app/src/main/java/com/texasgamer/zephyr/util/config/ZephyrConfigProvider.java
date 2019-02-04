package com.texasgamer.zephyr.util.config;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.service.threading.ZephyrExecutors;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class ZephyrConfigProvider {

    private Context context;
    private IConfigManager configManager;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private Map<String, String> localConfig;

    public ZephyrConfigProvider(@NonNull Context context, @NonNull IConfigManager configManager) {
        this.context = context;
        this.configManager = configManager;

        if (configManager.isFirebaseRemoteConfigEnabled()) {
            initFirebaseRemoteConfig();
        } else {
            try {
                initLocalConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean getBoolean(@NonNull String key) {
        if (firebaseRemoteConfig != null) {
            return firebaseRemoteConfig.getBoolean(key);
        } else {
            return Boolean.parseBoolean(localConfig.get(key));
        }
    }

    public String getString(@NonNull String key) {
        if (firebaseRemoteConfig != null) {
            return firebaseRemoteConfig.getString(key);
        } else {
            return localConfig.get(key);
        }
    }

    private void initFirebaseRemoteConfig() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.config_defaults);
        firebaseRemoteConfig.fetch(Constants.FIREBASE_REMOTE_CONFIG_CACHE_EXPIRY_IN_SECONDS).addOnCompleteListener(ZephyrExecutors.getNetworkExecutor(), task -> {
            if (task.isSuccessful()) {
                firebaseRemoteConfig.activateFetched();
            }
        });
    }

    private void initLocalConfig() throws Exception {
        XmlResourceParser xmlResourceParser = context.getResources().getXml(R.xml.config_defaults);

        int eventType = -1;
        String key = null;
        String value = null;
        localConfig = new HashMap<>();

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
                    localConfig.put(key, xmlResourceParser.getText());

                    // Reset key and value
                    key = null;
                    value = null;
                }
            }

            eventType = xmlResourceParser.next();
        }
    }
}
