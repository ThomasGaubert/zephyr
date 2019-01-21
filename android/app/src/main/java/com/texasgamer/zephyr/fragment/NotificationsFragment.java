package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.adapter.NotificationSettingListAdapter;
import com.texasgamer.zephyr.db.entity.NotificationPreferenceEntity;
import com.texasgamer.zephyr.view.NotificationPreferenceView;
import com.texasgamer.zephyr.viewmodel.ManageNotificationsViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class NotificationsFragment extends BaseFragment<ManageNotificationsViewModel> implements NotificationPreferenceView.OnPreferenceChangeListener {

    @BindView(R.id.app_list)
    RecyclerView appList;

    private NotificationSettingListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        subscribeUi(mViewModel.getNotificationPreferences());
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
        // Update the list when the data changes
        liveData.observe(this, new Observer<List<NotificationPreferenceEntity>>() {
            @Override
            public void onChanged(@Nullable List<NotificationPreferenceEntity> preferences) {
                if (preferences != null) {
                    // TODO: Hide loading spinner
                    mAdapter.setNotificationPreferences(preferences);
                } else {
                    // TODO: Loading spinner
                }
            }
        });
    }
}
