package com.texasgamer.zephyr.fragment.whatsnew;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.fragment.BaseFragment;
import com.texasgamer.zephyr.viewmodel.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * What's new item fragment for "Zephyr v2" item.
 */
public class WhatsNewItemZephyrFragment extends WhatsNewItemFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_whats_new_item_zephyr;
    }
}
