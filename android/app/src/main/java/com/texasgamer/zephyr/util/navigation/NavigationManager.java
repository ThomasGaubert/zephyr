package com.texasgamer.zephyr.util.navigation;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.texasgamer.zephyr.util.layout.ILayoutManager;

/**
 * Manages navigation operations.
 */
public class NavigationManager implements INavigationManager {

    private ILayoutManager mLayoutManager;
    private NavController mMainNavController;
    private NavController mSecondaryNavController;

    public NavigationManager(@NonNull ILayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    @Override
    public void setNavControllers(@NonNull NavController mainNavController,
                                  @Nullable NavController secondaryNavController) {
        mMainNavController = mainNavController;
        mSecondaryNavController = secondaryNavController;
    }

    @NonNull
    @Override
    public NavController getMainNavController() {
        return mMainNavController;
    }

    @Nullable
    @Override
    public NavController getSecondaryNavController() {
        return mSecondaryNavController;
    }

    @NonNull
    @Override
    public NavController getCurrentNavController(@NonNull Fragment fragment) {
        return Navigation.findNavController(fragment.requireView());
    }

    @Override
    public void navigate(@IdRes int navDestination) {
        getMainNavController().navigate(navDestination);
    }

    @Override
    public void navigate(@IdRes int mainNavDestination, @IdRes int secondaryNavDestination) {
        if (getSecondaryNavController() != null
                && mLayoutManager.isPrimarySecondaryLayoutEnabled()) {
            getSecondaryNavController().navigate(secondaryNavDestination);
        } else {
            getMainNavController().navigate(mainNavDestination);
        }
    }
}
