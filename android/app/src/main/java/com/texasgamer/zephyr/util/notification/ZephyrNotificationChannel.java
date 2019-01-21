package com.texasgamer.zephyr.util.notification;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

@StringDef(value = {ZephyrNotificationChannel.STATUS})
@Retention(RetentionPolicy.SOURCE)
public @interface ZephyrNotificationChannel {
    String STATUS = "status";
}
