package com.texasgamer.zephyr.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public Integer getApi() {
        return mApi;
    }

    public void setApi(Integer api) {
        this.mApi = api;
    }

    public String getDesktop() {
        return mDesktop;
    }

    public void setDesktop(String desktop) {
        this.mDesktop = desktop;
    }

    public String getNode() {
        return mNode;
    }

    public void setNode(String node) {
        this.mNode = node;
    }

    public String getBuildType() {
        return mBuildType;
    }

    public void setBuildType(String buildType) {
        this.mBuildType = buildType;
    }

    public ZephyrDesktopConfig getConfig() {
        return mConfig;
    }

    public void setConfig(ZephyrDesktopConfig config) {
        this.mConfig = config;
    }
}
