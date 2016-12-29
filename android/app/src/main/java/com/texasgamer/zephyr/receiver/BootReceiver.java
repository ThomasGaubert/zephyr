package com.texasgamer.zephyr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.service.NotificationService;
import com.texasgamer.zephyr.service.SocketService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       Intent socketService = new Intent(context, SocketService.class);
       context.startService(socketService);

       Intent notificationService = new Intent(context, NotificationService.class);
       context.startService(notificationService);
    }
}
