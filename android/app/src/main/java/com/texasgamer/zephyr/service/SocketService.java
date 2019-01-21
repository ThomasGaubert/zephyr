package com.texasgamer.zephyr.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.model.NotificationPayload;
import com.texasgamer.zephyr.util.NetworkUtils;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends Service {

    private static final String LOG_TAG = "SocketService";

    @Inject
    ILogger logger;
    @Inject
    Gson gson;

    private boolean mConnected = false;
    private String mServerAddress;
    private Socket socket;

    @Override
    public void onCreate() {
        ZephyrApplication.getApplicationComponent().inject(this);
        super.onCreate();

        if(!mConnected) {
            logger.log(LogPriority.VERBOSE, LOG_TAG, "onCreate");
        } else {
            logger.log(LogPriority.VERBOSE, LOG_TAG, "onCreate() called while already connected!");
            return;
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        logger.log(LogPriority.VERBOSE, LOG_TAG, "onDestroy");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mServerAddress != null && !mServerAddress.isEmpty()) {
            logger.log(LogPriority.DEBUG, LOG_TAG,"Connecting to saved address %s...", mServerAddress);
            connect(mServerAddress);
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

        if (serverAddress == null || serverAddress.isEmpty()) {
            logger.log(LogPriority.WARNING, LOG_TAG, "No address specified!");
            return;
        }

        logger.log(LogPriority.DEBUG, LOG_TAG, "Connecting to %s...", serverAddress);
        mServerAddress = serverAddress;

        try {
            socket = IO.socket("http://" + serverAddress + "/");
        } catch (Exception e) {
            logger.log(LogPriority.ERROR, LOG_TAG, e);
        }

        if(socket != null) {
            setUpEvents();
            socket.connect();
        }
    }

    private void disconnect() {
        logger.log(LogPriority.INFO, LOG_TAG, "Disconnecting...");

        if(socket != null) {
            socket.disconnect();
        }
    }

    private void setUpEvents() {
        socket.on(Socket.EVENT_CONNECT, args -> {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Connected to server.");
            mConnected = true;
        }).on(Socket.EVENT_DISCONNECT, args -> {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Disconnected from server.");
            mConnected = false;
        });
    }

    @Subscribe
    private void onNotification(NotificationPayload notificationPayload) {
        if (!mConnected) {
            return;
        }

        socket.emit("post-notification", gson.toJson(notificationPayload, NotificationPayload.class));
    }
}
