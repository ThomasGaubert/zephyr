package com.texasgamer.zephyr.util.privacy;

/**
 * Privacy manager interface.
 */
public interface IPrivacyManager {

    boolean havePrivacySettingsChanged();

    boolean isEditingUsageDataCollectionSettingEnabled();

    boolean isUsageDataCollectionEnabled();

    void setUsageDataCollectionEnabled(boolean enabled);

    boolean isEditingCrashReportingSettingEnabled();

    boolean isCrashReportingEnabled();

    void setCrashReportingEnabled(boolean enabled);

    boolean isEditingUuidSettingEnabled();

    boolean isUuidEnabled();

    void setUuidEnabled(boolean enabled);

    String getUuid();

    String generateUuid();
}
