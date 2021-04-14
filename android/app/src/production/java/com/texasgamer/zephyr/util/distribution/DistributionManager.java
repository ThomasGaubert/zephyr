package com.texasgamer.zephyr.util.distribution;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.log.ILogger;

/**
 * No-op distribution manager.
 */
public class DistributionManager implements IDistributionManager {

    public DistributionManager(@NonNull IConfigManager configManager, @NonNull ILogger logger) {
        // Intentionally empty.
    }

    @Override
    public void start() {
        // no-op
    }
}
