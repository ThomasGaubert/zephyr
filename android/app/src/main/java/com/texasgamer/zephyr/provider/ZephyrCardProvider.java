package com.texasgamer.zephyr.provider;

import android.content.Intent;

import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.fragment.BaseFragment;
import com.texasgamer.zephyr.fragment.WhatsNewFragment;
import com.texasgamer.zephyr.model.ZephyrCard;
import com.texasgamer.zephyr.model.ZephyrCardType;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * ZephyrCard provider.
 */
public final class ZephyrCardProvider {

    private ZephyrCardProvider() {
    }

    public static List<ZephyrCard> getCards(@NonNull final BaseFragment fragment) {
        List<ZephyrCard> cards = new ArrayList<>();

        if (!ZephyrApplication.getApplicationComponent().applicationUtilities().hasNotificationAccess()) {
            ZephyrCard notificationAccessCard = new ZephyrCard(ZephyrCardType.ERROR, R.string.card_notification_access_title, R.string.card_notification_access_body);
            notificationAccessCard.setOnClickListener(v -> {
                Intent intent = new Intent(Constants.ANDROID_NOTIFICATION_LISTENER_SETTINGS);
                v.getContext().startActivity(intent);
            });
            cards.add(notificationAccessCard);
        }

        if (ZephyrApplication.getApplicationComponent().applicationUtilities().isUpgradeFromV1()) {
            ZephyrCard zephyrV2Card = new ZephyrCard(ZephyrCardType.INFO, R.string.card_zephyr_v2_title, R.string.card_zephyr_v2_body);
            zephyrV2Card.setOnClickListener(v -> {
                WhatsNewFragment whatsNewFragment = new WhatsNewFragment();
                whatsNewFragment.show(fragment.getChildFragmentManager(), whatsNewFragment.getTag());
            });
            cards.add(zephyrV2Card);
        }

        return cards;
    }
}
