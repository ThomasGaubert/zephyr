package com.texasgamer.zephyr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.UUID;

import io.fabric.sdk.android.Fabric;

public class MetricsManager {

    private FirebaseAnalytics mFirebaseAnalytics;
    private Context mContext;

    public MetricsManager(Context context) {
        if (!Fabric.isInitialized()) {
            Fabric.with(context, new Crashlytics());
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mContext = context;

        String uuid = getUuid();
        mFirebaseAnalytics.setUserId(uuid);
        Crashlytics.setUserIdentifier(uuid);
    }

    public void logEvent(@StringRes int iri, Bundle extras) {
        Log.i("MetricsManager", mContext.getString(iri) + ": " + (extras != null ? extras.toString() : "null"));
        firebaseEvent(iri, extras);
        fabricEvent(iri, extras);
    }

    private void firebaseEvent(@StringRes int iri, Bundle extras) {
        mFirebaseAnalytics.logEvent(mContext.getString(iri), extras);
    }

    private void fabricEvent(@StringRes int iri, Bundle extras) {
        if (!Fabric.isInitialized()) {
            Fabric.with(mContext, new Crashlytics());
        }

        CustomEvent event = new CustomEvent(mContext.getString(iri));
        if(extras != null) {
            for (String key : extras.keySet()) {
                event.putCustomAttribute(key, extras.get(key).toString());
            }
        }

        Answers.getInstance().logCustom(event);
    }

    private String getUuid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String uuid = preferences.getString(mContext.getString(R.string.pref_uuid), "");

        if (uuid.isEmpty()) {
            uuid = UUID.randomUUID().toString();
            preferences.edit().putString(mContext.getString(R.string.pref_uuid), uuid).apply();
        }

        return uuid;
    }
}
