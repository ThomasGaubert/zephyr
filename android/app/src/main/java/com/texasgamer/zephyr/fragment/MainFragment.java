package com.texasgamer.zephyr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.texasgamer.zephyr.BuildConfig;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.ZephyrApplication;
import com.texasgamer.zephyr.adapter.ZephyrCardViewPagerAdapter;
import com.texasgamer.zephyr.databinding.FragmentMainBinding;
import com.texasgamer.zephyr.model.DismissNotificationPayload;
import com.texasgamer.zephyr.model.NotificationPayload;
import com.texasgamer.zephyr.provider.IZephyrCardProvider;
import com.texasgamer.zephyr.util.ImageUtils;
import com.texasgamer.zephyr.util.eventbus.EventBusEvent;
import com.texasgamer.zephyr.util.log.ILogger;
import com.texasgamer.zephyr.util.log.LogLevel;
import com.texasgamer.zephyr.util.preference.PreferenceKeys;
import com.texasgamer.zephyr.util.threading.ZephyrExecutors;
import com.texasgamer.zephyr.viewmodel.MainFragmentViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * Main fragment.
 */
public class MainFragment extends BaseFragment<MainFragmentViewModel, FragmentMainBinding> {

    private static final String LOG_TAG = "MainFragment";

    @Inject
    ILogger logger;
    @Inject
    IZephyrCardProvider zephyrCardProvider;

    private ZephyrCardViewPagerAdapter mZephyrCardViewPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mZephyrCardViewPagerAdapter = new ZephyrCardViewPagerAdapter(requireContext(),
                zephyrCardProvider.getCards(requireContext(), mLayoutManager, mNavigationManager));
        mDataBinding.mainCarousel.setAdapter(mZephyrCardViewPagerAdapter);
        mDataBinding.mainCarousel.setOffscreenPageLimit(5);

        checkForWhatsNew();
    }

    @Override
    public void onResume() {
        super.onResume();
        mZephyrCardViewPagerAdapter.setItems(zephyrCardProvider.getCards(requireContext(), mLayoutManager, mNavigationManager));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    @LayoutRes
    protected int getFragmentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void injectDependencies() {
        ZephyrApplication.getApplicationComponent().inject(this);
    }

    @NonNull
    @Override
    protected Class<MainFragmentViewModel> getViewModelClass() {
        return MainFragmentViewModel.class;
    }

    @Override
    protected void setViewBindings(@NonNull View view) {
        mDataBinding.setViewModel(mViewModel);
    }

    @Subscribe
    public void onEvent(@Nullable String eventPayload) {
        if (EventBusEvent.SHELL_REFRESH_CARDS.equals(eventPayload)) {
            mZephyrCardViewPagerAdapter.setItems(zephyrCardProvider.getCards(requireContext(), mLayoutManager, mNavigationManager));
        } else if (EventBusEvent.SERVICE_NOTIFICATION_STARTED.equals(eventPayload)
                && getActivity() != null
                && !ZephyrApplication.getInstance().isInForeground()) {
            logger.log(LogLevel.INFO, LOG_TAG, "Notification service started, bringing MainFragment to foreground...");
            Intent intent = new Intent(getContext(), getActivity().getClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @OnClick(R.id.test_notification_button)
    void sendTestNotification() {
        ZephyrExecutors.getDiskExecutor().execute(() -> {
            NotificationPayload notificationPayload = new NotificationPayload();
            notificationPayload.packageName = BuildConfig.APPLICATION_ID;
            notificationPayload.id = -1;
            notificationPayload.title = getString(R.string.test_notification_title);
            notificationPayload.body = getString(R.string.test_notification_message);
            notificationPayload.icon = ImageUtils.bitmapToBase64(ImageUtils.drawableToBitmap(getResources().getDrawable(R.drawable.ic_notifications)));

            logger.log(LogLevel.INFO, LOG_TAG, "Sending test notification");
            EventBus.getDefault().post(notificationPayload);
        });

        new Handler().postDelayed(() -> {
            DismissNotificationPayload dismissNotificationPayload = new DismissNotificationPayload();
            dismissNotificationPayload.packageName = BuildConfig.APPLICATION_ID;
            dismissNotificationPayload.id = -1;

            logger.log(LogLevel.INFO, LOG_TAG, "Dismissing test notification...");
            EventBus.getDefault().post(dismissNotificationPayload);
        }, 5000);
    }

    private void checkForWhatsNew() {
        int lastSeenWhatsNewVersion = mPreferenceManager.getInt(PreferenceKeys.PREF_LAST_SEEN_WHATS_NEW_VERSION);
        if (lastSeenWhatsNewVersion < Constants.WHATS_NEW_VERSION) {
            logger.log(LogLevel.VERBOSE, LOG_TAG, "Showing What's new for %d (last seen %d)",
                    Constants.WHATS_NEW_VERSION, lastSeenWhatsNewVersion);
            mNavigationManager.navigate(R.id.action_fragment_main_to_fragment_whats_new);
        }
    }
}
