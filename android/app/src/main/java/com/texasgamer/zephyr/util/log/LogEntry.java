package com.texasgamer.zephyr.util.log;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Represents log entry.
 */
public class LogEntry implements Serializable {

    private Date mTimestamp;
    @LogLevel
    private int mPriority;
    private String mTag;
    private String mMessage;

    public LogEntry(@LogLevel int priority, @NonNull String tag, @NonNull String message) {
        mTimestamp = Calendar.getInstance().getTime();
        mPriority = priority;
        mTag = tag;
        mMessage = message;
    }

    @NonNull
    @Override
    public String toString() {
        switch (mPriority) {
            case LogLevel.VERBOSE:
                return String.format("%s V/%s: %s", getFormattedTimestamp(), mTag, mMessage);
            case LogLevel.DEBUG:
                return String.format("%s D/%s: %s", getFormattedTimestamp(), mTag, mMessage);
            case LogLevel.INFO:
                return String.format("%s I/%s: %s", getFormattedTimestamp(), mTag, mMessage);
            case LogLevel.WARNING:
                return String.format("%s W/%s: %s", getFormattedTimestamp(), mTag, mMessage);
            case LogLevel.ERROR:
                return String.format("%s E/%s: %s", getFormattedTimestamp(), mTag, mMessage);
            default:
                return String.format("%s ?/%s: %s", getFormattedTimestamp(), mTag, mMessage);
        }
    }

    @LogLevel
    public int getPriority() {
        return mPriority;
    }

    @NonNull
    public String getTag() {
        return mTag;
    }

    @NonNull
    public String getMessage() {
        return mMessage;
    }

    private String getFormattedTimestamp() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);
        return df.format(mTimestamp);
    }
}
