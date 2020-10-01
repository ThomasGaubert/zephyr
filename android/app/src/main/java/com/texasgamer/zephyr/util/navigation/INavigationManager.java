package com.texasgamer.zephyr.util.navigation;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

/**
 * Manages navigation operations.
 */
public interface INavigationManager {
    void setNavControllers(@NonNull NavController mainNavController,
                           @Nullable NavController secondaryNavController);

    @NonNull
    NavController getMainNavController();

    @Nullable
    NavController getSecondaryNavController();

    @NonNull
    NavController getCurrentNavController(@NonNull View view);

    void navigate(@IdRes int navDestination);

    void navigate(@IdRes int mainNavDestination, @IdRes int secondaryNavDestination);
}
