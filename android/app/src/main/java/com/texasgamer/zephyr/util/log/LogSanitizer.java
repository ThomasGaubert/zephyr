package com.texasgamer.zephyr.util.log;

import com.texasgamer.zephyr.util.config.IConfigManager;

import java.util.regex.Pattern;

import androidx.annotation.NonNull;

public class LogSanitizer implements ILogSanitizer {

    private static final String IPV4_REGEX = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    private IConfigManager configManager;
    private Pattern ipV4Pattern;
    private Pattern emailPattern;

    public LogSanitizer(IConfigManager configManager) {
        this.configManager = configManager;

        ipV4Pattern = Pattern.compile(IPV4_REGEX, Pattern.CASE_INSENSITIVE);
        emailPattern = Pattern.compile(IPV4_REGEX, Pattern.CASE_INSENSITIVE);
    }

    @NonNull
    public String sanitize(@NonNull String stringToSanitize) {
        if (enabled()) {
            String sanitizedString = ipV4Pattern.matcher(stringToSanitize).replaceAll("[REDACTED IP]");
            sanitizedString = emailPattern.matcher(stringToSanitize).replaceAll("[REDACTED EMAIL]");
            return sanitizedString;
        }

        return stringToSanitize;
    }

    private boolean enabled() {
        return configManager.isProduction();
    }
}
