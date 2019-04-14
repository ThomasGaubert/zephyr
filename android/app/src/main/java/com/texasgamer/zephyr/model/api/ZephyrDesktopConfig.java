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

    public void setType(String type) {
        this.mType = type;
    }

    public Boolean getUpdatesEnabled() {
        return mUpdatesEnabled;
    }

    public void setUpdatesEnabled(Boolean updatesEnabled) {
        this.mUpdatesEnabled = updatesEnabled;
    }

    public Integer getPort() {
        return mPort;
    }

    public void setPort(Integer port) {
        this.mPort = port;
    }
}
