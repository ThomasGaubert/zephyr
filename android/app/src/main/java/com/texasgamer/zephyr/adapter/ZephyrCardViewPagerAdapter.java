package com.texasgamer.zephyr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.model.ZephyrCard;
import com.texasgamer.zephyr.view.ZephyrCardView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ZephyrCardViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<ZephyrCard> mCards;

    public ZephyrCardViewPagerAdapter(@NonNull Context context, @NonNull List<ZephyrCard> cards) {
        mContext = context;
        mCards = cards;
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        ZephyrCardView itemView = new ZephyrCardView(collection.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        itemView.setZephyrCard(mCards.get(position));

        int sideMargin = itemView.getResources().getDimensionPixelSize(R.dimen.zephyr_card_margin_sides);
        layoutParams.setMargins(sideMargin, 0, sideMargin, 0);
        itemView.setLayoutParams(layoutParams);

        collection.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(mCards.get(position).getTitle());
    }
}
