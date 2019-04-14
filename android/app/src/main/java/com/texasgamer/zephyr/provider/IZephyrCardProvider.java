package com.texasgamer.zephyr.provider;

import android.content.Context;

import com.texasgamer.zephyr.model.ZephyrCard;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

/**
 * Zephyr card provider interface.
 */
public interface IZephyrCardProvider {
    List<ZephyrCard> getCards(@NonNull final Context context, @NonNull final FragmentManager fragmentManager);
}
