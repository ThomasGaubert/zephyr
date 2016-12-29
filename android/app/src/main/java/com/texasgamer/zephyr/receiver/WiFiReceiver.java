package com.texasgamer.zephyr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.service.NotificationService;

public class WiFiReceiver extends BroadcastReceiver {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.pref_smart_connect), true)) {
            Log.i(TAG, "Smart connect is disabled, so ignoring network state change.");
        }

        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.isConnected()) {
                String address = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_last_addr), "");

                if (address.trim().isEmpty()) {
                    Log.i(TAG, "No previously known address, not attempting to connect!");
                }

                Log.i(TAG, "Connected to WiFi, attempting to connect to the server...");
                Intent i = new  Intent("com.texasgamer.zephyr.SOCKET_SERVICE");
                i.putExtra("type", "connect");
                i.putExtra("address", address);
                context.sendBroadcast(i);
            } else {
                Log.i(TAG, "No longer connected to WiFi, attempting to disconnect from the server...");
                Intent i = new  Intent("com.texasgamer.zephyr.SOCKET_SERVICE");
                i.putExtra("type", "disconnect");
                context.sendBroadcast(i);
            }
        }
    }
}
