package com.texasgamer.zephyr.model.discovery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data sent by server in discovery broadcast.
 */
public class DiscoveryPacket {

    @Expose
    @SerializedName("timestamp")
    private long mTimestamp;
    @Expose
    @SerializedName("apiVersion")
    private int mApiVersion;
    @Expose
    @SerializedName("port")
    private int mPort;

    public long getTimestamp() {
        return mTimestamp;
    }

    public int getApiVersion() {
        return mApiVersion;
    }

    public int getPort() {
        return mPort;
    }
}
