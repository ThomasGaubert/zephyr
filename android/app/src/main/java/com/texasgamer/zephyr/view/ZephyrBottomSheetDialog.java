package com.texasgamer.zephyr.view;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.layout.ILayoutManager;

import javax.inject.Inject;

public class ZephyrBottomSheetDialog extends BottomSheetDialog {

    @Inject
    ILayoutManager mLayoutManager;

    public ZephyrBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public ZephyrBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected ZephyrBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ZephyrApplication.getApplicationComponent().inject(this);

        Window window = getWindow();
        if (window != null && mLayoutManager != null) {
            window.setLayout(mLayoutManager.getPrimaryLayoutWidth(), ViewGroup.LayoutParams.MATCH_PARENT);

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.START | Gravity.BOTTOM;
            window.setAttributes(windowAttributes);
        }
    }
}
