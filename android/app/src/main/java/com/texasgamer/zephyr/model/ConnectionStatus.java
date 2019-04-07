package com.texasgamer.zephyr.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Connection status to Zephyr server.
 */
@IntDef({ConnectionStatus.UNKNOWN,
        ConnectionStatus.CONNECTED,
        ConnectionStatus.DISCONNECTED,
        ConnectionStatus.CONNECTING,
        ConnectionStatus.OFFLINE,
        ConnectionStatus.NO_WIFI,
        ConnectionStatus.NO_JOIN_CODE})
@Retention(RetentionPolicy.SOURCE)
public @interface ConnectionStatus {
    int UNKNOWN = 0;
    int CONNECTED = 1;
    int DISCONNECTED = 2;
    int CONNECTING = 3;
    int OFFLINE = 4;
    int NO_WIFI = 5;
    int NO_JOIN_CODE = 6;
}
