package com.texasgamer.zephyr.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.texasgamer.zephyr.R;

import androidx.annotation.NonNull;

/**
 * Base fragment for rounded bottom sheets.
 */
public class RoundedBottomSheetDialogFragment extends BottomSheetDialogFragment implements DialogInterface.OnShowListener {

    @Override
    public int getTheme() {
        return R.style.Theme_Zephyr_Dark_BottomSheetDialog;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), getTheme());
        bottomSheetDialog.setOnShowListener(this);
        return bottomSheetDialog;
    }

    public void onShow(DialogInterface dialog) {
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
