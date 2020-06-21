package com.texasgamer.zephyr.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.model.ZephyrNotificationPreference;
import com.texasgamer.zephyr.util.threading.ZephyrExecutors;
import com.texasgamer.zephyr.view.NotificationPreferenceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Notification preference list adapter.
 */
public class NotificationPreferenceListAdapter extends RecyclerView.Adapter<NotificationPreferenceListAdapter.NotificationPreferenceListViewHolder> {

    private List<? extends ZephyrNotificationPreference> mPrefs;
    private NotificationPreferenceView.OnPreferenceChangeListener mOnPrefChangeListener;

    public NotificationPreferenceListAdapter(@Nullable NotificationPreferenceView.OnPreferenceChangeListener onPreferenceChangeListener) {
        mPrefs = new ArrayList<>();
        mOnPrefChangeListener = onPreferenceChangeListener;

        setHasStableIds(true);
    }

    @Override
    @NonNull
    public NotificationPreferenceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationPreferenceView itemView = new NotificationPreferenceView(parent.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        itemView.setLayoutParams(layoutParams);

        if (mOnPrefChangeListener != null) {
            itemView.setOnPrefChangeListener(mOnPrefChangeListener);
        }

        return new NotificationPreferenceListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationPreferenceListViewHolder holder, int position) {
        holder.getView().setNotificationPreference(mPrefs.get(position));
    }

    public void setNotificationPreferences(@NonNull List<? extends ZephyrNotificationPreference> notificationPreferences) {
        if (mPrefs == null) {
            mPrefs = notificationPreferences;
            notifyItemRangeInserted(0, notificationPreferences.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mPrefs.size();
                }

                @Override
                public int getNewListSize() {
                    return notificationPreferences.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mPrefs.get(oldItemPosition).getPackageName().equals(notificationPreferences.get(newItemPosition).getPackageName());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ZephyrNotificationPreference newProduct = notificationPreferences.get(newItemPosition);
                    ZephyrNotificationPreference oldProduct = mPrefs.get(oldItemPosition);
                    return newProduct.getPackageName().equals(oldProduct.getPackageName())
                            && newProduct.isEnabled() == oldProduct.isEnabled();
                }
            });

            mPrefs = notificationPreferences;
            result.dispatchUpdatesTo(this);

            ZephyrExecutors.getDiskExecutor().execute(() -> {
                for (ZephyrNotificationPreference preference : mPrefs) {
                    if (preference.getIcon() == null) {
                        preference.setIcon(ZephyrApplication.getApplicationComponent().applicationUtilities().getAppIcon(preference.getPackageName()));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPrefs.size();
    }

    @Override
    public long getItemId(int position) {
        return mPrefs.get(position).getPackageName().hashCode();
    }

    @NonNull
    public ZephyrNotificationPreference getItem(int index) {
        return mPrefs.get(index);
    }

    /**
     * Notification setting list view holder.
     */
    static class NotificationPreferenceListViewHolder extends RecyclerView.ViewHolder {

        private NotificationPreferenceView mView;

        NotificationPreferenceListViewHolder(@NonNull View v) {
            super(v);
            mView = (NotificationPreferenceView) v;
        }

        public NotificationPreferenceView getView() {
            return mView;
        }
    }
}
