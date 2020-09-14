package com.texasgamer.zephyr.util.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

public interface INavigationManager {
    void setNavControllers(@NonNull NavController mainNavController,
                           @Nullable NavController secondaryNavController);

    @NonNull
    NavController getMainNavController();

    @Nullable
    NavController getSecondaryNavController();

    @NonNull
    NavController getCurrentNavController(@NonNull Fragment fragment);
}
