package com.texasgamer.zephyr.util.theme;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDelegate;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

/**
 * Theme manager.
 */
public class ThemeManager implements IThemeManager {

    private Context mContext;
    private IPreferenceManager mPreferenceManager;

    public ThemeManager(@NonNull Context context, @NonNull IPreferenceManager preferenceManager) {
        mContext = context;
        mPreferenceManager = preferenceManager;
    }

    @Theme
    @Override
    public int getCurrentTheme() {
        int currentNightMode = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                return Theme.LIGHT;
            case Configuration.UI_MODE_NIGHT_YES:
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
            default:
                return Theme.DARK;
        }
    }

    @Theme
    @Override
    public int getCurrentThemeSetting() {
        return mPreferenceManager.getInt(PreferenceKeys.PREF_THEME, getDefaultTheme());
    }

    @Theme
    @Override
    public int getDefaultTheme() {
        return Theme.DARK;
    }

    @StringRes
    @Override
    public int getThemeNameStringResource(@Theme int theme) {
        switch (theme) {
            case Theme.SYSTEM_DEFAULT:
                return R.string.menu_theme_system;
            case Theme.LIGHT:
                return R.string.menu_theme_light;
            case Theme.DARK:
                return R.string.menu_theme_dark;
            default:
                return getThemeNameStringResource(getDefaultTheme());
        }
    }

    @Override
    public void setCurrentThemeSetting(@Theme int themeSetting) {
        mPreferenceManager.putInt(PreferenceKeys.PREF_THEME, themeSetting);
        setDefaultNightMode(themeSetting);
    }

    @Override
    public void setDefaultNightMode() {
        setDefaultNightMode(getCurrentThemeSetting());
    }

    private void setDefaultNightMode(@Theme int theme) {
        int nightMode;
        switch (theme) {
            case Theme.SYSTEM_DEFAULT:
                nightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
            case Theme.LIGHT:
                nightMode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case Theme.DARK:
                nightMode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            default:
                setDefaultNightMode(getDefaultTheme());
                return;
        }

        AppCompatDelegate.setDefaultNightMode(nightMode);
    }
}
