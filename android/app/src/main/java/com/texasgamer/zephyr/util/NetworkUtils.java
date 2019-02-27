package com.texasgamer.zephyr.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Locale;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

public class NetworkUtils {

    private static final String IP_ADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static final String JOIN_CODE_PATTERN = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.)?([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static Pattern ipAddressPattern = Pattern.compile(IP_ADDRESS_PATTERN);
    private static Pattern joinCodePattern = Pattern.compile(JOIN_CODE_PATTERN);

    public static boolean isConnectedToWifi(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr != null && wifiMgr.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            return wifiInfo.getNetworkId() != -1;
        } else {
            return false;
        }
    }

    /**
     * Converts an IP address to a short join code if the IP address conforms to a partially known format.
     *
     * 192.168.x.y -> x.y
     * 192.168.0.y -> y
     * 192.170.x.y -> 192.170.x.y
     * 102.170.x.y -> 102.170.x.y
     *
     * @param ipAddress IP address to convert to join code
     * @return Join code associated with given IP address
     */
    @NonNull
    public static String ipToJoinCode(@NonNull String ipAddress) {
        String joinCode = "";
        String[] parts = ipAddress.split("\\.");

        if (parts.length == 4 && parts[0].equals("192") && parts[1].equals("168")) {
            if (!parts[2].equals("0")) {
                joinCode += parts[2] + ".";
            }

            return joinCode + parts[3];
        }

        return ipAddress;
    }

    /**
     * Converts a join code to an IP address.
     *
     * x.y -> 192.168.x.y
     * y -> 192.168.0.y
     * 192.170.x.y -> 192.170.x.y
     * 102.170.x.y -> 102.170.x.y
     *
     * @param joinCode Join code to convert to IP address
     * @return IP address associated with given join code
     */
    @NonNull
    public static String joinCodeToIp(@NonNull String joinCode) {
        String[] parts = joinCode.split("\\.");

        if (parts.length == 1) {
            return String.format(Locale.getDefault(), "192.168.0.%s", joinCode);
        } else if (parts.length == 2) {
            return String.format(Locale.getDefault(), "192.168.%s.%s", parts[0], parts[1]);
        }

        return joinCode;
    }

    /**
     * Returns if a given string is a valid join code.
     *
     * Valid join codes include:
     *  - IP addresses
     *  - A single number from 0 to 255
     *  - Two numbers from 0 to 255 separated by a period
     *
     * @param joinCode Join code to validate
     * @return If given join code is valid
     */
    public static boolean isValidJoinCode(@NonNull String joinCode) {
        if (ipAddressPattern.matcher(joinCode).matches()) {
            return true;
        } else {
            return joinCodePattern.matcher(joinCode).matches();
        }
    }
}
