package com.texasgamer.zephyr.service.img;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DrawableModelLoader implements ModelLoader<ApplicationInfo, Drawable>
{
    private final Context mContext;

    public DrawableModelLoader(Context context ) {
        mContext = context;
    }

    @Nullable
    @Override
    public ModelLoader.LoadData<Drawable> buildLoadData(@NonNull ApplicationInfo applicationInfo, int width, int height, @NonNull Options options) {
        return new LoadData<>(new ObjectKey(applicationInfo.packageName), new DrawableDataFetcher(mContext, applicationInfo));
    }

    @Override
    public boolean handles(@NonNull ApplicationInfo applicationInfo) {
        return true;
    }
}
