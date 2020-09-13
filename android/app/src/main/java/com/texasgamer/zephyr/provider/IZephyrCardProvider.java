package com.texasgamer.zephyr.provider;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import com.texasgamer.zephyr.model.ZephyrCard;

import java.util.List;

/**
 * Zephyr card provider interface.
 */
public interface IZephyrCardProvider {
    List<ZephyrCard> getCards(@NonNull final Context context, @NonNull NavController navController);
}
