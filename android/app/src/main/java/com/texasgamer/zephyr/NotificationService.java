package com.texasgamer.zephyr;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "onNotificationPosted");

        if(PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(getString(R.string.pref_app_notif_base) + "-" + sbn.getPackageName(), true)) {
            Notification n = sbn.getNotification();
            String title = null;
            String text = null;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                title = n.extras.getString(Notification.EXTRA_TITLE);
                text = n.extras.getString(Notification.EXTRA_TEXT);
            } else {
                try {
                    title = getPackageManager().getApplicationLabel(getPackageManager()
                                .getApplicationInfo(sbn.getPackageName(), PackageManager.GET_META_DATA)).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    title = getString(R.string.notif_default_title);
                }
                text = n.tickerText.toString();
            }

            Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getPackageName() + "\t" + title + "\t" + text);
            Intent i = new  Intent("com.texasgamer.zephyr.SOCKET_SERVICE");
            i.putExtra("type", "notification");
            i.putExtra("id", sbn.getId());
            i.putExtra("package", sbn.getPackageName());
            i.putExtra("title", title);
            i.putExtra("text", text);
            sendBroadcast(i);
        } else {
            Log.i(TAG, "Ignoring notification from " + sbn.getPackageName());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "onNotificationRemoved");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
    }
}
