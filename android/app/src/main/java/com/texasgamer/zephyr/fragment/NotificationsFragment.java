package com.texasgamer.zephyr.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import com.texasgamer.zephyr.model.ZephyrNotificationPreference;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
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
    @BindView(R.id.no_results_found)
    TextView mNoResultsFound;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setupEdgeToEdgeLayout();
        }

        if (!mPreferenceManager.getBoolean(PreferenceKeys.PREF_SEEN_MANAGE_NOTIFICATIONS)) {
            mPreferenceManager.putBoolean(PreferenceKeys.PREF_SEEN_MANAGE_NOTIFICATIONS, true);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.manage_notifications, menu);
        MenuItem searchAction = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchAction.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mViewModel.setSearchQuery(query);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RecyclerView.Adapter appListAdapter = mAppList.getAdapter();
        switch (item.getItemId()) {
            case R.id.action_notif_enable_all:
                mViewModel.enableAll();
                if (appListAdapter != null) {
                    appListAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.action_notif_disable_all:
                mViewModel.disableAll();
                if (appListAdapter != null) {
                    appListAdapter.notifyDataSetChanged();
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    @LayoutRes
    protected int getFragmentLayout() {
        return R.layout.fragment_notifications;
    }

    @Override
    protected void setViewBindings(@NonNull View view) {

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

    private void subscribeUi(LiveData<List<ZephyrNotificationPreference>> liveData) {
        liveData.observe(getViewLifecycleOwner(), preferences -> {
            if (preferences != null && !preferences.isEmpty()) {
                // Show results
                mAdapter.setNotificationPreferences(preferences);
                mSpinner.setVisibility(View.GONE);
                mAppList.setVisibility(View.VISIBLE);
                mFastScrollerThumbView.setVisibility(View.VISIBLE);
                mFastScrollerView.setVisibility(View.VISIBLE);
                mNoResultsFound.setVisibility(View.GONE);
            } else if (mViewModel.getSearchQuery().getValue() != null
                    && !mViewModel.getSearchQuery().getValue().isEmpty()) {
                // Show no result found
                mAppList.setVisibility(View.GONE);
                mFastScrollerThumbView.setVisibility(View.GONE);
                mFastScrollerView.setVisibility(View.GONE);
                mSpinner.setVisibility(View.GONE);
                mNoResultsFound.setVisibility(View.VISIBLE);
            } else {
                // Show spinner
                mAppList.setVisibility(View.GONE);
                mFastScrollerThumbView.setVisibility(View.GONE);
                mFastScrollerView.setVisibility(View.GONE);
                mSpinner.setVisibility(View.VISIBLE);
                mNoResultsFound.setVisibility(View.GONE);
            }
        });
    }

    private void setupFastScroll() {
        mFastScrollerView.setupWithRecyclerView(
                mAppList,
                (position) -> {
                    ZephyrNotificationPreference item = mAdapter.getItem(position);
                    if (TextUtils.isDigitsOnly(item.getTitle().substring(0, 1))) {
                        return new FastScrollItemIndicator.Text("#");
                    }

                    return new FastScrollItemIndicator.Text(item.getTitle().substring(0, 1).toUpperCase());
                }
        );

        mFastScrollerThumbView.setupWithFastScroller(mFastScrollerView);
    }

    private void setupEdgeToEdgeLayout() {
        mAppList.setOnApplyWindowInsetsListener((v, insets) -> {
            mAppList.setClipToPadding(false);
            mAppList.setPadding(0, 0, 0, insets.getSystemWindowInsetBottom());
            mFastScrollerView.setPadding(0, 0, insets.getSystemWindowInsetRight(), 0);
            return insets;
        });
    }
}
