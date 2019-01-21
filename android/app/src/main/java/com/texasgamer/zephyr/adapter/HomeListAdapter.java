package com.texasgamer.zephyr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.model.ZephyrCard;
import com.texasgamer.zephyr.view.ZephyrCardView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder> {
    private List<ZephyrCard> mCards;

    public HomeListAdapter(@NonNull List<ZephyrCard> cards) {
        mCards = cards;
    }

    @Override
    public HomeListAdapter.HomeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ZephyrCardView itemView = new ZephyrCardView(parent.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int sideMargin = itemView.getResources().getDimensionPixelSize(R.dimen.zephyr_card_margin_sides);
        layoutParams.setMargins(sideMargin, 0, sideMargin, 0);
        itemView.setLayoutParams(layoutParams);

        return new HomeListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeListViewHolder holder, int position) {
        holder.getCard().setZephyrCard(mCards.get(position));
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public void setCards(@NonNull List<ZephyrCard> cards) {
        mCards = cards;
    }

    static class HomeListViewHolder extends RecyclerView.ViewHolder {

        private ZephyrCardView mCard;

        public HomeListViewHolder(@NonNull View v) {
            super(v);
            mCard = (ZephyrCardView) v;
        }

        public ZephyrCardView getCard() {
            return mCard;
        }
    }
}
