package com.texasgamer.zephyr.service.threading;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

public class ZephyrExecutors {

    private static final Executor DISK_EXECUTOR;
    private static final Executor NETWORK_EXECUTOR;
    private static final Executor MAIN_THREAD_EXECUTOR;

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
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
