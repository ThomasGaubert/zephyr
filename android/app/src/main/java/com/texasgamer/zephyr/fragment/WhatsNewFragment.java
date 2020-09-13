package com.texasgamer.zephyr.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.adapter.WhatsNewAdapter;
import com.texasgamer.zephyr.model.WhatsNewItem;
import com.texasgamer.zephyr.util.navigation.NavigationUtils;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * What's new fragment.
 */
public class WhatsNewFragment extends RoundedBottomSheetDialogFragment {

    public static final String LOG_TAG = "ConnectFragment";

    @Inject
    IPreferenceManager preferenceManager;

    @BindView(R.id.whats_new_subtitle)
    TextView mWhatsNewSubtitle;
    @BindView(R.id.whats_new_recyclerview)
    RecyclerView mWhatsNewRecyclerView;

    private WhatsNewAdapter mWhatsNewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_whats_new, container, false);
        ButterKnife.bind(this, root);

        mWhatsNewSubtitle.setText(BuildConfig.VERSION_NAME);

        mWhatsNewRecyclerView.setHasFixedSize(true);
        mWhatsNewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mWhatsNewAdapter = new WhatsNewAdapter(getWhatsNewItems());
        mWhatsNewRecyclerView.setAdapter(mWhatsNewAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);
        preferenceManager.putBoolean(PreferenceKeys.PREF_SEEN_V2_PROMO, true);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        preferenceManager.putInt(PreferenceKeys.PREF_LAST_SEEN_WHATS_NEW_VERSION, Constants.WHATS_NEW_VERSION);
    }

    @Override
    protected int getInitialBottomSheetState() {
        return BottomSheetBehavior.STATE_COLLAPSED;
    }

    @Override
    protected boolean shouldSkipCollapsedState() {
        return false;
    }

    @OnClick(R.id.whats_new_more_icon)
    public void onClickMoreIcon() {
        NavigationUtils.openUrl(getContext(), Constants.ZEPHYR_WHATS_NEW_URL);
    }

    private WhatsNewItem[] getWhatsNewItems() {
        return new WhatsNewItem[] {
                new WhatsNewItem(R.drawable.ic_logo_inverse_white, R.string.menu_whats_new_zephyr_v2_title, R.string.menu_whats_new_zephyr_v2_body),
                new WhatsNewItem(R.drawable.ic_steam, R.string.menu_whats_new_steam_title, R.string.menu_whats_new_steam_body),
                new WhatsNewItem(R.drawable.ic_link, R.string.menu_whats_new_connect_title, R.string.menu_whats_new_connect_body),
                new WhatsNewItem(R.drawable.ic_notifications, R.string.menu_whats_new_notif_title, R.string.menu_whats_new_notif_body)
        };
    }
}
