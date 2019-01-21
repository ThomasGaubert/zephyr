package com.texasgamer.zephyr.provider;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.model.ZephyrCard;
import com.texasgamer.zephyr.model.ZephyrCardType;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class ZephyrCardProvider {

    public static List<ZephyrCard> getCards(@NonNull Context context) {
        List<ZephyrCard> cards = new ArrayList<>();

        if (!ZephyrApplication.getApplicationComponent().applicationUtilities().hasNotificationAccess()) {
            ZephyrCard notificationAccessCard = new ZephyrCard(ZephyrCardType.ERROR, R.string.card_notification_access_title, R.string.card_notification_access_body);
            notificationAccessCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Constants.ANDROID_NOTIFICATION_LISTENER_SETTINGS);
                    v.getContext().startActivity(intent);
                }
            });
            cards.add(notificationAccessCard);
        }

        if (ZephyrApplication.getApplicationComponent().applicationUtilities().isUpgradeFromV1()) {
            cards.add(new ZephyrCard(ZephyrCardType.INFO, R.string.card_zephyr_v2_title, R.string.card_zephyr_v2_body));
        }

        cards.add(new ZephyrCard(ZephyrCardType.SUCCESS, R.string.card_connected_title, R.string.card_connected_body));

        cards.add(new ZephyrCard(ZephyrCardType.WARNING, R.string.card_disconnected_title, R.string.card_disconnected_body));

        return cards;
    }
}
