package com.texasgamer.zephyr.util.config;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.service.threading.ZephyrExecutors;

import java.util.Map;

import androidx.annotation.NonNull;

public class ZephyrRemoteConfig {

    private static final String LOG_TAG = "ZephyrRemoteConfig";

    private Context context;
    private IConfigManager configManager;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private Map<String, Object> localConfig;

    public ZephyrRemoteConfig(@NonNull Context context, @NonNull IConfigManager configManager) {
        this.context = context;
        this.configManager = configManager;

        if (configManager.isFirebaseRemoteConfigEnabled()) {
            initFirebaseRemoteConfig();
        } else {
            initLocalConfig();
        }
    }

    public boolean getBoolean(@NonNull String key) {
        if (firebaseRemoteConfig != null) {
            return firebaseRemoteConfig.getBoolean(key);
        } else {
            return (boolean) localConfig.get(key);
        }
    }

    public String getString(@NonNull String key) {
        if (firebaseRemoteConfig != null) {
            return firebaseRemoteConfig.getString(key);
        } else {
            return (String) localConfig.get(key);
        }
    }

    private void initFirebaseRemoteConfig() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        firebaseRemoteConfig.fetch(Constants.FIREBASE_REMOTE_CONFIG_CACHE_EXPIRY_IN_SECONDS).addOnCompleteListener(ZephyrExecutors.getNetworkExecutor(), task -> {
            if (task.isSuccessful()) {
                firebaseRemoteConfig.activateFetched();
            }
        });
    }

    private void initLocalConfig() {
        XmlResourceParser xmlResourceParser = context.getResources().getXml(R.xml.remote_config_defaults);
        int eventType = -1;
        while (eventType != xmlResourceParser.END_DOCUMENT) {
            if (eventType == xmlResourceParser.START_TAG) {
                System.out.println(xmlResourceParser.getName());
            }

            try {
                eventType = xmlResourceParser.next();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
