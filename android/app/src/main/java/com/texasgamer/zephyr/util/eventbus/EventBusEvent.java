package com.texasgamer.zephyr.util.eventbus;

import com.texasgamer.zephyr.BuildConfig;

/**
 * EventBus events.
 */
public class EventBusEvent {
    public static final String SHELL_REFRESH_CARDS = BuildConfig.APPLICATION_ID + ".shell.REFRESH_CARDS";

    private EventBusEvent() {
    }
}
