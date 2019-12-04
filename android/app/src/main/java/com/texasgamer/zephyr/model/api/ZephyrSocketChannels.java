package com.texasgamer.zephyr.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Zephyr socket channels.
 */
public class ZephyrSocketChannels {

    @Expose
    @SerializedName("actions")
    private Actions mActions;
    @Expose
    @SerializedName("events")
    private Events mEvents;

    public Actions getActions() {
        return mActions;
    }

    public Events getEvents() {
        return mEvents;
    }

    /**
     * Actions
     */
    public static class Actions {
        @Expose
        @SerializedName("postNotification")
        private String mPostNotification;
        @Expose
        @SerializedName("dismissNotification")
        private String mDismissNotification;
        @Expose
        @SerializedName("disconnect")
        private String mDisconnect;

        public String getPostNotification() {
            return mPostNotification;
        }

        public String getDismissNotification() {
            return mDismissNotification;
        }

        public String getDisconnect() {
            return mDisconnect;
        }
    }

    /**
     * Events
     */
    public static class Events {
        @Expose
        @SerializedName("notificationPosted")
        private String mNotificationPosted;
        @Expose
        @SerializedName("notificationDismissed")
        private String mNotificationDismissed;

        public String getNotificationPosted() {
            return mNotificationPosted;
        }

        public String getNotificationDismissed() {
            return mNotificationDismissed;
        }
    }
}
