package com.texasgamer.zephyr.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.HandlerThread;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.model.discovery.DiscoveredServer;
import com.texasgamer.zephyr.model.discovery.DiscoveryTxtRecord;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogLevel;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.threading.ZephyrExecutors;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Manages discovery of Zephyr servers on the local network.
 */
public class DiscoveryManager implements IDiscoveryManager {

    private static final String LOG_TAG = "DiscoveryManager";
    private static final String SERVICE_TYPE = "_zephyr._tcp.";

    private final ILogger mLogger;
    private final IPreferenceManager mPreferenceManager;
    private final Map<String, DiscoveredServer> mDiscoveredServers;
    private final MutableLiveData<List<DiscoveredServer>> mDiscoveredServersLiveData;
    private boolean mRunning;

    private final NsdManager mNsdManager;
    private NsdManager.ResolveListener mResolveListener;
    private NsdManager.DiscoveryListener mDiscoveryListener;

    private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener;

    public DiscoveryManager(@NonNull Context context,
                            @NonNull ILogger logger,
                            @NonNull IPreferenceManager preferenceManager) {
        mLogger = logger;
        mPreferenceManager = preferenceManager;

        HandlerThread mPacketHandlerThread = new HandlerThread(LOG_TAG);
        mPacketHandlerThread.start();

        mDiscoveredServers = new ArrayMap<>();
        mDiscoveredServersLiveData = new MutableLiveData<>();

        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        initializeResolveListener();
        initializeDiscoveryListener();

        initializePreferenceChangeListener();
        checkIfMockDataIsEnabled();
    }

    @Override
    public synchronized void start() {
        if (!mRunning) {
            mLogger.log(LogLevel.INFO, LOG_TAG, "Starting DiscoveryManager");
            mRunning = true;
            mNsdManager.discoverServices(
                    SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
        } else {
            mLogger.log(LogLevel.WARNING, LOG_TAG, "DiscoveryManager already started");
        }
    }

    @Override
    public synchronized void stop() {
        if (mRunning) {
            mLogger.log(LogLevel.INFO, LOG_TAG, "Stopping DiscoveryManager");
            mRunning = false;
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        } else {
            mLogger.log(LogLevel.WARNING, LOG_TAG, "DiscoveryManager already stopped");
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

    private void handleDiscoveredService(@NonNull NsdServiceInfo serviceInfo) {
        DiscoveryTxtRecord discoveryTxtRecord = getTxtRecord(serviceInfo);
        @DiscoveredServer.DisabledReason
        int disabledReason = getDisabledReason(discoveryTxtRecord);
        InetAddress inetAddress = serviceInfo.getHost();
        DiscoveredServer discoveredServer = new DiscoveredServer(inetAddress.getHostAddress(),
                discoveryTxtRecord.displayName, discoveryTxtRecord.apiVersion, disabledReason);

        synchronized (mDiscoveredServers) {
            mDiscoveredServers.put(serviceInfo.getServiceName(), discoveredServer);
            ZephyrExecutors.getMainThreadExecutor().execute(() -> {
                mDiscoveredServersLiveData.setValue(new ArrayList<>(mDiscoveredServers.values()));
            });
        }
    }

    private void handleLostService(@NonNull NsdServiceInfo serviceInfo) {
        mDiscoveredServers.remove(serviceInfo.getServiceName());
        ZephyrExecutors.getMainThreadExecutor().execute(() -> {
            mDiscoveredServersLiveData.setValue(new ArrayList<>(mDiscoveredServers.values()));
        });
    }

    @DiscoveredServer.DisabledReason
    private int getDisabledReason(DiscoveryTxtRecord discoveryTxtRecord) {
        if (discoveryTxtRecord.apiVersion != Constants.ZEPHYR_API_VERSION) {
            return DiscoveredServer.DisabledReason.UNSUPPORTED_API;
        } else {
            return DiscoveredServer.DisabledReason.NOT_DISABLED;
        }
    }

    private DiscoveryTxtRecord getTxtRecord(@NonNull NsdServiceInfo serviceInfo) {
        DiscoveryTxtRecord discoveryTxtRecord = new DiscoveryTxtRecord();
        discoveryTxtRecord.apiVersion = Integer.parseInt(new String(serviceInfo.getAttributes().get("apiVersion"), StandardCharsets.UTF_8));
        discoveryTxtRecord.displayName = new String(serviceInfo.getAttributes().get("displayName"), StandardCharsets.UTF_8);
        return discoveryTxtRecord;
    }

    private void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails. Use the error code to debug.
                mLogger.log(LogLevel.ERROR, LOG_TAG, "onResolveFailed: " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                mLogger.log(LogLevel.VERBOSE, LOG_TAG, "onServiceResolved: " + serviceInfo);
                handleDiscoveredService(serviceInfo);
            }
        };
    }

    private void initializeDiscoveryListener() {
        // Instantiate a new DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                mLogger.log(LogLevel.VERBOSE, LOG_TAG, "onDiscoveryStarted");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found! Do something with it.
                mLogger.log(LogLevel.VERBOSE, LOG_TAG, "onServiceFound: " + service);
                mNsdManager.resolveService(service, mResolveListener);
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                mLogger.log(LogLevel.ERROR, LOG_TAG, "onServiceLost: " + service);
                handleLostService(service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                mLogger.log(LogLevel.VERBOSE, LOG_TAG, "onDiscoveryStopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                mLogger.log(LogLevel.ERROR, LOG_TAG, "onStartDiscoveryFailed: " + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                mLogger.log(LogLevel.ERROR, LOG_TAG, "onStopDiscoveryFailed: " + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    private void initializePreferenceChangeListener() {
        mPreferenceChangeListener = (sharedPreferences, key) -> {
            if (PreferenceKeys.PREF_DEBUG_ENABLE_MOCK_DATA.equals(key)) {
                checkIfMockDataIsEnabled();
            }
        };
        mPreferenceManager.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);
    }

    private void checkIfMockDataIsEnabled() {
        if (mPreferenceManager.getBoolean(PreferenceKeys.PREF_DEBUG_ENABLE_MOCK_DATA)) {
            populateMockData();
        } else {
            removeMockData();
        }
    }

    @SuppressWarnings("PMD.AvoidUsingHardCodedIP")
    private void populateMockData() {
        synchronized (mDiscoveredServers) {
            mDiscoveredServers.put("MOCK_SERVER", new DiscoveredServer("192.168.0.18",
                    "Desktop", Constants.ZEPHYR_API_VERSION,
                    DiscoveredServer.DisabledReason.NOT_DISABLED));
            ZephyrExecutors.getMainThreadExecutor().execute(() -> {
                mDiscoveredServersLiveData.setValue(new ArrayList<>(mDiscoveredServers.values()));
            });
        }
    }

    private void removeMockData() {
        synchronized (mDiscoveredServers) {
            mDiscoveredServers.remove("MOCK_SERVER");
            ZephyrExecutors.getMainThreadExecutor().execute(() -> {
                mDiscoveredServersLiveData.setValue(new ArrayList<>(mDiscoveredServers.values()));
            });
        }
    }
}
