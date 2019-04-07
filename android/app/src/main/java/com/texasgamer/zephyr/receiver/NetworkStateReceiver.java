package com.texasgamer.zephyr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class NetworkStateReceiver extends BroadcastReceiver {

    private ConnectivityManager mManager;
    private List<NetworkStateReceiverListener> mListeners;
    private boolean mConnected;
    private boolean mConnectedToWifi;

    public NetworkStateReceiver(@NonNull Context context) {
        mListeners =  new ArrayList<>();
        mManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(NetworkStateReceiver.this, intentFilter);
    }

    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null)
            return;

        if (checkStateChanged()) {
            notifyStateToAll();
        }

        if (checkWifiStateChanged()) {
            notifyWifiStateToAll();
        }
    }

    private boolean checkStateChanged() {
        boolean previousState = mConnected;
        NetworkInfo activeNetwork = mManager.getActiveNetworkInfo();
        mConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return previousState != mConnected;
    }

    private void notifyStateToAll() {
        for (NetworkStateReceiverListener listener : mListeners) {
            notifyState(listener);
        }
    }

    private void notifyState(NetworkStateReceiverListener listener) {
        if (listener != null) {
            if (mConnected) {
                listener.onNetworkAvailable();
            } else {
                listener.onNetworkUnavailable();
            }
        }
    }

    private boolean checkWifiStateChanged() {
        boolean previousState = mConnectedToWifi;
        NetworkInfo activeNetwork = mManager.getActiveNetworkInfo();
        mConnectedToWifi = activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        return previousState != mConnectedToWifi;
    }

    private void notifyWifiStateToAll() {
        for (NetworkStateReceiverListener listener : mListeners) {
            notifyWifiState(listener);
        }
    }

    private void notifyWifiState(NetworkStateReceiverListener listener) {
        if (listener != null) {
            if (mConnectedToWifi) {
                listener.onWifiConnected();
            } else {
                listener.onWifiDisconnected();
            }
        }
    }

    public void addListener(@NonNull NetworkStateReceiverListener l) {
        mListeners.add(l);
        notifyState(l);
    }

    public void removeListener(@NonNull NetworkStateReceiverListener l) {
        mListeners.remove(l);
    }

    public interface NetworkStateReceiverListener {
        void onNetworkAvailable();

        void onNetworkUnavailable();

        void onWifiConnected();

        void onWifiDisconnected();
    }
}