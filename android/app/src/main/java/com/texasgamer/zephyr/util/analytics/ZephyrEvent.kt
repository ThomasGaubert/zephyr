package com.texasgamer.zephyr.util.analytics

/**
 * Analytics events.
 */
class ZephyrEvent {
    /**
     * Navigation events.
     */
    object Navigation {
        const val BASE = "nav"

        /* Hamburger menu */
        const val MANAGE_NOTIFICATIONS = "manage_notifications"
        const val HELP = "help"
        const val ABOUT = "about"

        /* Connection menu */
        const val SCAN_QR_CODE = "scan_qr_code"
        const val ENTER_CODE = "enter_code"

        /* About */
        const val GITHUB = "github"
        const val LICENSES = "licenses"
    }

    /**
     * Action events.
     */
    object Action {
        const val BASE = "action"

        /* Hamburger menu */
        const val OPEN_HAMBURGER_MENU = "open_hamburger_menu"

        /* Connection menu */
        const val OPEN_CONNECTION_MENU = "open_connection_menu"
        const val TAP_QR_CODE = "tap_qr_code"
        const val TAP_ENTER_CODE = "tap_entercode"
        const val TAP_DISCOVERED_SERVER = "tap_discovered_server"

        /* Connection button */
        const val TAP_CONNECTION_BUTTON = "tap_connection_button"
        const val LONG_PRESS_CONNECTION_BUTTON = "long_press_connection_button"
    }

    /**
     * Event parameters.
     */
    object Parameter {
        /* Connection button */
        const val JOIN_CODE_SET = "join_code_set"
        const val CONNECTED = "connected"
    }
}