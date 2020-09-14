package com.texasgamer.zephyr.provider;

import android.content.Context;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.model.ZephyrCard;
import com.texasgamer.zephyr.util.layout.ILayoutManager;
import com.texasgamer.zephyr.util.navigation.INavigationManager;

import java.util.List;

/**
 * Zephyr card provider interface.
 */
public interface IZephyrCardProvider {
    List<ZephyrCard> getCards(@NonNull final Context context,
                              @NonNull ILayoutManager layoutManager,
                              @NonNull INavigationManager navigationManager);
}
