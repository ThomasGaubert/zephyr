package com.texasgamer.zephyr.util.threading;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

/**
 * Provides executors for threaded operations.
 */
public final class ZephyrExecutors {

    private static final Executor DISK_EXECUTOR;
    private static final Executor NETWORK_EXECUTOR;
    private static final Executor MAIN_THREAD_EXECUTOR;

    private ZephyrExecutors() {
    }

    static {
        DISK_EXECUTOR = Executors.newFixedThreadPool(3);
        NETWORK_EXECUTOR = Executors.newFixedThreadPool(3);
        MAIN_THREAD_EXECUTOR = new MainThreadExecutor();
    }

    public static Executor getDiskExecutor() {
        return DISK_EXECUTOR;
    }

    public static Executor getNetworkExecutor() {
        return NETWORK_EXECUTOR;
    }

    public static Executor getMainThreadExecutor() {
        return MAIN_THREAD_EXECUTOR;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mMainThreadHandler.post(command);
        }
    }
}
