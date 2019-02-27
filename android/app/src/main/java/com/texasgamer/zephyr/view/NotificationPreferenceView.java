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

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.model.NotificationPreference;
import com.texasgamer.zephyr.service.threading.ZephyrExecutors;
import com.texasgamer.zephyr.util.ApplicationUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Notification preference view.
 */
public class NotificationPreferenceView extends ConstraintLayout implements View.OnClickListener {

    @BindView(R.id.notif_pref_icon)
    ImageView prefIcon;
    @BindView(R.id.notif_pref_title)
    TextView prefTitle;
    @BindView(R.id.top_view)
    View topView;
    @BindView(R.id.bottom_view)
    View bottomView;
    @BindView(R.id.summary_text)
    TextView summaryText;
    @BindView(R.id.notif_pref_switch)
    Switch prefSwitch;

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
        prefSwitch.toggle();
    }

    @NonNull
    public String getTitle() {
        return prefTitle.getText().toString();
    }

    public boolean getPrefEnabled() {
        return prefSwitch.isChecked();
    }

    public void setOnPrefChangeListener(@Nullable OnPreferenceChangeListener onPrefChangeListener) {
        mOnPrefChangeListener = onPrefChangeListener;
    }

    public void setNotificationPreference(@NonNull NotificationPreference pref) {
        mPackageName = pref.getPackageName();

        ApplicationUtils appUtils = ZephyrApplication.getApplicationComponent().applicationUtilities();

        ZephyrExecutors.getDiskExecutor().execute(() -> {
            if (pref.getIcon() == null) {
                pref.setIcon(appUtils.getAppIcon(mPackageName));
            }

            setIcon(pref.getIcon());
            setTitle(pref.getTitle());
            setColors(pref.getColor());
            setPrefEnabled(pref.getEnabled());
        });
    }

    private void init() {
        setOnClickListener(this);
        inflate(getContext(), R.layout.item_notification_preference, this);
        ButterKnife.bind(this);
        prefSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mOnPrefChangeListener != null) {
                mOnPrefChangeListener.onPreferenceChange(mPackageName, getPrefEnabled());
            }
        });
    }

    private void setIcon(@Nullable Drawable drawable) {
        ZephyrExecutors.getMainThreadExecutor().execute(() -> {
            if (drawable != null) {
                prefIcon.setImageDrawable(drawable);
            } else {
                prefIcon.setVisibility(View.INVISIBLE);
            }

            prefIcon.setVisibility(View.VISIBLE);
        });
    }

    private void setTitle(@NonNull String title) {
        ZephyrExecutors.getMainThreadExecutor().execute(() -> {
            prefTitle.setText(title);
        });
    }

    private void setColors(@ColorInt int color) {
        ZephyrExecutors.getMainThreadExecutor().execute(() -> {
            topView.setBackground(createBackground(color, false));
            bottomView.setBackground(createBackground(ColorUtils.blendARGB(color, Color.BLACK, 0.2f), true));

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
            prefSwitch.getThumbDrawable().setTintList(buttonStates);
            prefSwitch.getTrackDrawable().setTintList(buttonStates);
        });
    }

    private void setPrefEnabled(boolean enabled) {
        ZephyrExecutors.getMainThreadExecutor().execute(() -> {
            summaryText.setText(enabled ? R.string.notif_pref_enabled : R.string.notif_pref_disabled);
            prefSwitch.setChecked(enabled);
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
