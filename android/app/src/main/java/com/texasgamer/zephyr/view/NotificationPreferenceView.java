package com.texasgamer.zephyr.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.model.NotificationPreference;
import com.texasgamer.zephyr.service.threading.ZephyrExecutors;
import com.texasgamer.zephyr.util.ApplicationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationPreferenceView extends LinearLayout implements View.OnClickListener {

    @BindView(R.id.notif_pref_icon)
    ImageView prefIcon;
    @BindView(R.id.notif_pref_title)
    TextView prefTitle;
    @BindView(R.id.notif_pref_checkbox)
    CheckBox prefCheckbox;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NotificationPreferenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void onClick(View v) {
        prefCheckbox.toggle();
    }

    @NonNull
    public String getTitle() {
        return prefTitle.getText().toString();
    }

    public boolean getPrefEnabled() {
        return prefCheckbox.isChecked();
    }

    public void setOnPrefChangeListener(@Nullable OnPreferenceChangeListener onPrefChangeListener) {
        mOnPrefChangeListener = onPrefChangeListener;
    }

    public void setNotificationPreference(@NonNull NotificationPreference pref) {
        mPackageName = pref.getPackageName();
        prefIcon.setVisibility(View.INVISIBLE);

        ApplicationUtils appUtils = ZephyrApplication.getApplicationComponent().applicationUtilities();

        ZephyrExecutors.getDiskExecutor().execute(() -> {
            setIcon(appUtils.getAppIcon(mPackageName));
            setTitle(appUtils.getAppName(mPackageName));
            setPrefEnabled(pref.getEnabled());
        });
    }

    private void init() {
        setOnClickListener(this);
        inflate(getContext(), R.layout.item_notification_preference, this);
        ButterKnife.bind(this);
        prefCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
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

    private void setPrefEnabled(boolean enabled) {
        ZephyrExecutors.getMainThreadExecutor().execute(() -> {
            prefCheckbox.setChecked(enabled);
        });
    }

    public interface OnPreferenceChangeListener {
        void onPreferenceChange(@NonNull String packageName, boolean newValue);
    }
}
