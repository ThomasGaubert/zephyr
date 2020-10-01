package com.texasgamer.zephyr.binding;

import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

/**
 * {@link BindingAdapter}s for {@link WebView}.
 */
public final class WebViewBindingAdapters {

    private WebViewBindingAdapters() {
    }

    @BindingAdapter("loadUrl")
    public static void loadUrl(@NonNull WebView view, @NonNull String url) {
        view.loadUrl(url);
    }
}
