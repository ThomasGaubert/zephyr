package com.texasgamer.zephyr.binding;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * {@link BindingAdapter}s for {@link ViewPager}.
 */
public final class ViewPagerBindingAdapters {

    private ViewPagerBindingAdapters() {
    }

    @BindingAdapter("adapter")
    public static void setAdapter(@NonNull ViewPager pagerAdapter, @NonNull PagerAdapter adapter) {
        pagerAdapter.setAdapter(adapter);
    }

    @BindingAdapter("offscreenPageLimit")
    public static void setOffscreenPageLimit(@NonNull ViewPager pagerAdapter, @NonNull int offscreenPageLimit) {
        pagerAdapter.setOffscreenPageLimit(offscreenPageLimit);
    }
}
