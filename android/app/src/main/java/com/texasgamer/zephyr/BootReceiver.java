package com.texasgamer.zephyr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.texasgamer.zephyr.service.NotificationService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.pref_start_on_boot), true)) {
           Intent serviceIntent = new Intent(context, NotificationService.class);
           context.startService(serviceIntent);
       }
    }
}
