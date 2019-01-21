package com.texasgamer.zephyr.util.notification;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef(value = {ZephyrNotificationId.SOCKET_SERVICE_STATUS})
@Retention(RetentionPolicy.SOURCE)
public @interface ZephyrNotificationId {
    int SOCKET_SERVICE_STATUS = 1;
}
