package com.texasgamer.zephyr.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.databinding.FragmentNotificationsBinding;
import com.texasgamer.zephyr.model.ZephyrNotificationPreference;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.viewmodel.ManageNotificationsViewModel;

/**
 * Notifications fragment.
 */
public class NotificationsFragment extends BaseFragment<ManageNotificationsViewModel, FragmentNotificationsBinding> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupFastScroll();

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
        RecyclerView.Adapter appListAdapter = mViewModel.getAdapter();
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
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @NonNull
    @Override
    protected Class<ManageNotificationsViewModel> getViewModelClass() {
        return ManageNotificationsViewModel.class;
    }

    @Override
    protected void setViewBindings(@NonNull View view) {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected WindowInsets onApplyWindowInsets(@NonNull View view, @NonNull WindowInsets windowInsets) {
        super.onApplyWindowInsets(view, windowInsets);
        mDataBinding.appList.setClipToPadding(false);
        mDataBinding.appList.setPadding(0, 0, 0, windowInsets.getSystemWindowInsetBottom());
        mDataBinding.fastScroller.setPadding(0, 0, windowInsets.getSystemWindowInsetRight(), 0);
        return windowInsets;
    }

    private void setupFastScroll() {
        mDataBinding.fastScroller.setupWithRecyclerView(
                mDataBinding.appList,
                (position) -> {
                    ZephyrNotificationPreference item = mViewModel.getAdapter().getItem(position);
                    if (TextUtils.isDigitsOnly(item.getTitle().substring(0, 1))) {
                        return new FastScrollItemIndicator.Text("#");
                    }

                    return new FastScrollItemIndicator.Text(item.getTitle().substring(0, 1).toUpperCase());
                }
        );

        mDataBinding.fastScrollerThumb.setupWithFastScroller(mDataBinding.fastScroller);
    }
}
