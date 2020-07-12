package com.texasgamer.zephyr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.model.WhatsNewItem;

import java.util.Arrays;

/**
 * Adapter for "What's new" RecyclerView.
 */
public class WhatsNewAdapter extends RecyclerView.Adapter<WhatsNewAdapter.ViewHolder> {

    private WhatsNewItem[] mWhatsNewItems;

    public WhatsNewAdapter(@NonNull WhatsNewItem... whatsNewItems) {
        mWhatsNewItems = Arrays.copyOf(whatsNewItems, whatsNewItems.length);
    }

    @NonNull
    @Override
    public WhatsNewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_whats_new, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mIcon.setImageResource(mWhatsNewItems[position].getIconRes());
        holder.mTitle.setText(mWhatsNewItems[position].getTitleRes());
        holder.mBody.setText(mWhatsNewItems[position].getBodyRes());
    }

    @Override
    public int getItemCount() {
        return mWhatsNewItems.length;
    }

    /**
     * ViewHolder for WhatsNewAdapter.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIcon;
        public TextView mTitle;
        public TextView mBody;

        public ViewHolder(View v) {
            super(v);
            mIcon = v.findViewById(R.id.whats_new_item_icon);
            mTitle = v.findViewById(R.id.whats_new_item_title);
            mBody = v.findViewById(R.id.whats_new_item_body);
        }
    }
}