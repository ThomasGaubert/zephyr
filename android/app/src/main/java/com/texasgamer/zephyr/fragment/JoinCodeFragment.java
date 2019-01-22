package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.textfield.TextInputEditText;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.preference.PreferenceManager;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class JoinCodeFragment extends RoundedBottomSheetDialogFragment {

    @BindView(R.id.join_code_edit_text)
    TextInputEditText joinCodeTextEdit;

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
            joinCodeTextEdit.requestFocus();
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        joinCodeTextEdit.setOnKeyListener((view1, keyCode, keyevent) -> {
            if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                Editable joinCodeEditable = joinCodeTextEdit.getText();

                if (joinCodeEditable != null) {
                    String joinCode = joinCodeEditable.toString();
                    if (joinCode.trim().isEmpty()) {
                        return true;
                    }

                    preferenceManager.putString(PreferenceKeys.PREF_JOIN_CODE, joinCodeTextEdit.getText().toString());
                }
                return true;
            }
            return false;
        });
    }

    @OnTextChanged(R.id.join_code_edit_text)
    protected void onJoinCodeChanged(CharSequence text) {
        String joinCode = text.toString();
        if (joinCode.matches("[a-zA-Z]+|,+|\\s+")) {
            joinCodeTextEdit.setError(getString(R.string.menu_join_code_invalid));
        } else {
            joinCodeTextEdit.setError(null);
        }
    }
}
