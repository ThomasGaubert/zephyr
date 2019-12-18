package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.adapter.NotificationPreferenceListAdapter;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.model.NotificationPreference;
import com.texasgamer.zephyr.view.NotificationPreferenceView;
import com.texasgamer.zephyr.viewmodel.ManageNotificationsViewModel;

import java.util.List;

import butterknife.BindView;

/**
 * Notifications fragment.
 */
public class NotificationsFragment extends BaseFragment<ManageNotificationsViewModel, ViewDataBinding> implements NotificationPreferenceView.OnPreferenceChangeListener {

    @BindView(R.id.spinner)
    ProgressBar mSpinner;
    @BindView(R.id.fast_scroller)
    FastScrollerView mFastScrollerView;
    @BindView(R.id.fast_scroller_thumb)
    FastScrollerThumbView mFastScrollerThumbView;
    @BindView(R.id.app_list)
    RecyclerView mAppList;

    private NotificationPreferenceListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new NotificationPreferenceListAdapter(this);

        mAppList.setHasFixedSize(true);
        mAppList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAppList.setAdapter(mAdapter);

        subscribeUi(mViewModel.getNotificationPreferences());
        setupFastScroll();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notif_enable_all:
                mViewModel.enableAll();
                mAppList.getAdapter().notifyDataSetChanged();
                return true;
            case R.id.action_notif_disable_all:
                mViewModel.disableAll();
                mAppList.getAdapter().notifyDataSetChanged();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_notifications;
    }

    @Override
    protected void setViewBindings(View view) {

    }

    @Override
    protected ManageNotificationsViewModel onCreateViewModel() {
        return new ManageNotificationsViewModel(ZephyrApplication.getInstance());
    }

    @Override
    public void onPreferenceChange(@NonNull String packageName, boolean newValue) {
        mViewModel.updateNotificationPreference(packageName, newValue);
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    private void subscribeUi(LiveData<List<NotificationPreferenceEntity>> liveData) {
        liveData.observe(this, preferences -> {
            if (preferences != null) {
                mAdapter.setNotificationPreferences(preferences);
                mSpinner.setVisibility(View.GONE);
                mAppList.setVisibility(View.VISIBLE);
                mFastScrollerThumbView.setVisibility(View.VISIBLE);
                mFastScrollerView.setVisibility(View.VISIBLE);
            } else {
                mAppList.setVisibility(View.GONE);
                mFastScrollerThumbView.setVisibility(View.GONE);
                mFastScrollerView.setVisibility(View.GONE);
                mSpinner.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupFastScroll() {
        mFastScrollerView.setupWithRecyclerView(
                mAppList,
                (position) -> {
                    NotificationPreference item = mAdapter.getItem(position);
                    if (TextUtils.isDigitsOnly(item.getTitle().substring(0, 1))) {
                        return new FastScrollItemIndicator.Text("#");
                    }

                    return new FastScrollItemIndicator.Text(item.getTitle().substring(0, 1).toUpperCase());
                }
        );

        mFastScrollerThumbView.setupWithFastScroller(mFastScrollerView);
    }
}
