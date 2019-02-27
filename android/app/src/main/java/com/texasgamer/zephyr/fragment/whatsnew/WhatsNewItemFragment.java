package com.texasgamer.zephyr.fragment.whatsnew;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.fragment.BaseFragment;
import com.texasgamer.zephyr.viewmodel.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * What's new item fragment.
 */
public class WhatsNewItemFragment extends BaseFragment {

    public static final String ARG_TITLE = "title";
    public static final String ARG_BODY = "body";

    @BindView(R.id.whats_new_item_title)
    TextView mTitleTextView;
    @BindView(R.id.whats_new_item_body)
    TextView mBodyTextView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTitleTextView.setText(getArguments().getString(ARG_TITLE));
            mBodyTextView.setText(getArguments().getString(ARG_BODY));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_whats_new_item;
    }

    @Override
    protected void setViewBindings(View view) {

    }

    @Override
    protected BaseViewModel onCreateViewModel() {
        return null;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }
}
