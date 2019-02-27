package com.texasgamer.zephyr.fragment;

import android.app.Dialog;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.texasgamer.zephyr.R;

import androidx.annotation.NonNull;

/**
 * Base fragment for rounded bottom sheets.
 */
public class RoundedBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }
}
