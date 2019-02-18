package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.fragment.whatsnew.WhatsNewItemFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WhatsNewFragment extends RoundedBottomSheetDialogFragment {

    public static final String LOG_TAG = "ConnectFragment";

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.pager_indicator)
    IndefinitePagerIndicator pagerIndicator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_whats_new, container, false);
        ButterKnife.bind(this, root);

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        pagerIndicator.attachToViewPager(viewPager);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] childFragments;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);

            WhatsNewItemFragment zephyrV2Fragment = new WhatsNewItemFragment();
            zephyrV2Fragment.setArguments(getArguments(R.string.menu_whats_new_zephyr_v2_title, R.string.menu_whats_new_zephyr_v2_body));

            WhatsNewItemFragment steamFragment = new WhatsNewItemFragment();
            steamFragment.setArguments(getArguments(R.string.menu_whats_new_steam_title, R.string.menu_whats_new_steam_body));

            WhatsNewItemFragment feedbackFragment = new WhatsNewItemFragment();
            feedbackFragment.setArguments(getArguments(R.string.menu_whats_new_feedback_title, R.string.menu_whats_new_feedback_body));

            childFragments = new Fragment[] {
                    zephyrV2Fragment,
                    steamFragment,
                    feedbackFragment
            };
        }

        @Override
        public Fragment getItem(int position) {
            return childFragments[position];
        }

        @Override
        public int getCount() {
            return childFragments.length;
        }

        @NonNull
        private Bundle getArguments(@StringRes int title, @StringRes int body) {
            Bundle arguments = new Bundle();
            arguments.putString(WhatsNewItemFragment.ARG_TITLE, getString(title));
            arguments.putString(WhatsNewItemFragment.ARG_BODY, getString(body));
            return arguments;
        }
    }
}
