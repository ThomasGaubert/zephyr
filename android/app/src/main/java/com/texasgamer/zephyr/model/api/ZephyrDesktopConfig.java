package com.texasgamer.zephyr.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Zephyr desktop configuration.
 */
public class ZephyrDesktopConfig {

    @Expose
    @SerializedName("type")
    private String mType;
    @Expose
    @SerializedName("updatesEnabled")
    private Boolean mUpdatesEnabled;
    @Expose
    @SerializedName("port")
    private Integer mPort;

    public String getType() {
        return mType;
    }

    public Boolean getUpdatesEnabled() {
        return mUpdatesEnabled;
    }

    public Integer getPort() {
        return mPort;
    }
}
