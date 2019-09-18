package com.texasgamer.zephyr.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.activity.MainActivity;
import com.texasgamer.zephyr.model.ConnectionStatus;
import com.texasgamer.zephyr.model.NotificationPayload;
import com.texasgamer.zephyr.model.api.ZephyrApiVersion;
import com.texasgamer.zephyr.network.ZephyrService;
import com.texasgamer.zephyr.receiver.NetworkStateReceiver;
import com.texasgamer.zephyr.receiver.ZephyrBroadcastReceiver;
import com.texasgamer.zephyr.util.NetworkUtils;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogPriority;
import com.texasgamer.zephyr.util.notification.ZephyrNotificationChannel;
import com.texasgamer.zephyr.util.notification.ZephyrNotificationId;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.SharedPreferenceLiveData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleService;
import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Socket service. Maintains connection to server.
 */
public class SocketService extends LifecycleService implements NetworkStateReceiver.NetworkStateReceiverListener {

    public static boolean instanceCreated = false;

    private static final String LOG_TAG = "SocketService";

    @Inject
    IPreferenceManager preferenceManager;
    @Inject
    ILogger logger;
    @Inject
    Gson gson;

    @ConnectionStatus
    private int mConnectionStatus = ConnectionStatus.DISCONNECTED;
    private boolean mEnableNotifications = true;
    private String mServerAddress;
    private Socket mSocket;
    private NotificationCompat.Builder mStatusNotificationBuilder;
    private SharedPreferenceLiveData<String> mJoinCode;
    private NetworkStateReceiver mNetworkStateReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        ZephyrApplication.getApplicationComponent().inject(this);

        createServiceNotification();

        mNetworkStateReceiver = new NetworkStateReceiver(this);
        mNetworkStateReceiver.addListener(this);

        mJoinCode = new SharedPreferenceLiveData<>(PreferenceKeys.PREF_JOIN_CODE);
        mJoinCode.observe(this, this::onUpdateJoinCode);

        String joinCode = preferenceManager.getString(PreferenceKeys.PREF_JOIN_CODE);
        if (joinCode != null && !joinCode.isEmpty()) {
            mServerAddress = NetworkUtils.joinCodeToIp(preferenceManager.getString(PreferenceKeys.PREF_JOIN_CODE)) + ":" + Constants.ZEPHYR_SERVER_PORT;
        }

