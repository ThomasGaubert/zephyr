package com.texasgamer.zephyr.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkUtils {

    public static boolean isConnectedToWifi(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr != null && wifiMgr.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            return wifiInfo.getNetworkId() != -1;
        } else {
            return false;
        }
    }
}
