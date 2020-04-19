package com.texasgamer.zephyr.util.analytics;

/**
 * Analytics events.
 */
public class ZephyrEvent {

    /**
     * Navigation events.
     */
    public static final class Navigation {
        public static final String BASE = "nav";

        /* Hamburger menu */
        public static final String MANAGE_NOTIFICATIONS = "manage_notifications";
        public static final String HELP = "help";
        public static final String ABOUT = "about";

        /* Connection menu */
        public static final String SCAN_QR_CODE = "scan_qr_code";
        public static final String ENTER_CODE = "enter_code";

        /* About */
        public static final String GITHUB = "github";
        public static final String LICENSES = "licenses";
    }

    /**
     * Action events.
     */
    public static final class Action {
        public static final String BASE = "action";

        /* Hamburger menu */
        public static final String OPEN_HAMBURGER_MENU = "open_hamburger_menu";

        /* Connection menu */
        public static final String OPEN_CONNECTION_MENU = "open_connection_menu";
        public static final String TAP_QR_CODE = "tap_qr_code";
        public static final String TAP_ENTER_CODE = "tap_entercode";
        public static final String TAP_DISCOVERED_SERVER = "tap_discovered_server";

        /* Connection button */
        public static final String TAP_CONNECTION_BUTTON = "tap_connection_button";
        public static final String LONG_PRESS_CONNECTION_BUTTON = "long_press_connection_button";
    }

    /**
     * Event parameters.
     */
    public static final class Parameter {
        /* Connection button */
        public static final String JOIN_CODE_SET = "join_code_set";
        public static final String CONNECTED = "connected";
    }
}
