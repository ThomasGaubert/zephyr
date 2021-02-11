package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.eventbus.EventBusEvent;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Menu fragment.
 */
public class DebugFragment extends RoundedBottomSheetDialogFragment {

    @Inject
    IConfigManager configManager;
    @Inject
    IPreferenceManager preferenceManager;

    private SwitchCompat mShowAllCardsSwitch;
    private SwitchCompat mEnableMockDataSwitch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_debug, container, false);
        mShowAllCardsSwitch = root.findViewById(R.id.debug_switch_show_all_cards);
        mEnableMockDataSwitch = root.findViewById(R.id.debug_switch_enable_mock_data);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);

        mShowAllCardsSwitch.setChecked(preferenceManager.getBoolean(PreferenceKeys.PREF_DEBUG_SHOW_ALL_CARDS));
        mEnableMockDataSwitch.setChecked(preferenceManager.getBoolean(PreferenceKeys.PREF_DEBUG_ENABLE_MOCK_DATA));

        mShowAllCardsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.putBoolean(PreferenceKeys.PREF_DEBUG_SHOW_ALL_CARDS, buttonView.isChecked());
            EventBus.getDefault().post(EventBusEvent.SHELL_REFRESH_CARDS);
        });

        mEnableMockDataSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferenceManager.putBoolean(PreferenceKeys.PREF_DEBUG_ENABLE_MOCK_DATA, buttonView.isChecked()));
    }

    @Override
    protected int getInitialBottomSheetState() {
        return BottomSheetBehavior.STATE_EXPANDED;
    }

    @Override
    protected boolean shouldSkipCollapsedState() {
        return true;
    }
}
