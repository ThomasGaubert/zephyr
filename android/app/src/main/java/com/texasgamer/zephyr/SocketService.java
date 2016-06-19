package com.texasgamer.zephyr;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends Service {

    private String TAG = this.getClass().getSimpleName();

    private SocketServiceReceiver serviceReceiver;
    private FirebaseAnalytics firebaseAnalytics;

    private Socket socket;
    private String serverAddr;
    private String clientId;
    private boolean connected = false;
    private boolean reconnect = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if(!connected) {
            serviceReceiver = new SocketServiceReceiver();
            firebaseAnalytics = FirebaseAnalytics.getInstance(this);

            IntentFilter filter = new IntentFilter();
            filter.addAction("com.texasgamer.zephyr.SOCKET_SERVICE");
            registerReceiver(serviceReceiver, filter);

            serverAddr = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_last_addr), "");
        } else {
            Log.w(TAG, "onCreate() called while already connected!");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!serverAddr.isEmpty()) {
            Log.i(TAG, "Connecting to saved address " + serverAddr + "...");
            connect(serverAddr);
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void connect(String address) {
        if(connected) {
            Log.i(TAG, "Already connected to a server! Disconnect first.");
            return;
        }

        Log.i(TAG, "Connecting to " + address + "...");

        serverAddr = address;

        Bundle b = new Bundle();
        b.putString(getString(R.string.analytics_param_server_addr), serverAddr);
        firebaseAnalytics.logEvent(getString(R.string.analytics_event_connect), b);

        try {
            socket = IO.socket("http://" + address + "/");
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(socket != null) {
            setUpEvents();

            socket.connect();
        }
    }

    private void disconnect() {
        Log.i(TAG, "Disconnecting...");
        Bundle b = new Bundle();
        b.putString(getString(R.string.analytics_param_server_addr), serverAddr);
        firebaseAnalytics.logEvent(getString(R.string.analytics_event_disconnect), b);

        if(socket != null)
            socket.disconnect();
    }

    private void setUpEvents() {
        clientId = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getString(getString(R.string.pref_device_name), getString(R.string.pref_default_device_name));

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Bundle b = new Bundle();
                b.putString(getString(R.string.analytics_param_server_addr), serverAddr);
                firebaseAnalytics.logEvent(getString(R.string.analytics_event_version), b);

                socket.emit("version", getVersionInfo().toString());
            }
        }).on(clientId, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject msg = new JSONObject(args[0].toString());
                    JSONObject metadata = msg.getJSONObject("metadata");
                    if(metadata.getString("type").equals("version") && metadata.getInt("version") == 1) {
                        Log.i(TAG, "Connected to server at " + serverAddr);

                        Bundle b = new Bundle();
                        b.putString(getString(R.string.analytics_param_server_addr), serverAddr);
                        firebaseAnalytics.logEvent(getString(R.string.analytics_event_connected), b);

                        Intent i = new  Intent("com.texasgamer.zephyr.MAIN_ACTIVITY");
                        i.putExtra("type", "connected");
                        i.putExtra("address", serverAddr);
                        sendBroadcast(i);
                        connected = true;

                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(getString(R.string.pref_last_addr), serverAddr).apply();
                    } else if(metadata.getString("type").equals("notification-response") && metadata.getInt("version") == 1) {
                        if(msg.getJSONObject("payload").getBoolean("result")) {
                            Log.i(TAG, "Notification sent.");

                            Bundle b = new Bundle();
                            b.putString(getString(R.string.analytics_param_server_addr), serverAddr);
                            firebaseAnalytics.logEvent(getString(R.string.analytics_event_notif_sent), b);

                            Intent i = new  Intent("com.texasgamer.zephyr.MAIN_ACTIVITY");
                            i.putExtra("type", "notif-sent");
                            sendBroadcast(i);
                        } else {
                            Log.i(TAG, "Notification failed.");

                            Bundle b = new Bundle();
                            b.putString(getString(R.string.analytics_param_server_addr), serverAddr);
                            firebaseAnalytics.logEvent(getString(R.string.analytics_event_notif_failed), b);

                            Intent i = new  Intent("com.texasgamer.zephyr.MAIN_ACTIVITY");
                            i.putExtra("type", "notif-failed");
                            sendBroadcast(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("broadcast", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject msg = new JSONObject(args[0].toString());
                    JSONObject metadata = msg.getJSONObject("metadata");
                    if(metadata.getString("type").equals("broadcast-ping")) {
                        Log.i(TAG, "Responding to ping...");

                        Bundle b = new Bundle();
                        b.putString(getString(R.string.analytics_param_server_addr), serverAddr);
                        firebaseAnalytics.logEvent(getString(R.string.analytics_event_ping), b);

                        socket.emit("broadcast", getPong(metadata.getString("from")).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "Disconnected from server.");

                Bundle b = new Bundle();
                b.putString(getString(R.string.analytics_param_server_addr), serverAddr);
                firebaseAnalytics.logEvent(getString(R.string.analytics_event_disconnected), b);

                Intent i = new  Intent("com.texasgamer.zephyr.MAIN_ACTIVITY");
                i.putExtra("type", "disconnected");
                i.putExtra("address", serverAddr);
                sendBroadcast(i);
                connected = false;

                if(reconnect) {
                    Log.i(TAG, "Reconnecting to server...");
                    reconnect = false;
                    connect(serverAddr);
                }
            }
        });
    }

    private JSONObject getVersionInfo() {
        clientId = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getString(getString(R.string.pref_device_name), getString(R.string.pref_default_device_name));

        try {
            JSONObject versionInfo = new JSONObject();
            JSONObject metadata = new JSONObject();
            metadata.put("version", 1);
            metadata.put("type", "version");
            metadata.put("from", clientId);
            metadata.put("to", "");
            JSONObject payload = new JSONObject();
            payload.put("name", "Android Client");
            payload.put("version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            payload.put("versionCode", getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            payload.put("versions", new JSONArray());
            versionInfo.put("metadata", metadata);
            versionInfo.put("payload", payload);

            return versionInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }

    private JSONObject getPong(String to) {
        clientId = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getString(getString(R.string.pref_device_name), getString(R.string.pref_default_device_name));

        try {
            JSONObject pong = new JSONObject();
            JSONObject metadata = new JSONObject();
            metadata.put("version", 1);
            metadata.put("type", "broadcast-pong");
            metadata.put("from", clientId);
            metadata.put("to", to);
            JSONObject payload = new JSONObject();
            pong.put("metadata", metadata);
            pong.put("payload", payload);

            return pong;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }

    class SocketServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            if(type.equals("notification")) {
                if(connected) {
                    int id = intent.getIntExtra("id", 0);
                    String title = intent.getStringExtra("title");
                    String text = intent.getStringExtra("text");
                    socket.emit("notification", buildNotificationJSONObject(id, title, text).toString());
                }
            } else if(type.equals("connect")) {
                if(connected)
                    disconnect();

                connect(intent.getStringExtra("address"));
            } else if(type.equals("disconnect")) {
                disconnect();
            } else if(type.equals("status")) {
                Intent i = new Intent("com.texasgamer.zephyr.MAIN_ACTIVITY");
                i.putExtra("type", connected ? "connected" : "disconnected");
                i.putExtra("address", serverAddr);
                i.putExtra("silent", true);
                sendBroadcast(i);
            } else if(type.equals("test")) {
                if(connected) {
                    socket.emit("notification", buildNotificationJSONObject(0, "Test Notification",
                            "This is a test notification.").toString());
                }
            } else if(type.equals("update-devices")) {
                clientId = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                        .getString(getString(R.string.pref_device_name), getString(R.string.pref_default_device_name));

                if(connected) {
                    Log.i(TAG, "Client ID updated, reconnecting to server...");
                    reconnect = true;

                    disconnect();
                } else {
                    Log.i(TAG, "Client ID updated, will apply on next connection.");
                }
            }
        }

        private JSONObject buildNotificationJSONObject(int id, String title, String text) {
            clientId = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                    .getString(getString(R.string.pref_device_name), getString(R.string.pref_default_device_name));

            try {
                JSONObject notif = new JSONObject();
                JSONObject metadata = new JSONObject();
                metadata.put("verison", 1);
                metadata.put("type", "notification");
                metadata.put("from", clientId);
                metadata.put("to", "");

                JSONObject payload = new JSONObject();
                payload.put("id", id);
                payload.put("title", title);
                payload.put("text", text);
                payload.put("device", PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                        .getString(getString(R.string.pref_device_name), getString(R.string.pref_default_device_name)));
                notif.put("metadata", metadata);
                notif.put("payload", payload);

                return notif;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new JSONObject();
        }
    }
}
