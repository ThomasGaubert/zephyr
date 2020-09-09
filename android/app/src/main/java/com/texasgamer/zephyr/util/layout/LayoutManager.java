package com.texasgamer.zephyr.util.layout;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.window.DisplayFeature;
import androidx.window.WindowManager;

import java.lang.ref.WeakReference;
import java.util.List;

public class LayoutManager implements ILayoutManager {

    private WeakReference<Context> mContext;
    private WindowManager mWindowManager;
    private DisplayFeature mDisplayFeature;

    public LayoutManager(@NonNull Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public void setCurrentActivity(@NonNull Activity activity) {
        mContext = new WeakReference<>(activity);
        mWindowManager = new WindowManager(activity, null);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        System.out.println("HEYO - onConfigChanged");
        List<DisplayFeature> displayFeatures = mWindowManager.getWindowLayoutInfo().getDisplayFeatures();
        if (displayFeatures.size() == 1) {
            DisplayFeature displayFeature = displayFeatures.get(0);
            if (mDisplayFeature == null
                    || !mDisplayFeature.getBounds().equals(displayFeature.getBounds())
                    || mDisplayFeature.getType() != displayFeature.getType()) {
                mDisplayFeature = displayFeature;
                System.out.println("HEYO - new displayFeature : " + displayFeature);
            }
        } else {
            mDisplayFeature = null;
        }
    }

    @Override
    public int getOrientation() {
        return getContext().getResources().getConfiguration().orientation;
    }

    @Override
    public DisplayFeature getDisplayFeature() {
        return mDisplayFeature;
    }

    @Override
    public int getSpacerWidth() {
        return getDisplayFeature() != null ? getDisplayFeature().getBounds().width() : 0;
    }

    @Override
    public int getWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public int getHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public int getPrimaryLayoutWidth() {
        if (isPrimarySecondaryLayoutEnabled()) {
            return getDisplayFeature() != null ? getDisplayFeature().getBounds().left : getWidth() / 2;
        } else {
            return getWidth();
        }
    }

    @Override
    public int getSecondaryLayoutWidth() {
        return getWidth() - getPrimaryLayoutWidth();
    }

    @Override
    public boolean isPrimarySecondaryLayoutEnabled() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int widthDp = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return widthDp >= 600;
    }

    @NonNull
    private Context getContext() {
        return mContext.get();
    }
}
