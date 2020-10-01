package com.texasgamer.zephyr.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.view.ZephyrBottomSheetDialog;

/**
 * Base fragment for rounded bottom sheets.
 */
public class RoundedBottomSheetDialogFragment extends BottomSheetDialogFragment implements DialogInterface.OnShowListener {

    @Override
    public int getTheme() {
        return R.style.Theme_Zephyr_BottomSheetDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = new ZephyrBottomSheetDialog(requireContext(), getTheme());
        bottomSheetDialog.setOnShowListener(this);
        return bottomSheetDialog;
    }

    @Override
    public void onShow(@NonNull DialogInterface dialog) {
        BottomSheetDialog d = (BottomSheetDialog) dialog;
        FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setSkipCollapsed(shouldSkipCollapsedState());
            bottomSheetBehavior.setState(getInitialBottomSheetState());
        }
    }

    protected int getInitialBottomSheetState() {
        return BottomSheetBehavior.STATE_COLLAPSED;
    }

    protected boolean shouldSkipCollapsedState() {
        return false;
    }
}
