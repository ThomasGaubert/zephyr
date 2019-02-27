package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.adapter.NotificationSettingListAdapter;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.model.NotificationPreference;
import com.texasgamer.zephyr.service.threading.ZephyrExecutors;
import com.texasgamer.zephyr.util.ApplicationUtils;
import com.texasgamer.zephyr.view.NotificationPreferenceView;
import com.texasgamer.zephyr.viewmodel.ManageNotificationsViewModel;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class NotificationsFragment extends BaseFragment<ManageNotificationsViewModel> implements NotificationPreferenceView.OnPreferenceChangeListener {

    @BindView(R.id.spinner)
    ProgressBar spinner;
    @BindView(R.id.fast_scroller)
    FastScrollerView fastScrollerView;
    @BindView(R.id.fast_scroller_thumb)
    FastScrollerThumbView fastScrollerThumbView;
    @BindView(R.id.app_list)
    RecyclerView appList;

    @Inject
    ApplicationUtils applicationUtils;

    private NotificationSettingListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subscribeUi(mViewModel.getNotificationPreferences());
        setupFastScroll();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notif_enable_all:
                mViewModel.enableAll();
                appList.getAdapter().notifyDataSetChanged();
                return true;
            case R.id.action_notif_disable_all:
                mViewModel.disableAll();
                appList.getAdapter().notifyDataSetChanged();
                return true;
        }
        return false;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_notifications;
    }

    @Override
    protected void setViewBindings(View view) {
        mAdapter = new NotificationSettingListAdapter(this);

        appList.setHasFixedSize(true);
        appList.setLayoutManager(new LinearLayoutManager(getContext()));
        appList.setAdapter(mAdapter);
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
        liveData.observe(this, new Observer<List<NotificationPreferenceEntity>>() {
            @Override
            public void onChanged(@Nullable List<NotificationPreferenceEntity> preferences) {
                if (preferences != null) {
                    mAdapter.setNotificationPreferences(preferences);
                    spinner.setVisibility(View.GONE);
                    appList.setVisibility(View.VISIBLE);
                    fastScrollerThumbView.setVisibility(View.VISIBLE);
                    fastScrollerView.setVisibility(View.VISIBLE);
                } else {
                    appList.setVisibility(View.GONE);
                    fastScrollerThumbView.setVisibility(View.GONE);
                    fastScrollerView.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setupFastScroll() {
        fastScrollerView.setupWithRecyclerView(
                appList,
                (position) -> {
                    NotificationPreference item = mAdapter.getItem(position);
                    if (TextUtils.isDigitsOnly(item.getTitle().substring(0, 1))) {
                        return new FastScrollItemIndicator.Text("#");
                    }

                    return new FastScrollItemIndicator.Text(item.getTitle().substring(0, 1).toUpperCase());
                }
        );

        fastScrollerThumbView.setupWithFastScroller(fastScrollerView);
    }
}