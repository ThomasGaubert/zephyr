package com.texasgamer.zephyr.binding;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link BindingAdapter}s for {@link RecyclerView}.
 */
public final class RecyclerViewBindingAdapters {

    private RecyclerViewBindingAdapters() {
    }

    @BindingAdapter("adapter")
    public static void setAdapter(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("layoutManager")
    public static void setOffscreenPageLimit(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    @BindingAdapter("hasFixedSize")
    public static void setOffscreenPageLimit(@NonNull RecyclerView recyclerView, boolean hasFixedSize) {
        recyclerView.setHasFixedSize(hasFixedSize);
    }
}
