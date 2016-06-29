package com.texasgamer.zephyr;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MetricsManager {

    private FirebaseAnalytics mFirebaseAnalytics;
    private Context mContext;

    public MetricsManager(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mContext = context;
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
        CustomEvent event = new CustomEvent(mContext.getString(iri));

        if(extras != null) {
            for (String key : extras.keySet()) {
                event.putCustomAttribute(key, extras.get(key).toString());
            }
        }

        Answers.getInstance().logCustom(event);
    }
}
