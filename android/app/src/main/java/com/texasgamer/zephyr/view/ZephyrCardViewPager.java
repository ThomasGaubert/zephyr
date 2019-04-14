package com.texasgamer.zephyr.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * ZephyrCard view pager.
 */
public class ZephyrCardViewPager extends ViewPager {

    private boolean mAnimating = false;

    public ZephyrCardViewPager(@NonNull Context context) {
        super(context);
    }

    public ZephyrCardViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                requestLayout();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!mAnimating) {
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

            if (getLayoutParams().height != 0 && heightMeasureSpec != measuredHeight) {
                ValueAnimator slideAnimator = ValueAnimator
                        .ofInt(getHeight(), height)
                        .setDuration(100);

                slideAnimator.addUpdateListener(animation -> {
                    getLayoutParams().height = (Integer) animation.getAnimatedValue();
                    requestLayout();
                });

                slideAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mAnimating = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mAnimating = false;
                    }
                });

                AnimatorSet set = new AnimatorSet();
                set.play(slideAnimator);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.start();

                mAnimating = true;
            } else {
                heightMeasureSpec = measuredHeight;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
