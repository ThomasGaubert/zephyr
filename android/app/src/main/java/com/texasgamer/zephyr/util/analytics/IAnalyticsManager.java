package com.texasgamer.zephyr.util.analytics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IAnalyticsManager {

    void logEvent(@NonNull String eventId);
    void logEvent(@NonNull String eventId, @Nullable Bundle params);
}
