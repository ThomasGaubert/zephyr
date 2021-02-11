package com.texasgamer.zephyr.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.service.QuickSettingService;
import com.texasgamer.zephyr.util.NetworkUtils;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Join code fragment.
 */
public class JoinCodeFragment extends RoundedBottomSheetDialogFragment {

    private TextInputEditText mJoinCodeTextEdit;
    private TextView joinCodeInvalidText;

    @Inject
    IPreferenceManager preferenceManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_join_code, container, false);
        mJoinCodeTextEdit = root.findViewById(R.id.join_code_edit_text);
        joinCodeInvalidText = root.findViewById(R.id.join_code_invalid);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);

        Window window = Objects.requireNonNull(getDialog()).getWindow();
        if (window != null) {
            mJoinCodeTextEdit.requestFocus();
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        mJoinCodeTextEdit.setOnKeyListener((v, keyCode, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                Editable joinCodeEditable = mJoinCodeTextEdit.getText();

                if (joinCodeEditable != null) {
                    String joinCode = joinCodeEditable.toString();
                    if (joinCode.trim().isEmpty() || !NetworkUtils.isValidJoinCode(joinCode)) {
                        return true;
                    }

                    preferenceManager.putString(PreferenceKeys.PREF_JOIN_CODE, mJoinCodeTextEdit.getText().toString());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        QuickSettingService.updateQuickSettingTile(requireContext());
                    }
                }

                dismiss();

                return true;
            }
            return false;
        });

        mJoinCodeTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String joinCode = s.toString();
                if (!joinCode.isEmpty() && !NetworkUtils.isValidJoinCode(joinCode)) {
                    joinCodeInvalidText.setVisibility(View.VISIBLE);
                } else {
                    joinCodeInvalidText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int getInitialBottomSheetState() {
        return BottomSheetBehavior.STATE_EXPANDED;
    }

    @Override
    protected boolean shouldSkipCollapsedState() {
        return true;
    }
}
