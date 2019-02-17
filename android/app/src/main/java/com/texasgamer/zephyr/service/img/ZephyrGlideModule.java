package com.texasgamer.zephyr.service.img;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import androidx.annotation.NonNull;

@GlideModule
public class ZephyrGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.prepend(ApplicationInfo.class, Drawable.class, new DrawableModelLoaderFactory(context));
    }
}