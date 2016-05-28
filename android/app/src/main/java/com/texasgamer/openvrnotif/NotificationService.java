package com.texasgamer.openvrnotif;

import android.app.Notification;
import android.content.Intent;
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

        Notification n = sbn.getNotification();
        String title = n.extras.getString(Notification.EXTRA_TITLE);
        String text = n.extras.getString(Notification.EXTRA_TEXT);

        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getPackageName() + "\t" + title + "\t" + text);
        Intent i = new  Intent("com.texasgamer.openvrnotif.SOCKET_SERVICE");
        i.putExtra("type", "notification");
        i.putExtra("id", sbn.getId());
        i.putExtra("package", sbn.getPackageName());
        i.putExtra("title", title);
        i.putExtra("text", text);
        sendBroadcast(i);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "onNotificationRemoved");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
    }
}
