package com.texasgamer.zephyr.util.theme;

import androidx.annotation.StringRes;

public interface IThemeManager {
    @Theme
    int getCurrentTheme();

    @Theme
    int getCurrentThemeSetting();

    @Theme
    int getDefaultTheme();

    @StringRes
    int getThemeNameStringResource(@Theme int theme);

    void setCurrentThemeSetting(@Theme int themeSetting);

    void setDefaultNightMode();
}
