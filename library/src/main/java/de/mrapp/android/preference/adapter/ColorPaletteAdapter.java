/*
 * Copyright 2014 - 2019 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.preference.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import de.mrapp.android.preference.AbstractColorPickerPreference.PreviewShape;
import de.mrapp.android.preference.multithreading.ColorPreviewDataBinder;
import de.mrapp.android.preference.view.ColorPaletteItem;
import de.mrapp.util.Condition;

/**
 * An adapter, which provides colors for visualization using a RecyclerView.
 *
 * @author Michael Rapp
 * @since 1.4.0
 */
public class ColorPaletteAdapter extends RecyclerView.Adapter<ColorPaletteAdapter.ViewHolder> {

    /**
     * The view holder, which is used by the adapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Creates a new view holder.
         *
         * @param itemView
         *         The view, the view holder corresponds to, as an instance of the class {@link
         *         ColorPaletteItem}. The view may not be null
         */
        public ViewHolder(@NonNull final ColorPaletteItem itemView) {
            super(itemView);
        }

    }

    /**
     * The color palette, which is provided by the adapter.
     */
    private final int[] colorPalette;

    /**
     * The data loader, which is used to asynchronously load the previews of colors.
     */
    private final ColorPreviewDataBinder previewLoader;

    /**
     * Creates a new adapter, which provides colors for visualization using a RecyclerView.
     *
     * @param colorPalette
     *         The color palette, which should be provided by the adapter, as an {@link Integer}
     *         array. The color palette may not be null
     * @param previewSize
     *         The size, which should be used to preview colors, as an {@link Integer} value in
     *         pixels. The size must be at least 1
     * @param previewShape
     *         The shape, which should be used to preview colors, as a value of the enum {@link
     *         PreviewShape}. The shape may not be null
     * @param previewBorderWidth
     *         The border width, which should be used to preview colors, as an {@link Integer}
     *         value. The border width must be at least 0
     * @param previewBorderColor
     *         The border color, which should be used to preview colors, as an {@link Integer}
     *         value
     * @param previewBackground
     *         The background, which should be used to preview colors, as an instance of the class
     *         {@link Drawable} or null, if no background should be shown
     */
    public ColorPaletteAdapter(@NonNull final Context context, @NonNull final int[] colorPalette,
                               final int previewSize, @NonNull final PreviewShape previewShape,
                               final int previewBorderWidth, @ColorInt final int previewBorderColor,
                               @Nullable final Drawable previewBackground) {
        Condition.INSTANCE.ensureNotNull(context, "The context may not be null");
        Condition.INSTANCE.ensureNotNull(colorPalette, "The color palette may not be null");
        Condition.INSTANCE.ensureAtLeast(previewSize, 1, "The preview size must be at least 1");
        Condition.INSTANCE.ensureNotNull(previewShape, "The preview shape may not be null");
        Condition.INSTANCE
                .ensureAtLeast(previewBorderWidth, 0, "The border width must be at least 0");
        this.colorPalette = colorPalette;
        this.previewLoader =
                new ColorPreviewDataBinder(context, previewBackground, previewShape, previewSize,
                        previewBorderWidth, previewBorderColor);
    }

    /**
     * Returns the index of a specific color.
     *
     * @param color
     *         The color, whose index should be returned, as an {@link Integer} value
     * @return The index of the given color or -1, if the adapter does not contain the color
     */
    public final int indexOf(@ColorInt final int color) {
        for (int i = 0; i < colorPalette.length; i++) {
            if (colorPalette[i] == color) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the color that corresponds to a specific position.
     *
     * @param position
     *         The position of the color, which should be returned, as an {@link Integer} value
     * @return The color that corresponds to the given position as an {@link Integer} value
     */
    public final int getItem(final int position) {
        return colorPalette[position];
    }

    @NonNull
    @Override
    public final ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                               final int viewType) {
        ColorPaletteItem view = new ColorPaletteItem(parent.getContext());
        return new ViewHolder(view);
    }

    @Override
    public final void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        int color = colorPalette[position];
        previewLoader.load(color, ((ColorPaletteItem) holder.itemView).getColorView());
    }

    @Override
    public final int getItemCount() {
        return colorPalette.length;
    }

    @Override
    public final long getItemId(final int position) {
        return colorPalette[position];
    }

}
