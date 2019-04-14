package com.texasgamer.zephyr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * ZephyrCard view pager.
 */
public class ZephyrCardViewPager extends ViewPager {

    public ZephyrCardViewPager(@NonNull Context context) {
        super(context);
    }

    public ZephyrCardViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                requestLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = heightMeasureSpec;
        int height = 0;

        if (getChildCount() > 0) {
            for (int i = getCurrentItem() - 1; i <= getCurrentItem() + 1; i++) {
                if (i < 0 || i > getChildCount() - 1) {
                    continue;
                }

                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) {
                    height = h;
                }
            }
        }

        if (height != 0) {
            measuredHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, measuredHeight);
    }
}
