package com.texasgamer.zephyr.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.NavigationUtils;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.privacy.IPrivacyManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Privacy fragment.
 */
public class PrivacyFragment extends RoundedBottomSheetDialogFragment {

    @Inject
    IConfigManager configManager;
    @Inject
    IPrivacyManager privacyManager;

    @BindView(R.id.privacy_disabled_beta_text)
    TextView betaPrivacyDisabledText;
    @BindView(R.id.privacy_disabled_config_text)
    TextView configPrivacyDisabledText;
    @BindView(R.id.privacy_setting_usage_data)
    Switch usageDataSwitch;
    @BindView(R.id.privacy_setting_crash_reports)
    Switch crashReportSwitch;
    @BindView(R.id.privacy_setting_uuid)
    Switch uuidSwitch;
    @BindView(R.id.privacy_btn_uuid_generate)
    Button generateUuidButton;
    @BindView(R.id.privacy_uuid)
    TextView uuidTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_privacy, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);

        usageDataSwitch.setChecked(privacyManager.isUsageDataCollectionEnabled());
        crashReportSwitch.setChecked(privacyManager.isCrashReportingEnabled());
        uuidSwitch.setChecked(privacyManager.isUuidEnabled());
        uuidTextView.setText(privacyManager.getUuid());

        boolean settingDisabledFlag = false;
        if (!privacyManager.isEditingUsageDataCollectionSettingEnabled()) {
            usageDataSwitch.setEnabled(false);
            settingDisabledFlag = true;
        }

        if (!privacyManager.isEditingUsageDataCollectionSettingEnabled()) {
            crashReportSwitch.setEnabled(false);
            settingDisabledFlag = true;
        }

        if (!privacyManager.isEditingUsageDataCollectionSettingEnabled()) {
            uuidSwitch.setEnabled(false);
            settingDisabledFlag = true;
        }

        if (!uuidSwitch.isChecked()) {
            generateUuidButton.setVisibility(View.GONE);
        }

        if (settingDisabledFlag && (!configManager.isFirebaseAnalyticsEnabled() || !configManager.isFirebaseCrashlyticsEnabled())) {
            configPrivacyDisabledText.setVisibility(View.VISIBLE);
        }

        if (configManager.isBeta()) {
            betaPrivacyDisabledText.setVisibility(View.VISIBLE);
        }
    }

    @OnCheckedChanged(R.id.privacy_setting_usage_data)
    void onToggleUsageDataSwitch(CompoundButton view, boolean isChecked) {
        privacyManager.setUsageDataCollectionEnabled(isChecked);
    }

    @OnCheckedChanged(R.id.privacy_setting_crash_reports)
    void onToggleCrashReportsSwitch(CompoundButton view, boolean isChecked) {
        privacyManager.setCrashReportingEnabled(isChecked);
    }

    @OnCheckedChanged(R.id.privacy_setting_uuid)
    void onToggleUuidSwitch(CompoundButton view, boolean isChecked) {
        privacyManager.setUuidEnabled(isChecked);
        uuidTextView.setText(privacyManager.getUuid());
        generateUuidButton.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.privacy_btn_uuid_generate)
    void onClickGenerateUuidButton() {
        String uuid = privacyManager.generateUuid();
        uuidTextView.setText(uuid);
    }

    @OnClick(R.id.privacy_btn_policy)
    void onClickPrivacyPolicyButton() {
        NavigationUtils.openUrl(getContext(), Constants.ZEPHYR_PRIVACY_URL);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);

        if (privacyManager.havePrivacySettingsChanged()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.menu_privacy_restart_app_title)
                    .setMessage(getContext().getString(R.string.menu_privacy_restart_app_body))
                    .setCancelable(false)
                    .setPositiveButton(R.string.menu_privacy_restart_app_btn, (dialog, which) -> ApplicationUtils.restartApp(ZephyrApplication.getInstance()))
                    .create();
            alertDialog.show();
        }
    }
}
