package com.texasgamer.zephyr.util.eventbus;

import com.texasgamer.zephyr.BuildConfig;

/**
 * EventBus events.
 */
public final class EventBusEvent {
    public static final String SHELL_REFRESH_CARDS = BuildConfig.APPLICATION_ID + ".shell.REFRESH_CARDS";
    public static final String SERVICE_NOTIFICATION_STARTED = BuildConfig.APPLICATION_ID + ".service.notification.STARTED";

    private EventBusEvent() {
    }
}
