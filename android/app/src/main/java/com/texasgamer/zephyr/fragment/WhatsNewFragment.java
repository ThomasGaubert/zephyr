package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.fragment.whatsnew.WhatsNewItemFragment;
import com.texasgamer.zephyr.fragment.whatsnew.WhatsNewItemZephyrFragment;
import com.texasgamer.zephyr.util.preference.IPreferenceManager;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * What's new fragment.
 */
public class WhatsNewFragment extends RoundedBottomSheetDialogFragment {

    public static final String LOG_TAG = "ConnectFragment";

    @Inject
    IPreferenceManager preferenceManager;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.pager_indicator)
    IndefinitePagerIndicator mPagerIndicator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_whats_new, container, false);
        ButterKnife.bind(this, root);

        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        mPagerIndicator.attachToViewPager(mViewPager);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);
        preferenceManager.putBoolean(PreferenceKeys.PREF_SEEN_V2_PROMO, true);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] mChildFragments;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);

            WhatsNewItemFragment zephyrV2Fragment = new WhatsNewItemZephyrFragment();
            zephyrV2Fragment.setArguments(getArguments(R.drawable.ic_logo_inverse_white,
                    R.string.menu_whats_new_zephyr_v2_title,
                    R.string.menu_whats_new_zephyr_v2_body,
                    0,
                    null));

            WhatsNewItemFragment steamFragment = new WhatsNewItemFragment();
            steamFragment.setArguments(getArguments(R.drawable.ic_steam,
                    R.string.menu_whats_new_steam_title,
                    R.string.menu_whats_new_steam_body,
                    R.string.menu_whats_new_steam_button_text,
                    Constants.ZEPHYR_STEAM_URL));

            WhatsNewItemFragment connectFragment = new WhatsNewItemFragment();
            connectFragment.setArguments(getArguments(R.drawable.ic_link,
                    R.string.menu_whats_new_connect_title,
                    R.string.menu_whats_new_connect_body,
                    0,
                    null));

            WhatsNewItemFragment feedbackFragment = new WhatsNewItemFragment();
            feedbackFragment.setArguments(getArguments(R.drawable.ic_notifications,
                    R.string.menu_whats_new_notif_title,
                    R.string.menu_whats_new_notif_body,
                    0,
                    null));

            mChildFragments = new Fragment[] {
                    zephyrV2Fragment,
                    steamFragment,
                    connectFragment,
                    feedbackFragment
            };
        }

        @Override
        public Fragment getItem(int position) {
            return mChildFragments[position];
        }

        @Override
        public int getCount() {
            return mChildFragments.length;
        }

        @NonNull
        private Bundle getArguments(@DrawableRes int icon, @StringRes int title, @StringRes int body, @StringRes int buttonText, @Nullable String buttonUrl) {
            Bundle arguments = new Bundle();
            arguments.putInt(WhatsNewItemFragment.ARG_ICON, icon);
            arguments.putString(WhatsNewItemFragment.ARG_TITLE, getString(title));
            arguments.putString(WhatsNewItemFragment.ARG_BODY, getString(body));

            if (buttonText != 0) {
                if (buttonUrl == null || buttonUrl.isEmpty()) {
                    throw new IllegalArgumentException("Button URL must be defined if button text is specified.");
                }

                arguments.putString(WhatsNewItemFragment.ARG_BUTTON_TEXT, getString(buttonText));
                arguments.putString(WhatsNewItemFragment.ARG_BUTTON_URL, buttonUrl);
            }

            return arguments;
        }
    }
}
