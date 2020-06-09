package com.texasgamer.zephyr.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.eventbus.EventBusEvent;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.theme.IThemeManager;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Menu fragment.
 */
public class DebugFragment extends RoundedBottomSheetDialogFragment {

    @Inject
    IConfigManager configManager;
    @Inject
    IPreferenceManager preferenceManager;
    @Inject
    IThemeManager themeManager;

    @BindView(R.id.debug_switch_show_all_cards)
    SwitchCompat mShowAllCardsSwitch;
    @BindView(R.id.debug_theme)
    TextView mTheme;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_debug, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);

        mShowAllCardsSwitch.setChecked(preferenceManager.getBoolean(PreferenceKeys.PREF_DEBUG_SHOW_ALL_CARDS));
        mShowAllCardsSwitch.setChecked(preferenceManager.getBoolean(PreferenceKeys.PREF_DEBUG_ENABLE_MOCK_DATA));
        updateThemeText();
    }

    @OnClick(R.id.debug_switch_show_all_cards)
    public void onClickShowAllCards(@NonNull SwitchCompat view) {
        preferenceManager.putBoolean(PreferenceKeys.PREF_DEBUG_SHOW_ALL_CARDS, view.isChecked());
        EventBus.getDefault().post(EventBusEvent.SHELL_REFRESH_CARDS);
    }

    @OnClick(R.id.debug_switch_enable_mock_data)
    public void onClickEnableMockData(@NonNull SwitchCompat view) {
        preferenceManager.putBoolean(PreferenceKeys.PREF_DEBUG_ENABLE_MOCK_DATA, view.isChecked());
    }

    @OnClick(R.id.debug_theme)
    public void onClickTheme(@NonNull TextView view) {
        if (getActivity() == null) {
            return;
        }

        Resources resources = getActivity().getResources();
        final CharSequence[] themeChoices = {
                resources.getString(R.string.menu_debug_theme_system),
                resources.getString(R.string.menu_debug_theme_light),
                resources.getString(R.string.menu_debug_theme_dark)};

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(getActivity())
                .setTitle(R.string.menu_debug_theme_title)
                .setSingleChoiceItems(themeChoices, themeManager.getCurrentThemeSetting(), (dialog, which) -> {
                    themeManager.setCurrentThemeSetting(which);
                    updateThemeText();
                    dialog.dismiss();
                }).create();
        alertDialog.show();
    }

    @Override
    protected int getInitialBottomSheetState() {
        return BottomSheetBehavior.STATE_EXPANDED;
    }

    @Override
    protected boolean shouldSkipCollapsedState() {
        return true;
    }

    private void updateThemeText() {
        mTheme.setText(getString(R.string.menu_debug_theme_setting, getString(themeManager.getThemeNameStringResource(themeManager.getCurrentThemeSetting()))));
    }
}
