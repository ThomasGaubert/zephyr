package com.texasgamer.zephyr.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.ArrayMap;

import com.google.gson.Gson;
import com.texasgamer.zephyr.model.NotificationPreference;
import com.texasgamer.zephyr.provider.AppProvider;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotificationPreferenceManager {

    private static final String LOG_TAG = "NotificationPreferenceManager";

    private Context mContext;
    private ILogger mLogger;
    private PreferenceManager mPreferenceManager;
    private AppProvider mAppProvider;
    private Gson mGson;
    private Map<String, NotificationPreference> mNotificationPreferences;

    public NotificationPreferenceManager(@NonNull Context context,
                                         @NonNull ILogger logger,
                                         @NonNull PreferenceManager preferenceManager,
                                         @NonNull AppProvider appProvider,
                                         @NonNull Gson gson) {
        mContext = context;
        mLogger = logger;
        mPreferenceManager = preferenceManager;
        mAppProvider = appProvider;
        mGson = gson;

        loadFromPreferences();
        syncApps();
    }

    @NonNull
    public List<NotificationPreference> getNotificationPreferences() {
        List<NotificationPreference> preferences = new ArrayList<>(mNotificationPreferences.values());
//        Collections.sort(preferences, (object1, object2) -> object1.appName.compareTo(object2.appName));
        return preferences;
    }

    @Nullable
    public NotificationPreference getNotificationPreference(@NonNull String packageName) {
        return mNotificationPreferences.get(packageName);
    }

    public void save() {
        mLogger.log(LogPriority.INFO, LOG_TAG, "Saving notification preferences...");
        List<NotificationPreference> preferences = new ArrayList<>(mNotificationPreferences.values());
        mPreferenceManager.putString(mContext, getPreferenceKey(), mGson.toJson(preferences));
        mLogger.log(LogPriority.INFO, LOG_TAG, "Done saving notification preferences.");
    }

    private void syncApps() {
        mLogger.log(LogPriority.INFO, LOG_TAG, "Syncing apps...");
        Iterator<String> it = mNotificationPreferences.keySet().iterator();
        while (it.hasNext()) {
            String packageName = it.next();
            if (!mAppProvider.isAppInstalled(packageName)) {
                it.remove();
            }
        }

        for (PackageInfo packageInfo : mAppProvider.getApps()) {
            if (!mNotificationPreferences.containsKey(packageInfo.packageName)) {
                mNotificationPreferences.put(packageInfo.packageName, getDefaultNotificationPreference(packageInfo));
            }
        }

        save();
        mLogger.log(LogPriority.INFO, LOG_TAG, "Done syncing apps.");
    }

    private void loadFromPreferences() {
        mLogger.log(LogPriority.INFO, LOG_TAG, "Loading notification preferences...");
        mNotificationPreferences = new ArrayMap<>();
        NotificationPreference[] prefs = mGson.fromJson(mPreferenceManager.getString(mContext, getPreferenceKey()), NotificationPreference[].class);

        if (prefs == null) {
            return;
        }

//        for (NotificationPreference pref : prefs) {
//            pref.appName = mAppProvider.getAppName(pref.packageName).toString();
//            pref.appIcon = mAppProvider.getAppIcon(pref.packageName);
//            mNotificationPreferences.put(pref.packageName, pref);
//        }
        mLogger.log(LogPriority.INFO, LOG_TAG, "Done loading notification preferences.");
    }

    private NotificationPreference getDefaultNotificationPreference(@NonNull PackageInfo packageInfo) {
//        NotificationPreference pref = new NotificationPreference();
//        pref.packageName = packageInfo.packageName;
//        pref.appName = mAppProvider.getAppName(packageInfo.packageName).toString();
//        pref.appIcon = mAppProvider.getAppIcon(packageInfo.packageName);
//        pref.enabled = true;
//
//        return pref;
        return null;
    }

    @NonNull
    private String getPreferenceKey() {
        return "notification-settings";
    }
}
