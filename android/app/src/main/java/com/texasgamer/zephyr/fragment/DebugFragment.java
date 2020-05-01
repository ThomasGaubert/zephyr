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

    @BindView(R.id.debug_switch_show_all_cards)
    SwitchCompat mShowAllCardsSwitch;

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
    }

    @OnClick(R.id.debug_switch_show_all_cards)
    public void onClickShowAllCards(@NonNull SwitchCompat view) {
        preferenceManager.putBoolean(PreferenceKeys.PREF_DEBUG_SHOW_ALL_CARDS, view.isChecked());

        EventBus.getDefault().post(EventBusEvent.SHELL_REFRESH_CARDS);
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
