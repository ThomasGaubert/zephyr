package com.texasgamer.zephyr.service.img;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.texasgamer.zephyr.R;

import androidx.annotation.NonNull;

public class DrawableDataFetcher implements DataFetcher<Drawable> {
    private final ApplicationInfo mModel;
    private final Context mContext;

    public DrawableDataFetcher(Context context, ApplicationInfo model ) {
        mModel = model;
        mContext = context;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super Drawable> callback) {
        Drawable icon;

        if (mModel != null) {
            icon = mContext.getPackageManager().getApplicationIcon(mModel);
        } else {
            icon = mContext.getDrawable(R.drawable.ic_app_icon_unknown);
        }

        callback.onDataReady(icon);
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void cancel() {
    }

    @NonNull
    @Override
    public Class<Drawable> getDataClass() {
        return Drawable.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
}
