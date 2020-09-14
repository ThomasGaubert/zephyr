package com.texasgamer.zephyr.util.layout;

import android.app.Activity;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.window.DisplayFeature;

/**
 * Provides and manages layout data based on screen size, orientation, etc.
 */
public interface ILayoutManager {

    void setCurrentActivity(@NonNull Activity activity);

    void onConfigurationChanged(@NonNull Configuration configuration);

    int getOrientation();

    DisplayFeature getDisplayFeature();

    int getSpacerWidth();

    int getWidth();

    int getHeight();

    int getPrimaryLayoutWidth();

    int getSecondaryLayoutWidth();

    boolean isPrimarySecondaryLayoutEnabled();
}
