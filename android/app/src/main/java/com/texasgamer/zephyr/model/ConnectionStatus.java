package com.texasgamer.zephyr.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({ConnectionStatus.UNKNOWN,
        ConnectionStatus.CONNECTED,
        ConnectionStatus.DISCONNECTED,
        ConnectionStatus.CONNECTING,
        ConnectionStatus.OFFLINE,
        ConnectionStatus.NO_WIFI})
@Retention(RetentionPolicy.SOURCE)
public @interface ConnectionStatus {
    int UNKNOWN = 0;
    int CONNECTED = 1;
    int DISCONNECTED = 2;
    int CONNECTING = 3;
    int OFFLINE = 4;
    int NO_WIFI = 5;
}
