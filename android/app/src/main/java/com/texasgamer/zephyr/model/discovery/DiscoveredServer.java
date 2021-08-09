package com.texasgamer.zephyr.model.discovery;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents discovered Zephyr server running on local network.
 */
public class DiscoveredServer {

    private final String mIpAddress;
    private final String mHostName;
    private final int mApiVersion;
    private final boolean mDisabled;
    @DisabledReason
    private final int mDisabledReason;

    public DiscoveredServer(@NonNull String ipAddress,
                            @NonNull String hostName,
                            int apiVersion,
                            @DisabledReason int disabledReason) {
        mIpAddress = ipAddress;
        mHostName = hostName;
        mApiVersion = apiVersion;
        mDisabled = disabledReason != DisabledReason.NOT_DISABLED;
        mDisabledReason = disabledReason;
    }

    public String getIpAddress() {
        return mIpAddress;
    }

    public String getHostName() {
        return mHostName;
    }

    public int getApiVersion() {
        return mApiVersion;
    }

    public boolean isDisabled() {
        return mDisabled;
    }

    public int getDisabledReason() {
        return mDisabledReason;
    }

    @IntDef({DisabledReason.NOT_DISABLED, DisabledReason.UNKNOWN, DisabledReason.UNSUPPORTED_API})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DisabledReason {
        int NOT_DISABLED = -1;
        int UNKNOWN = 0;
        int UNSUPPORTED_API = 1;
    }
}
