package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.viewmodel.BaseViewModel;

import butterknife.BindView;

/**
 * Fragment to display open source licenses.
 */
public class LicensesFragment extends BaseFragment {

    @BindView(R.id.licenses_webview)
    WebView mWebView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mWebView.loadUrl("file:///android_asset/open_source_licenses.html");
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_licenses;
    }

    @Override
    protected void setViewBindings(@NonNull View view) {

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
