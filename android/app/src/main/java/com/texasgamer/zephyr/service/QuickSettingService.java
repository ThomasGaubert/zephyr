package com.texasgamer.zephyr.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogLevel;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import javax.inject.Inject;

/**
 * Quick setting service. Provides tile which allows user to toggle SocketService from quick settings.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class QuickSettingService extends TileService {

    private static final String LOG_TAG = "QuickSettingService";

    @Inject
    ApplicationUtils applicationUtils;
    @Inject
    ILogger logger;
    @Inject
    IPreferenceManager preferenceManager;

    public static void updateQuickSettingTile(@NonNull Context context) {
        TileService.requestListeningState(context, new ComponentName(context, QuickSettingService.class));
    }

    @Override
    public void onCreate() {
        ZephyrApplication.getApplicationComponent().inject(this);
        super.onCreate();
        logger.log(LogLevel.VERBOSE, LOG_TAG, "onCreate");
    }

    @Override
    public void onClick() {
        super.onClick();
        logger.log(LogLevel.VERBOSE, LOG_TAG, "onClick");

        Intent socketServiceIntent = new Intent(this, SocketService.class);
        boolean isJoinCodeSet = isJoinCodeSet();

        if (!isJoinCodeSet) {
            logger.log(LogLevel.WARNING, LOG_TAG, "onClick - join code not set");
            Toast.makeText(this, R.string.quick_setting_unable_to_start_no_join_code, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!SocketService.instanceCreated) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(socketServiceIntent);
            } else {
                startService(socketServiceIntent);
            }
        } else {
            stopService(socketServiceIntent);
        }
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        logger.log(LogLevel.VERBOSE, LOG_TAG, "onTileAdded");
        updateTile();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        logger.log(LogLevel.VERBOSE, LOG_TAG, "onTileRemoved");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        logger.log(LogLevel.VERBOSE, LOG_TAG, "onStopListening");
        updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        logger.log(LogLevel.VERBOSE, LOG_TAG, "onStopListening");
    }

    private void updateTile() {
        boolean isServiceRunning = preferenceManager.getBoolean(PreferenceKeys.PREF_IS_SOCKET_SERVICE_RUNNING, false);
        boolean isAvailable = isJoinCodeSet() && applicationUtils.hasNotificationAccess();
        int tileState = isAvailable ? (isServiceRunning ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE) : Tile.STATE_UNAVAILABLE;

        logger.log(LogLevel.DEBUG, LOG_TAG, "updateTile - isServiceRunning: %b, isAvailable: %b, tileState: %d",
                isServiceRunning, isAvailable, tileState);

        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(tileState);
            tile.updateTile();
        } else {
            logger.log(LogLevel.WARNING, LOG_TAG, "Unable to update tile: null");
        }
    }

    private boolean isJoinCodeSet() {
        String joinCode = preferenceManager.getString(PreferenceKeys.PREF_JOIN_CODE);
        return joinCode != null && !joinCode.isEmpty();
    }
}
