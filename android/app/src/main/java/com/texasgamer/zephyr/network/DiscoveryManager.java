package com.texasgamer.zephyr.network;

import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.model.discovery.DiscoveredServer;
import com.texasgamer.zephyr.model.discovery.DiscoveryPacket;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogLevel;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.threading.ZephyrExecutors;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Manages discovery of Zephyr servers on the local network.
 */
public class DiscoveryManager implements IDiscoveryManager {

    private static final String LOG_TAG = "DiscoveryManager";

    private Gson mGson;
    private ILogger mLogger;
    private IPreferenceManager mPreferenceManager;
    private Handler mPacketHandler;
    private Runnable mDiscoveryRunnable;
    private boolean mRunning;
    private MulticastSocket mMulticastSocket;
    private byte[] mBuffer = new byte[256];
    private final Map<String, DiscoveredServer> mDiscoveredServers;
    private MutableLiveData<List<DiscoveredServer>> mDiscoveredServersLiveData;

    public DiscoveryManager(@NonNull Gson gson,
                            @NonNull ILogger logger,
                            @NonNull IPreferenceManager preferenceManager) {
        mGson = gson;
        mLogger = logger;
        mPreferenceManager = preferenceManager;

        HandlerThread mPacketHandlerThread = new HandlerThread(LOG_TAG);
        mPacketHandlerThread.start();
        mPacketHandler = new Handler(mPacketHandlerThread.getLooper());
        mDiscoveryRunnable = getDiscoveryRunnable();

        mDiscoveredServers = new ArrayMap<>();
        mDiscoveredServersLiveData = new MutableLiveData<>();
    }

    @Override
    public synchronized void start() {
        if (!mRunning) {
            mRunning = true;
            mPacketHandler.post(mDiscoveryRunnable);
        }
    }

    @Override
    public synchronized void stop() {
        if (mRunning) {
            mRunning = false;
            mPacketHandler.removeCallbacks(mDiscoveryRunnable);
        }
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public LiveData<List<DiscoveredServer>> getDiscoveredServers() {
        return mDiscoveredServersLiveData;
    }

    private Runnable getDiscoveryRunnable() {
        return () -> {
            try {
                InetAddress group = InetAddress.getByName(Constants.DISCOVERY_BROADCAST_ADDRESS);
                mMulticastSocket = new MulticastSocket(Constants.DISCOVERY_BROADCAST_PORT);
                mMulticastSocket.joinGroup(group);
                mMulticastSocket.setSoTimeout(Constants.DISCOVERY_BROADCAST_TIMEOUT_IN_MS);

                while (mRunning) {
                    DatagramPacket packet = new DatagramPacket(mBuffer, mBuffer.length);
                    try {
                        mMulticastSocket.receive(packet);
                        handlePacket(packet);
                    } catch (SocketTimeoutException e) {
                        cleanupDiscoveredServers();
                    }
                }

                mMulticastSocket.leaveGroup(group);
                mMulticastSocket.close();
            } catch (IOException e) {
                mLogger.log(LogLevel.ERROR, LOG_TAG, e, "Error encountered!");
            }
        };
    }

    private void handlePacket(@NonNull DatagramPacket packet) {
        String received = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
        DiscoveryPacket discoveryPacket = mGson.fromJson(received, DiscoveryPacket.class);
        if (discoveryPacket == null) {
            return;
        }

        @DiscoveredServer.DisabledReason
        int disabledReason = getDisabledReason(discoveryPacket);
        InetAddress inetAddress = packet.getAddress();
        DiscoveredServer discoveredServer = new DiscoveredServer(inetAddress.getHostAddress(),
                inetAddress.getHostName(), discoveryPacket.getApiVersion(),
                discoveryPacket.getTimestamp(), disabledReason);

        synchronized (mDiscoveredServers) {
            mDiscoveredServers.put(discoveredServer.getIpAddress(), discoveredServer);
            ZephyrExecutors.getMainThreadExecutor().execute(() -> {
                mDiscoveredServersLiveData.setValue(new ArrayList<>(mDiscoveredServers.values()));
            });
        }

        cleanupDiscoveredServers();
    }

    private void cleanupDiscoveredServers() {
        if (mPreferenceManager.getBoolean(PreferenceKeys.PREF_DEBUG_ENABLE_MOCK_DATA)) {
            populateMockData();
        }

        synchronized (mDiscoveredServers) {
            for (Map.Entry<String, DiscoveredServer> entry : mDiscoveredServers.entrySet()) {
                long timeSinceLastPacket = System.currentTimeMillis() - entry.getValue().getTimestamp();
                if (timeSinceLastPacket > Constants.DISCOVERY_BROADCAST_INTERVAL_IN_MS) { // 3 seconds
                    mLogger.log(LogLevel.DEBUG, LOG_TAG, "Removing server: last seen " + timeSinceLastPacket + "ms ago");
                    mDiscoveredServers.remove(entry.getKey());
                    ZephyrExecutors.getMainThreadExecutor().execute(() -> {
                        mDiscoveredServersLiveData.setValue(new ArrayList<>(mDiscoveredServers.values()));
                    });
                }
            }
        }
    }

    @DiscoveredServer.DisabledReason
    private int getDisabledReason(DiscoveryPacket discoveryPacket) {
        if (discoveryPacket.getApiVersion() != Constants.ZEPHYR_API_VERSION) {
            return DiscoveredServer.DisabledReason.UNSUPPORTED_API;
        } else {
            return DiscoveredServer.DisabledReason.NOT_DISABLED;
        }
    }

    @SuppressWarnings("PMD.AvoidUsingHardCodedIP")
    private void populateMockData() {
        synchronized (mDiscoveredServers) {
            mDiscoveredServers.put("0", new DiscoveredServer("192.168.0.18",
                    "Desktop", Constants.ZEPHYR_API_VERSION,
                    System.currentTimeMillis(), DiscoveredServer.DisabledReason.NOT_DISABLED));
            ZephyrExecutors.getMainThreadExecutor().execute(() -> {
                mDiscoveredServersLiveData.setValue(new ArrayList<>(mDiscoveredServers.values()));
            });
        }
    }
}