        EventBus.getDefault().register(this);
        instanceCreated = true;
    }

    @Override
    public void onDestroy() {
        logger.log(LogPriority.VERBOSE, LOG_TAG, "onDestroy");

        mEnableNotifications = false;

        preferenceManager.putBoolean(PreferenceKeys.PREF_IS_SOCKET_SERVICE_RUNNING, false);
        preferenceManager.putInt(PreferenceKeys.PREF_CONNECTION_STATUS, ConnectionStatus.DISCONNECTED);

        EventBus.getDefault().unregister(this);
        unregisterReceiver(mNetworkStateReceiver);

        disconnect(ConnectionStatus.DISCONNECTED);

        dismissServiceNotification();
        instanceCreated = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        preferenceManager.putBoolean(PreferenceKeys.PREF_IS_SOCKET_SERVICE_RUNNING, true);
        preferenceManager.putInt(PreferenceKeys.PREF_CONNECTION_STATUS, ConnectionStatus.DISCONNECTED);

        if (mServerAddress != null && !mServerAddress.isEmpty()) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Connecting to saved address %s...", mServerAddress);
            connect();
        } else {
            logger.log(LogPriority.DEBUG, LOG_TAG, "No saved address found, will attempt to reconnect if set...");
            updateServiceNotification(ConnectionStatus.NO_JOIN_CODE);
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    private void connect() {
        if (NetworkUtils.connectionStatusToIsConnected(mConnectionStatus)) {
            logger.log(LogPriority.WARNING, LOG_TAG, "Already connected to a server! Disconnect first.");
            return;
        }

        if (mConnectionStatus == ConnectionStatus.CONNECTING) {
            logger.log(LogPriority.WARNING, LOG_TAG, "Already attempting to connect to a server! Disconnect first.");
            return;
        }

        if (!NetworkUtils.isConnectedToWifi(this)) {
            logger.log(LogPriority.WARNING, LOG_TAG, "Not connected to WiFi!");
            updateServiceNotification(ConnectionStatus.NO_WIFI);
            return;
        }

        if (mServerAddress == null || mServerAddress.isEmpty()) {
            logger.log(LogPriority.WARNING, LOG_TAG, "No address specified!");
            updateServiceNotification(ConnectionStatus.NO_JOIN_CODE);
            return;
        }

        logger.log(LogPriority.DEBUG, LOG_TAG, "Connecting to %s...", mServerAddress);
        updateServiceNotification(ConnectionStatus.CONNECTING);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + mServerAddress + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ZephyrService zephyrService = retrofit.create(ZephyrService.class);
        zephyrService.listRepos().enqueue(new Callback<ZephyrApiVersion>() {
            @Override
            public void onResponse(@NonNull Call<ZephyrApiVersion> call, @NonNull Response<ZephyrApiVersion> response) {
                if (!response.isSuccessful()) {
                    logger.log(LogPriority.WARNING, LOG_TAG, "Zephyr server API request failed, abandoning!");
                    updateServiceNotification(ConnectionStatus.UNKNOWN);
                    return;
                }

                ZephyrApiVersion zephyrApiVersion = response.body();
                if (zephyrApiVersion == null) {
                    logger.log(LogPriority.WARNING, LOG_TAG, "Zephyr server returned null body, abandoning!");
                    updateServiceNotification(ConnectionStatus.UNKNOWN);
                    return;
                }

                logger.log(LogPriority.INFO, LOG_TAG, "Connected to server running API v%d (%s-%s)",
                        zephyrApiVersion.getApi(), zephyrApiVersion.getDesktop(), zephyrApiVersion.getBuildType());

                if (zephyrApiVersion.getApi() != Constants.ZEPHYR_API_VERSION) {
                    logger.log(LogPriority.WARNING, LOG_TAG, "Zephyr server returned API %d but app only supports API v%d, abandoning!",
                            zephyrApiVersion.getApi(), Constants.ZEPHYR_API_VERSION);
                    updateServiceNotification(ConnectionStatus.UNSUPPORTED_API);
                    return;
                }

                try {
                    mSocket = IO.socket("http://" + mServerAddress);
                } catch (Exception e) {
                    logger.log(LogPriority.ERROR, LOG_TAG, e);
                }

                if (mSocket != null) {
                    setUpEvents();
                    mSocket.connect();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ZephyrApiVersion> call, @NonNull Throwable t) {
                logger.log(LogPriority.WARNING, LOG_TAG, "Zephyr server not found!");
                updateServiceNotification(ConnectionStatus.SERVER_NOT_FOUND);
            }
        });
    }

    private void disconnect(@ConnectionStatus int newConnectionStatus) {
        logger.log(LogPriority.INFO, LOG_TAG, "Disconnecting...");

        if (mSocket != null) {
            mSocket.disconnect();
        }

        updateServiceNotification(newConnectionStatus);
    }

    private void setUpEvents() {
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Connected to server.");
            updateServiceNotification(ConnectionStatus.CONNECTED);
        }).on(Socket.EVENT_DISCONNECT, args -> {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Disconnected from server.");
            updateServiceNotification(ConnectionStatus.DISCONNECTED);
        });
    }

    @Subscribe
    public void onNotification(NotificationPayload notificationPayload) {
        if (!NetworkUtils.connectionStatusToIsConnected(mConnectionStatus)) {
            logger.log(LogPriority.DEBUG, LOG_TAG, "Ignoring notification since currently disconnected from server.");
            return;
        }

        mSocket.emit("post-notification", gson.toJson(notificationPayload, NotificationPayload.class));
    }

    private void onUpdateJoinCode(@Nullable String joinCode) {
        if (joinCode == null || joinCode.isEmpty()) {
            logger.log(LogPriority.WARNING, LOG_TAG, "Updated join code is null!");
            return;
        }

        String newServerAddress = NetworkUtils.joinCodeToIp(preferenceManager.getString(PreferenceKeys.PREF_JOIN_CODE)) + ":" + Constants.ZEPHYR_SERVER_PORT;
        if (newServerAddress.equals(mServerAddress)) {
            logger.log(LogPriority.INFO, LOG_TAG, "Join code didn't change, ignoring.");
            return;
        }

        logger.log(LogPriority.INFO, LOG_TAG, "Join code was updated! Handling...");
        disconnect(ConnectionStatus.DISCONNECTED);
        mServerAddress = newServerAddress;
        connect();
    }

    private void createServiceNotification() {
        if (!mEnableNotifications) {
            logger.log(LogPriority.WARNING, LOG_TAG, "Not creating notification: disabled");
            return;
        }

        // Tap notification to open MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Stop SocketService
        Intent stopIntent = new Intent(this, ZephyrBroadcastReceiver.class);
        stopIntent.setAction(ZephyrBroadcastReceiver.ACTION_STOP_SOCKET_SERVICE);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0);

        mStatusNotificationBuilder = new NotificationCompat.Builder(this, ZephyrNotificationChannel.STATUS)
                .setSmallIcon(R.drawable.ic_link)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.status_notif_text_disconnected))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setAutoCancel(false)
                .setLocalOnly(true)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .addAction(R.drawable.ic_disconnected, getString(R.string.status_notif_action_stop), stopPendingIntent);

        startForeground(ZephyrNotificationId.SOCKET_SERVICE_STATUS, mStatusNotificationBuilder.build());
    }

    private void updateServiceNotification(@ConnectionStatus int connectionStatus) {
        if (mStatusNotificationBuilder == null) {
            logger.log(LogPriority.WARNING, LOG_TAG, "Unable to update status notification: null builder");
            dismissServiceNotification();
            return;
        } else if (!mEnableNotifications) {
            logger.log(LogPriority.WARNING, LOG_TAG, "Not updating notification: disabled");
            dismissServiceNotification();
            return;
        }

        mConnectionStatus = connectionStatus;

        String statusMessage = NetworkUtils.connectionStatusToString(this, connectionStatus);

        preferenceManager.putInt(PreferenceKeys.PREF_CONNECTION_STATUS, connectionStatus);

        mStatusNotificationBuilder.setContentText(statusMessage);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(ZephyrNotificationId.SOCKET_SERVICE_STATUS, mStatusNotificationBuilder.build());
    }

    private void dismissServiceNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(ZephyrNotificationId.SOCKET_SERVICE_STATUS);
    }

    @Override
    public void onNetworkAvailable() {
        if (!NetworkUtils.connectionStatusToIsConnected(mConnectionStatus) && mConnectionStatus != ConnectionStatus.CONNECTING) {
            logger.log(LogPriority.INFO, LOG_TAG, "Network available.");
            connect();
        }
    }

    @Override
    public void onNetworkUnavailable() {
        if (NetworkUtils.connectionStatusToIsConnected(mConnectionStatus) || mConnectionStatus == ConnectionStatus.CONNECTING) {
            logger.log(LogPriority.INFO, LOG_TAG, "Network not available.");
            disconnect(ConnectionStatus.OFFLINE);
        }
    }

    @Override
    public void onWifiConnected() {
        if (!NetworkUtils.connectionStatusToIsConnected(mConnectionStatus) && mConnectionStatus != ConnectionStatus.CONNECTING) {
            logger.log(LogPriority.INFO, LOG_TAG, "Connected to WiFi.");
            connect();
        }
    }

    @Override
    public void onWifiDisconnected() {
        if (NetworkUtils.connectionStatusToIsConnected(mConnectionStatus) || mConnectionStatus == ConnectionStatus.CONNECTING) {
            logger.log(LogPriority.INFO, LOG_TAG, "WiFi disconnected.");
            disconnect(ConnectionStatus.NO_WIFI);
        }
    }
}
