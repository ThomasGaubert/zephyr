package com.texasgamer.zephyr.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;

public class ConfigManager {

    private final String TAG = this.getClass().getSimpleName();
    private final int CACHE_EXPIRATION_IN_SECONDS = 3600;

    private Context mContext;
    private FirebaseRemoteConfig mRemoteConfig;

    public ConfigManager(Context context) {
        mContext = context;

        if (Constants.FIREBASE_REMOTE_CONFIG_ENABLED) {
            FirebaseRemoteConfigSettings configSettings =
                    new FirebaseRemoteConfigSettings.Builder()
                            .setDeveloperModeEnabled(Constants.FIREBASE_REMOTE_CONFIG_DEV_MODE)
                            .build();

            mRemoteConfig = FirebaseRemoteConfig.getInstance();
            mRemoteConfig.setConfigSettings(configSettings);
            mRemoteConfig.setDefaults(R.xml.remote_config_defaults);

            Log.i(TAG, "Firebase Remote Config enabled (dev "
                    + (configSettings.isDeveloperModeEnabled() ? "enabled)" : "disabled)"));

            mRemoteConfig.fetch(CACHE_EXPIRATION_IN_SECONDS)
                    .addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    boolean result = mRemoteConfig.activateFetched();
                                    Log.i(TAG, "Remote config data fetched (" + result + ")");
                                }
                            }
                    )
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Failed to retrieve remote config: " + e.getMessage());
                                }
                            }
                    );
        } else {
            Log.i(TAG, "Firebase Remote Config disabled");
        }
    }

    public boolean isLoginEnabled() {
        return getBoolean("loginEnabled", Constants.FIREBASE_LOGIN_ENABLED);
    }

    public boolean isLoginCardNew() {
        return getBoolean("loginCardNew", false);
    }

    public boolean shouldShowLoginSuccessDialog() {
        return getBoolean("loginSuccessDialog", true);
    }

    private boolean getBoolean(String key, boolean fallback) {
        boolean result;
        if (Constants.FIREBASE_REMOTE_CONFIG_ENABLED) {
            result = mRemoteConfig.getBoolean(key);
        } else {
            result = fallback;
        }

        Log.i(TAG, key + " --> " + result + " (" + fallback + ")");
        return result;
    }

}
