package com.texasgamer.zephyr.model;

/**
 * Represents data sent to server for each notification.
 */
public class NotificationPayload {
    public long timestamp;
    public String packageName;
    public int id;
    public String title;
    public String message;
    public String icon;
}
