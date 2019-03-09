package com.texasgamer.zephyr.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.activity.MainActivity;
import com.texasgamer.zephyr.model.ConnectionStatus;
import com.texasgamer.zephyr.model.NotificationPayload;
import com.texasgamer.zephyr.receiver.ZephyrBroadcastReceiver;
import com.texasgamer.zephyr.util.NetworkUtils;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;
import com.texasgamer.zephyr.util.notification.ZephyrNotificationChannel;
import com.texasgamer.zephyr.util.notification.ZephyrNotificationId;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Socket service. Maintains connection to server.
 */
public class SocketService extends Service {

    private static final String LOG_TAG = "SocketService";

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    ILogger logger;
    @Inject
    Gson gson;

    private boolean mConnected = false;
    private String mServerAddress;
    private Socket mSocket;
    private NotificationCompat.Builder mStatusNotificationBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        ZephyrApplication.getApplicationComponent().inject(this);

        createServiceNotification();

        if (!mConnected) {
            logger.log(LogPriority.VERBOSE, LOG_TAG, "onCreate");
        } else {
            logger.log(LogPriority.VERBOSE, LOG_TAG, "onCreate() called while already connected!");
            return;
        }

        mServerAddress = NetworkUtils.joinCodeToIp(preferenceManager.getString(PreferenceKeys.PREF_JOIN_CODE)) + ":" + Constants.ZEPHYR_SERVER_PORT;

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        logger.log(LogPriority.VERBOSE, LOG_TAG, "onDestroy");
        EventBus.getDefault().unregister(this);

        disconnect();

        dismissServiceNotification();
        preferenceManager.putBoolean(PreferenceKeys.PREF_IS_CONNECTED, false);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        preferenceManager.putBoolean(PreferenceKeys.PREF_IS_CONNECTED, true);

        if (mServerAddress != null && !mServerAddress.isEmpty()) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Connecting to saved address %s...", mServerAddress);
            connect(mServerAddress);
        } else {
            logger.log(LogPriority.DEBUG, LOG_TAG, "No saved address found, not connecting...");
            stopSelf();
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void connect(@NonNull String serverAddress) {
        if (mConnected) {
            logger.log(LogPriority.WARNING, LOG_TAG, "Already connected to a server! Disconnect first.");
            return;
        }

        if (!NetworkUtils.isConnectedToWifi(this)) {
            logger.log(LogPriority.WARNING, LOG_TAG, "Not connected to WiFi!");
            return;
        }

        if (serverAddress.isEmpty()) {
            logger.log(LogPriority.WARNING, LOG_TAG, "No address specified!");
            return;
        }

        logger.log(LogPriority.DEBUG, LOG_TAG, "Connecting to %s...", serverAddress);

        try {
            mSocket = IO.socket("http://" + serverAddress);
        } catch (Exception e) {
            logger.log(LogPriority.ERROR, LOG_TAG, e);
        }

        if (mSocket != null) {
            setUpEvents();
            mSocket.connect();
        }
    }

    private void disconnect() {
        logger.log(LogPriority.INFO, LOG_TAG, "Disconnecting...");

        if (mSocket != null) {
            mSocket.disconnect();
        }

        preferenceManager.putBoolean(PreferenceKeys.PREF_IS_CONNECTED, false);
    }

    private void setUpEvents() {
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Connected to server.");
            updateServiceNotification(ConnectionStatus.CONNECTED);
            mConnected = true;
        }).on(Socket.EVENT_DISCONNECT, args -> {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Disconnected from server.");
            updateServiceNotification(ConnectionStatus.DISCONNECTED);
            mConnected = false;
        });
    }

    @Subscribe
    public void onNotification(NotificationPayload notificationPayload) {
        if (!mConnected) {
            return;
        }

        mSocket.emit("post-notification", gson.toJson(notificationPayload, NotificationPayload.class));
    }

    private void createServiceNotification() {
        // Tap notification to open MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Stop SocketService
        Intent stopIntent = new Intent(this, ZephyrBroadcastReceiver.class);
        stopIntent.setAction(ZephyrBroadcastReceiver.ACTION_STOP_SOCKET_SERVICE);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0);

        mStatusNotificationBuilder = new NotificationCompat.Builder(this, ZephyrNotificationChannel.STATUS)
                .setSmallIcon(R.drawable.ic_status_notification)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.status_notif_text_connecting))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .addAction(R.drawable.ic_disconnected, getString(R.string.status_notif_action_stop),
                        snoozePendingIntent);

        startForeground(ZephyrNotificationId.SOCKET_SERVICE_STATUS, mStatusNotificationBuilder.build());
    }

    private void updateServiceNotification(@ConnectionStatus int connectionStatus) {
        if (mStatusNotificationBuilder == null) {
            logger.log(LogPriority.WARNING, LOG_TAG, "Unable to update status notification: null builder");
            return;
        }

        String statusMessage = null;

        switch (connectionStatus) {
            case ConnectionStatus.CONNECTED:
                statusMessage = getString(R.string.status_notif_text_connected);
                break;
            case ConnectionStatus.CONNECTING:
                statusMessage = getString(R.string.status_notif_text_connecting);
                break;
            case ConnectionStatus.DISCONNECTED:
                statusMessage = getString(R.string.status_notif_text_disconnected);
                break;
            case ConnectionStatus.NO_WIFI:
                statusMessage = getString(R.string.status_notif_text_no_wifi);
                break;
            case ConnectionStatus.OFFLINE:
                statusMessage = getString(R.string.status_notif_text_offline);
                break;
            case ConnectionStatus.UNKNOWN:
            default:
                statusMessage = getString(R.string.status_notif_text_unknown);
                break;
        }

        mStatusNotificationBuilder.setContentText(statusMessage);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(ZephyrNotificationId.SOCKET_SERVICE_STATUS, mStatusNotificationBuilder.build());
    }

    private void dismissServiceNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(ZephyrNotificationId.SOCKET_SERVICE_STATUS);
    }
}
