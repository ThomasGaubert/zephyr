package com.texasgamer.zephyr.provider;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.model.ConnectionStatus;
import com.texasgamer.zephyr.model.ZephyrCard;
import com.texasgamer.zephyr.model.ZephyrCardType;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.navigation.INavigationManager;
import com.texasgamer.zephyr.util.navigation.NavigationUtils;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import java.util.ArrayList;
import java.util.List;

/**
 * ZephyrCard provider.
 */
public class ZephyrCardProvider implements IZephyrCardProvider {

    private ApplicationUtils mApplicationUtils;
    private IConfigManager mConfigManager;
    private IPreferenceManager mPreferenceManager;

    public ZephyrCardProvider(@NonNull ApplicationUtils applicationUtils,
                              @NonNull IConfigManager configManager,
                              @NonNull IPreferenceManager preferenceManager) {
        mApplicationUtils = applicationUtils;
        mConfigManager = configManager;
        mPreferenceManager = preferenceManager;
    }

    public List<ZephyrCard> getCards(@NonNull Context context,
                                     @NonNull INavigationManager navigationManager) {
        List<ZephyrCard> cards = new ArrayList<>();

        @ConnectionStatus
        int connectionStatus = mPreferenceManager.getInt(PreferenceKeys.PREF_CONNECTION_STATUS);
        boolean forceShowCards = mPreferenceManager.getBoolean(PreferenceKeys.PREF_DEBUG_SHOW_ALL_CARDS) && mConfigManager.isDebug();
        boolean everConnectedToServer = mPreferenceManager.getBoolean(PreferenceKeys.PREF_EVER_CONNECTED_TO_SERVER);
        boolean completedFre = true;

        // Unsupported device: low RAM
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null && activityManager.isLowRamDevice()) {
            ZephyrCard lowRamCard = new ZephyrCard(ZephyrCardType.ERROR, R.string.card_low_ram_title, R.string.card_low_ram_body);
            cards.add(lowRamCard);
            return cards;
        }

        // Notification access
        if (forceShowCards || !mApplicationUtils.hasNotificationAccess()) {
            ZephyrCard notificationAccessCard = new ZephyrCard(ZephyrCardType.ERROR, R.string.card_notification_access_title, R.string.card_notification_access_body);
            notificationAccessCard.setOnClickListener(v -> NavigationUtils.openNotificationAccessSettings(v.getContext()));
            cards.add(notificationAccessCard);
            completedFre = false;
        }

        // Beta
        if (forceShowCards || mConfigManager.isBeta()) {
            ZephyrCard zephyrV2Card = new ZephyrCard(ZephyrCardType.INFO, R.string.card_zephyr_beta_title, R.string.card_zephyr_beta_body);
            zephyrV2Card.setOnClickListener(v -> {
                NavigationUtils.openUrl(context, Constants.ZEPHYR_FEEDBACK_URL);
            });
            cards.add(zephyrV2Card);
        }

        // Zephyr 2.0
        if (forceShowCards || (mApplicationUtils.didUpgradeFromV1()
                && !mPreferenceManager.getBoolean(PreferenceKeys.PREF_SEEN_V2_PROMO))) {
            ZephyrCard zephyrV2Card = new ZephyrCard(ZephyrCardType.INFO, R.string.card_zephyr_v2_title, R.string.card_zephyr_v2_body);
            zephyrV2Card.setOnClickListener(v -> {
                navigationManager.navigate(R.id.action_fragment_main_to_fragment_whats_new);
            });
            cards.add(zephyrV2Card);
        }

        // Onboarding: Download desktop client
        if (forceShowCards || (mApplicationUtils.hasNotificationAccess()
                && !everConnectedToServer
                && connectionStatus != ConnectionStatus.CONNECTED)
                && mConfigManager.isProduction()) {
            ZephyrCard manageNotificationsCard = new ZephyrCard(ZephyrCardType.INFO, R.string.card_download_desktop_title, R.string.card_download_desktop_body);
            manageNotificationsCard.setOnClickListener(v -> {
                NavigationUtils.openUrl(context, Constants.ZEPHYR_STEAM_URL);
            });
            cards.add(manageNotificationsCard);
            completedFre = false;
        }

        // Help: Connection issues
        boolean showConnectionHelpCard = (mPreferenceManager.getBoolean(PreferenceKeys.PREF_SHOW_CONNECTION_HELP_CARD)
                || connectionStatus == ConnectionStatus.SERVER_NOT_FOUND)
                && connectionStatus != ConnectionStatus.CONNECTED;
        if (forceShowCards || (mApplicationUtils.hasNotificationAccess()
                && !everConnectedToServer
                && showConnectionHelpCard)) {
            mPreferenceManager.putBoolean(PreferenceKeys.PREF_SHOW_CONNECTION_HELP_CARD, true);
            ZephyrCard manageNotificationsCard = new ZephyrCard(ZephyrCardType.WARNING, R.string.card_connection_issues_title, R.string.card_connection_issues_body);
            manageNotificationsCard.setOnClickListener(v -> {
                NavigationUtils.openUrl(context, Constants.ZEPHYR_CONNECTION_HELP_URL);
            });
            cards.add(manageNotificationsCard);
        } else {
            mPreferenceManager.putBoolean(PreferenceKeys.PREF_SHOW_CONNECTION_HELP_CARD, false);
        }

        // Onboarding: Configure notifications
        if (forceShowCards || (mApplicationUtils.hasNotificationAccess()
                && !mApplicationUtils.didUpgradeFromV1()
                && !mPreferenceManager.getBoolean(PreferenceKeys.PREF_SEEN_MANAGE_NOTIFICATIONS))) {
            ZephyrCard manageNotificationsCard = new ZephyrCard(ZephyrCardType.INFO, R.string.card_manage_notifications_title, R.string.card_manage_notifications_body);
            manageNotificationsCard.setOnClickListener(v -> {
                navigationManager.navigate(R.id.action_fragment_main_to_fragment_notifications, R.id.fragment_notifications);
            });
            cards.add(manageNotificationsCard);
            completedFre = false;
        }

        mPreferenceManager.putBoolean(PreferenceKeys.PREF_COMPLETED_FRE, completedFre);

        return cards;
    }
}
