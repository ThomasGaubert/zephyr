package com.texasgamer.zephyr.model;

/**
 * Represents data sent to server for each notification.
 */
public class NotificationPayload {
    public String packageName;
    public int id;
    public long timestamp;
    public String title;
    public String message;
    public String icon;
}
