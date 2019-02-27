package com.texasgamer.zephyr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.service.SocketService;
import com.texasgamer.zephyr.util.log.LogPriority;

/**
 * Broadcast receiver that handles stopping SocketService.
 */
public class ZephyrBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_STOP_SOCKET_SERVICE = BuildConfig.APPLICATION_ID + ".action.STOP_SOCKET_SERVICE";
    private static final String LOG_TAG = "ZephyrBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        ZephyrApplication.getApplicationComponent().logger().log(LogPriority.DEBUG, LOG_TAG, "Received action: " + action);
        if (action != null && action.equals(ACTION_STOP_SOCKET_SERVICE)) {
            ZephyrApplication.getApplicationComponent().logger().log(LogPriority.INFO, LOG_TAG, "Stopping SocketService...");
            context.stopService(new Intent(context, SocketService.class));
        }
    }
}
