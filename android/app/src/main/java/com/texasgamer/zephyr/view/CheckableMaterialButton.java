package com.texasgamer.zephyr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.google.android.material.button.MaterialButton;

public class CheckableMaterialButton extends MaterialButton implements Checkable {

    private boolean mChecked = true;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public CheckableMaterialButton(Context context) {
        super(context);
    }

    public CheckableMaterialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableMaterialButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
