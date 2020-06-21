package com.texasgamer.zephyr.model;

/**
 * Notification preference.
 */
public interface INotificationPreference {

    String getPackageName();

    boolean isEnabled();
}
