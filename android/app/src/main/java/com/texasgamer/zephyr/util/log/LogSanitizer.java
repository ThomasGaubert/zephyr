package com.texasgamer.zephyr.util.log;

import com.texasgamer.zephyr.util.config.IConfigManager;

import java.util.regex.Pattern;

import androidx.annotation.NonNull;

/**
 * Log sanitizer. Cleans logs of sensitive data.
 */
public class LogSanitizer implements ILogSanitizer {

    private static final String IPV4_REGEX = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    private IConfigManager mConfigManager;
    private Pattern mIpV4Pattern;
    private Pattern mEmailPattern;

    public LogSanitizer(IConfigManager configManager) {
        this.mConfigManager = configManager;

        mIpV4Pattern = Pattern.compile(IPV4_REGEX, Pattern.CASE_INSENSITIVE);
        mEmailPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
    }

    @NonNull
    public String sanitize(@NonNull String stringToSanitize) {
        if (enabled()) {
            String sanitizedString = mIpV4Pattern.matcher(stringToSanitize).replaceAll("[REDACTED IP]");
            sanitizedString = mEmailPattern.matcher(stringToSanitize).replaceAll("[REDACTED EMAIL]");
            return sanitizedString;
        }

        return stringToSanitize;
    }

    private boolean enabled() {
        return mConfigManager.isProduction();
    }
}
