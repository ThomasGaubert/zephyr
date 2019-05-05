package com.texasgamer.zephyr.util.analytics;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;
import com.texasgamer.zephyr.util.privacy.IPrivacyManager;

/**
 * Analytics manager.
 */
public class AnalyticsManager implements IAnalyticsManager {

    private static final String LOG_TAG = "AnalyticsManager";

    private Context mContext;
    private ILogger mLogger;
    private IConfigManager mConfigManager;
    private IPrivacyManager mPrivacyManager;

    public AnalyticsManager(@NonNull Context context,
                            @NonNull ILogger logger,
                            @NonNull IConfigManager configManager,
                            @NonNull IPrivacyManager privacyManager) {
        mContext = context;
        mLogger = logger;
        mConfigManager = configManager;
        mPrivacyManager = privacyManager;

        if (mPrivacyManager.isUsageDataCollectionEnabled()) {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
            firebaseAnalytics.setAnalyticsCollectionEnabled(true);
            firebaseAnalytics.setUserId(privacyManager.getUuid());
        } else {
            logger.log(LogPriority.WARNING, LOG_TAG, "Firebase analytics disabled.");
        }
    }

    public void logEvent(@NonNull String eventId) {
        logEvent(eventId, null);
    }

    public void logEvent(@NonNull String eventId, @Nullable Bundle params) {
        if (mConfigManager.isDev()) {
            StringBuilder paramsString = new StringBuilder("Bundle{");
            if (params != null) {
                for (String key : params.keySet()) {
                    paramsString.append(' ').append(key).append(" => ").append(params.get(key)).append(';');
                }
            }
            paramsString.append(" }");

            mLogger.log(LogPriority.VERBOSE, LOG_TAG, "Event: %s - Params: %s", eventId, paramsString);
        }

        if (mPrivacyManager.isUsageDataCollectionEnabled()) {
            FirebaseAnalytics.getInstance(mContext).logEvent(eventId, params);
        }
    }
}
