package com.texasgamer.zephyr.network;

import androidx.lifecycle.LiveData;

import com.texasgamer.zephyr.model.discovery.DiscoveredServer;

import java.util.List;

/**
 * Manages discovery of Zephyr servers on the local network.
 */
public interface IDiscoveryManager {
    void start();

    void stop();

    boolean isRunning();

    LiveData<List<DiscoveredServer>> getDiscoveredServers();
}
