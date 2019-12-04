package com.texasgamer.zephyr.provider;

import android.content.Context;
import android.content.Intent;

import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.activity.NotificationActivity;
import com.texasgamer.zephyr.fragment.WhatsNewFragment;
import com.texasgamer.zephyr.model.ZephyrCard;
import com.texasgamer.zephyr.model.ZephyrCardType;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.NavigationUtils;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

/**
 * ZephyrCard provider.
 */
public class ZephyrCardProvider implements IZephyrCardProvider {

    private ApplicationUtils mApplicationUtils;
    private IConfigManager mConfigManager;
    private IPreferenceManager mPreferenceManager;

    public ZephyrCardProvider(@NonNull ApplicationUtils applicationUtils, @NonNull IConfigManager configManager, @NonNull IPreferenceManager preferenceManager) {
        mApplicationUtils = applicationUtils;
        mConfigManager = configManager;
        mPreferenceManager = preferenceManager;
    }

    public List<ZephyrCard> getCards(@NonNull Context context, @NonNull final FragmentManager fragmentManager) {
        List<ZephyrCard> cards = new ArrayList<>();

        boolean forceShowCards = mPreferenceManager.getBoolean(PreferenceKeys.PREF_DEBUG_SHOW_ALL_CARDS) && mConfigManager.isDebug();

        // Notification access
        if (forceShowCards || !mApplicationUtils.hasNotificationAccess()) {
            ZephyrCard notificationAccessCard = new ZephyrCard(ZephyrCardType.ERROR, R.string.card_notification_access_title, R.string.card_notification_access_body);
            notificationAccessCard.setOnClickListener(v -> {
                Intent intent = new Intent(Constants.ANDROID_NOTIFICATION_LISTENER_SETTINGS);
                v.getContext().startActivity(intent);
            });
            cards.add(notificationAccessCard);
        }

        // Beta
        if (forceShowCards || mConfigManager.isBeta()) {
            ZephyrCard zephyrV2Card = new ZephyrCard(ZephyrCardType.INFO, R.string.card_zephyr_beta_title, R.string.card_zephyr_beta_body);
            zephyrV2Card.setOnClickListener(v -> {
                NavigationUtils.openUrl(context, Constants.ZEPHYR_FEEDBACK_URL);
            });
            cards.add(zephyrV2Card);
        }

        // Onboarding: Configure notifications
        if (forceShowCards || (mApplicationUtils.hasNotificationAccess()
                && !mApplicationUtils.didUpgradeFromV1()
                && !mPreferenceManager.getBoolean(PreferenceKeys.PREF_SEEN_MANAGE_NOTIFICATIONS))) {
            ZephyrCard manageNotificationsCard = new ZephyrCard(ZephyrCardType.INFO, R.string.card_manage_notifications_title, R.string.card_manage_notifications_body);
            manageNotificationsCard.setOnClickListener(v -> {
                NavigationUtils.openActivity(v.getContext(), NotificationActivity.class);
            });
            cards.add(manageNotificationsCard);
        }

        // Zephyr V2
        if (forceShowCards || mApplicationUtils.didUpgradeFromV1()) {
            ZephyrCard zephyrV2Card = new ZephyrCard(ZephyrCardType.INFO, R.string.card_zephyr_v2_title, R.string.card_zephyr_v2_body);
            zephyrV2Card.setOnClickListener(v -> {
                WhatsNewFragment whatsNewFragment = new WhatsNewFragment();
                whatsNewFragment.show(fragmentManager, whatsNewFragment.getTag());
            });
            cards.add(zephyrV2Card);
        }

        return cards;
    }
}
