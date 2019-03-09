package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.NetworkUtils;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.PreferenceManager;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

/**
 * Join code fragment.
 */
public class JoinCodeFragment extends RoundedBottomSheetDialogFragment {

    @BindView(R.id.join_code_edit_text)
    TextInputEditText mJoinCodeTextEdit;
    @BindView(R.id.join_code_invalid)
    TextView joinCodeInvalidText;

    @Inject
    PreferenceManager preferenceManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_join_code, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);

        Window window = getDialog().getWindow();
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
                }

                dismiss();

                return true;
            }
            return false;
        });
    }

    @OnTextChanged(R.id.join_code_edit_text)
    void onJoinCodeChanged(CharSequence text) {
        String joinCode = text.toString();
        if (!joinCode.isEmpty() && !NetworkUtils.isValidJoinCode(joinCode)) {
            joinCodeInvalidText.setVisibility(View.VISIBLE);
        } else {
            joinCodeInvalidText.setVisibility(View.INVISIBLE);
        }
    }
}
