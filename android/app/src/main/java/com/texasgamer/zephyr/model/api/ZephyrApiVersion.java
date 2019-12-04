package com.texasgamer.zephyr.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Zephyr API version.
 */
public class ZephyrApiVersion {

    @Expose
    @SerializedName("api")
    private Integer mApi;
    @Expose
    @SerializedName("desktop")
    private String mDesktop;
    @Expose
    @SerializedName("node")
    private String mNode;
    @Expose
    @SerializedName("buildType")
    private String mBuildType;
    @Expose
    @SerializedName("config")
    private ZephyrDesktopConfig mConfig;
    @Expose
    @SerializedName("features")
    private List<String> mFeatures;
    @Expose
    @SerializedName("socketChannels")
    private ZephyrSocketChannels mSocketChannels;

    public Integer getApi() {
        return mApi;
    }

    public String getDesktop() {
        return mDesktop;
    }

    public String getNode() {
        return mNode;
    }

    public String getBuildType() {
        return mBuildType;
    }

    public ZephyrDesktopConfig getConfig() {
        return mConfig;
    }

    public List<String> getFeatures() {
        return mFeatures;
    }

    public ZephyrSocketChannels getSocketChannels() {
        return mSocketChannels;
    }
}
