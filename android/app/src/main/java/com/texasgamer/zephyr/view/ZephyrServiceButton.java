package com.texasgamer.zephyr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.google.android.material.button.MaterialButton;
import com.texasgamer.zephyr.R;

/**
 * Material Design themed button that can be toggled between two states to control the Zephyr socket service.
 * The button is checked when the service is stopped (button displays "Start" with crossed connection icon).
 * The button is not checked when the service is running (button displays "Stop" with connection icon).
 */
public class ZephyrServiceButton extends MaterialButton {

    public ZephyrServiceButton(Context context) {
        super(context);
        init();
    }

    public ZephyrServiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZephyrServiceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setChecked(boolean checked) {
        boolean currentValue = isChecked();
        super.setChecked(checked);
        if (currentValue != checked) {
            updateText();
        }
    }

    public void setServiceRunning(boolean isServiceRunning) {
        setChecked(!isServiceRunning);
    }

    public boolean isServiceRunning() {
        return !isChecked();
    }

    private void init() {
        setCheckable(true);
        setServiceRunning(false);
    }

    private void updateText() {
        setText(isServiceRunning() ? R.string.btn_connection_stop : R.string.btn_connection_start);
    }
}
