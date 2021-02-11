package com.texasgamer.zephyr.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.model.ZephyrNotificationPreference;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.util.threading.ZephyrExecutors;

/**
 * Notification preference view.
 */
public class NotificationPreferenceView extends ConstraintLayout implements View.OnClickListener {

    private ImageView mPrefIcon;
    private TextView mPrefTitle;
    private View mTopView;
    private View mBottomView;
    private TextView mSummaryText;
    private Switch mPrefSwitch;

    private String mPackageName;
    private OnPreferenceChangeListener mOnPrefChangeListener;

    public NotificationPreferenceView(Context context) {
        this(context, null);
    }

    public NotificationPreferenceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotificationPreferenceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void onClick(View v) {
        mPrefSwitch.toggle();
    }

    @NonNull
    public String getTitle() {
        return mPrefTitle.getText().toString();
    }

    public boolean isPrefEnabled() {
        return mPrefSwitch.isChecked();
    }

    public void setOnPrefChangeListener(@Nullable OnPreferenceChangeListener onPrefChangeListener) {
        mOnPrefChangeListener = onPrefChangeListener;
    }

    public void setNotificationPreference(@NonNull ZephyrNotificationPreference pref) {
        mPackageName = pref.getPackageName();

        ApplicationUtils appUtils = ZephyrApplication.getApplicationComponent().applicationUtilities();

        setTitle(pref.getTitle());
        setColors(pref.getColor());
        setPrefEnabled(pref.isEnabled());
        if (pref.getIcon() != null) {
            setIcon(pref.getIcon());
        } else {
            setIcon(null);
            ZephyrExecutors.getDiskExecutor().execute(() -> {
                pref.setIcon(appUtils.getAppIcon(mPackageName));
                setIcon(pref.getIcon());
            });
        }
    }

    private void init() {
        setOnClickListener(this);
        inflate(getContext(), R.layout.item_notification_preference, this);

        mPrefIcon = findViewById(R.id.notif_pref_icon);
        mPrefTitle = findViewById(R.id.notif_pref_title);
        mTopView = findViewById(R.id.top_view);
        mBottomView = findViewById(R.id.bottom_view);
        mSummaryText = findViewById(R.id.summary_text);
        mPrefSwitch = findViewById(R.id.notif_pref_switch);

        mPrefSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mOnPrefChangeListener != null) {
                mOnPrefChangeListener.onPreferenceChange(mPackageName, isPrefEnabled());
            }
        });
    }

    private void setIcon(@Nullable Drawable drawable) {
        ZephyrExecutors.getMainThreadExecutor().execute(() -> {
            if (drawable != null) {
                mPrefIcon.setImageDrawable(drawable);
            } else {
                mPrefIcon.setVisibility(View.INVISIBLE);
            }

            mPrefIcon.setVisibility(View.VISIBLE);
        });
    }

    private void setTitle(@NonNull String title) {
        ZephyrExecutors.getMainThreadExecutor().execute(() -> {
            mPrefTitle.setText(title);
        });
    }

    private void setColors(@ColorInt int color) {
        ZephyrExecutors.getMainThreadExecutor().execute(() -> {
            mTopView.setBackground(createBackground(color, false));
            mBottomView.setBackground(createBackground(ColorUtils.blendARGB(color, Color.BLACK, 0.2f), true));

            ColorStateList buttonStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            Color.GRAY,
                            ColorUtils.blendARGB(color, Color.BLACK, 0.4f),
                            ColorUtils.blendARGB(color, Color.BLACK, 0.2f)
                    }
            );
            mPrefSwitch.getThumbDrawable().setTintList(buttonStates);
            mPrefSwitch.getTrackDrawable().setTintList(buttonStates);
        });
    }

    private void setPrefEnabled(boolean enabled) {
        ZephyrExecutors.getMainThreadExecutor().execute(() -> {
            mSummaryText.setText(enabled ? R.string.notif_pref_enabled : R.string.notif_pref_disabled);
            mPrefSwitch.setChecked(enabled);

            // Delay showing switch to allow animation to finish
            postDelayed(() -> mPrefSwitch.setVisibility(View.VISIBLE), 100);
        });
    }

    private Drawable createBackground(@ColorInt int color, boolean isBottom) {
        float cornerRadius = getResources().getDisplayMetrics().density * 8;

        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);

        if (isBottom) {
            background.setCornerRadii(new float[]{0f, 0f, 0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius});
        } else {
            background.setCornerRadii(new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f, 0f, 0f});
        }

        background.setColor(color);
        return background;
    }

    /**
     * Listener for when the notification preference is changed.
     */
    public interface OnPreferenceChangeListener {
        void onPreferenceChange(@NonNull String packageName, boolean newValue);
    }
}
