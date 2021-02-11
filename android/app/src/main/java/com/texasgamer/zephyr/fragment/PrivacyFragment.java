package com.texasgamer.zephyr.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.navigation.NavigationUtils;
import com.texasgamer.zephyr.util.privacy.IPrivacyManager;

import javax.inject.Inject;

/**
 * Privacy fragment.
 */
public class PrivacyFragment extends RoundedBottomSheetDialogFragment {

    @Inject
    IConfigManager configManager;
    @Inject
    IPrivacyManager privacyManager;

    private TextView mConfigPrivacyDisabledText;
    private Switch mUsageDataSwitch;
    private Switch mCrashReportSwitch;
    private Switch mUuidSwitch;
    private Button mGenerateUuidButton;
    private TextView mUuidTextView;
    private Button mPrivacyPolicyButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_privacy, container, false);
        mConfigPrivacyDisabledText = root.findViewById(R.id.privacy_disabled_config_text);
        mUsageDataSwitch = root.findViewById(R.id.privacy_setting_usage_data);
        mCrashReportSwitch = root.findViewById(R.id.privacy_setting_crash_reports);
        mUuidSwitch = root.findViewById(R.id.privacy_setting_uuid);
        mGenerateUuidButton = root.findViewById(R.id.privacy_btn_uuid_generate);
        mUuidTextView = root.findViewById(R.id.privacy_uuid);
        mPrivacyPolicyButton = root.findViewById(R.id.privacy_btn_policy);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);

        mUsageDataSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                privacyManager.setUsageDataCollectionEnabled(isChecked));

        mCrashReportSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                privacyManager.setCrashReportingEnabled(isChecked));

        mUuidSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            privacyManager.setUuidEnabled(isChecked);
            mUuidTextView.setText(privacyManager.getUuid());
            mGenerateUuidButton.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        mGenerateUuidButton.setOnClickListener(v ->
                mUuidTextView.setText(privacyManager.generateUuid()));

        mPrivacyPolicyButton.setOnClickListener(v ->
                NavigationUtils.openUrl(requireContext(), Constants.ZEPHYR_PRIVACY_URL));

        mUsageDataSwitch.setChecked(privacyManager.isUsageDataCollectionEnabled());
        mCrashReportSwitch.setChecked(privacyManager.isCrashReportingEnabled());
        mUuidSwitch.setChecked(privacyManager.isUuidEnabled());
        mUuidTextView.setText(privacyManager.getUuid());

        boolean settingDisabledFlag = false;
        if (!privacyManager.isEditingUsageDataCollectionSettingEnabled()) {
            mUsageDataSwitch.setEnabled(false);
            settingDisabledFlag = true;
        }

        if (!privacyManager.isEditingUsageDataCollectionSettingEnabled()) {
            mCrashReportSwitch.setEnabled(false);
            settingDisabledFlag = true;
        }

        if (!privacyManager.isEditingUsageDataCollectionSettingEnabled()) {
            mUuidSwitch.setEnabled(false);
            settingDisabledFlag = true;
        }

        if (!mUuidSwitch.isChecked()) {
            mGenerateUuidButton.setVisibility(View.GONE);
        }

        if (settingDisabledFlag && (!configManager.isFirebaseAnalyticsEnabled() || !configManager.isFirebaseCrashlyticsEnabled())) {
            mConfigPrivacyDisabledText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);

        if (privacyManager.havePrivacySettingsChanged() && getContext() != null) {
            AlertDialog alertDialog = new MaterialAlertDialogBuilder(getContext())
                    .setTitle(R.string.menu_privacy_restart_app_title)
                    .setMessage(getContext().getString(R.string.menu_privacy_restart_app_body))
                    .setCancelable(false)
                    .setPositiveButton(R.string.menu_privacy_restart_app_btn, (dialog, which) -> ApplicationUtils.restartApp(ZephyrApplication.getInstance()))
                    .create();
            alertDialog.show();
        }
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
