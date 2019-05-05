package com.texasgamer.zephyr.util.privacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.util.config.IConfigManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import java.util.UUID;

/**
 * Privacy manager.
 */
public class PrivacyManager implements IPrivacyManager {

    private SharedPreferences mSharedPreferences;
    private IConfigManager mConfigManager;

    private boolean mHavePrivacySettingsChanged;

    // Privacy settings at app launch
    private boolean mInitialUsageDataSetting;
    private boolean mInitialCrashReportSetting;
    private boolean mInitialUuidSetting;
    private String mInitialUuid;

    // Privacy settings as currently set by user
    private boolean mCurrentUsageDataSetting;
    private boolean mCurrentCrashReportSetting;
    private boolean mCurrentUuidSetting;
    private String mCurrentUuid;

    public PrivacyManager(@NonNull Context context, @NonNull IConfigManager configManager) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mConfigManager = configManager;

        mInitialUsageDataSetting = mSharedPreferences.getBoolean(PreferenceKeys.PREF_ENABLE_USAGE_DATA, true);
        mInitialCrashReportSetting = mSharedPreferences.getBoolean(PreferenceKeys.PREF_ENABLE_CRASH_REPORTS, true);
        mInitialUuidSetting = mSharedPreferences.getBoolean(PreferenceKeys.PREF_ENABLE_UUID, true);
        mInitialUuid = mSharedPreferences.getString(PreferenceKeys.PREF_UUID, Constants.DEFAULT_UUID);

        if (mInitialUuidSetting && mInitialUuid.equals(Constants.DEFAULT_UUID)) {
            generateUuid();
        }

        mCurrentUsageDataSetting = mInitialUsageDataSetting;
        mCurrentCrashReportSetting = mInitialCrashReportSetting;
        mCurrentUuidSetting = mInitialUuidSetting;
        mCurrentUuid = mInitialUuid;

        mHavePrivacySettingsChanged = false;
    }

    @Override
    public boolean havePrivacySettingsChanged() {
        return mHavePrivacySettingsChanged;
    }

    @Override
    public boolean isEditingUsageDataCollectionSettingEnabled() {
        return !mConfigManager.isBeta() && mConfigManager.isFirebaseAnalyticsEnabled();
    }

    @Override
    public boolean isUsageDataCollectionEnabled() {
        return mConfigManager.isFirebaseAnalyticsEnabled() && (mConfigManager.isBeta() || mCurrentUsageDataSetting);
    }

    @Override
    public void setUsageDataCollectionEnabled(boolean enabled) {
        mCurrentUsageDataSetting = enabled;
        mSharedPreferences.edit().putBoolean(PreferenceKeys.PREF_ENABLE_USAGE_DATA, mCurrentUsageDataSetting).apply();

        checkIfPrivacySettingsChanged();
    }

    @Override
    public boolean isEditingCrashReportingSettingEnabled() {
        return !mConfigManager.isBeta() && mConfigManager.isFirebaseCrashlyticsEnabled();
    }

    @Override
    public boolean isCrashReportingEnabled() {
        return mConfigManager.isFirebaseCrashlyticsEnabled() && (mConfigManager.isBeta() || mCurrentCrashReportSetting);
    }

    @Override
    public void setCrashReportingEnabled(boolean enabled) {
        mCurrentCrashReportSetting = enabled;
        mSharedPreferences.edit().putBoolean(PreferenceKeys.PREF_ENABLE_CRASH_REPORTS, mCurrentCrashReportSetting).apply();

        checkIfPrivacySettingsChanged();
    }

    @Override
    public boolean isEditingUuidSettingEnabled() {
        return !mConfigManager.isBeta();
    }

    @Override
    public boolean isUuidEnabled() {
        return (mConfigManager.isFirebaseAnalyticsEnabled() || mConfigManager.isFirebaseCrashlyticsEnabled())
                && (mConfigManager.isBeta() || mCurrentUuidSetting);
    }

    @Override
    public void setUuidEnabled(boolean enabled) {
        if (enabled == mCurrentUuidSetting) {
            return;
        }

        mCurrentUuidSetting = enabled;
        mSharedPreferences.edit().putBoolean(PreferenceKeys.PREF_ENABLE_UUID, mCurrentUuidSetting).apply();

        generateUuid();

        checkIfPrivacySettingsChanged();
    }

    @Override
    public String getUuid() {
        return isUuidEnabled() ? mCurrentUuid : Constants.DEFAULT_UUID;
    }

    @Override
    public String generateUuid() {
        mCurrentUuid = isUuidEnabled() ? UUID.randomUUID().toString() : Constants.DEFAULT_UUID;

        mSharedPreferences.edit().putString(PreferenceKeys.PREF_UUID, mCurrentUuid).apply();
        checkIfPrivacySettingsChanged();

        return mCurrentUuid;
    }

    private void checkIfPrivacySettingsChanged() {
        mHavePrivacySettingsChanged = mInitialUsageDataSetting != mCurrentUsageDataSetting
                || mInitialCrashReportSetting != mCurrentCrashReportSetting
                || mInitialUuidSetting != mCurrentUuidSetting
                || !mInitialUuid.equals(mCurrentUuid);
    }
}
