/*
 * AndroidMaterialPreferences Copyright 2014 - 2015 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package de.mrapp.android.preference.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import de.mrapp.android.preference.AbstractColorPickerPreference.PreviewShape;
import de.mrapp.android.preference.R;
import de.mrapp.android.preference.multithreading.ColorPreviewLoader;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An adapter, which provides colors for visualization using a {@link GridView} widget.
 *
 * @author Michael Rapp
 * @since 1.4.0
 */
public class ColorPaletteAdapter extends BaseAdapter {

    /**
     * The view holder, which is used by the adapter.
     */
    private class ViewHolder {

        /**
         * The image view, which is used to visualize a color.
         */
        private ImageView colorView;

    }

    /**
     * The context, which is used by the adapter.
     */
    private final Context context;

    /**
     * The color palette, which is provided by the adapter.
     */
    private final int[] colorPalette;

    /**
     * The data loader, which is used to asynchronously load the previews of colors.
     */
    private final ColorPreviewLoader previewLoader;

    /**
     * Inflates the view, which is used to visualize a color and initializes the corresponding view
     * holder.
     *
     * @param parent
     *         The parent of the view, which should be inflated, as an instance of the class {@link
     *         ViewGroup} or null, if the view does not have a parent
     * @return The view, which has been inflated, as an instance of the class {@link View}
     */
    private View inflateView(@Nullable final ViewGroup parent) {
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.color_palette_item, parent, false);
        ImageView colorView = (ImageView) view.findViewById(R.id.color_view);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.colorView = colorView;
        view.setTag(viewHolder);
        return view;
    }

    /**
     * Creates a new adapter, which provides colors for visualization using a {@link GridView}
     * widget.
     *
     * @param context
     *         The context, which should be used by the adapter, as an instance of the class {@link
     *         Context}. The context may not be null
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
                               final int previewSize, final @NonNull PreviewShape previewShape,
                               final int previewBorderWidth, @ColorInt final int previewBorderColor,
                               @Nullable final Drawable previewBackground) {
        ensureNotNull(context, "The context may not be null");
        ensureNotNull(colorPalette, "The color palette may not be null");
        ensureAtLeast(previewSize, 1, "The preview size must be at least 1");
        ensureNotNull(previewShape, "The preview shape may not be null");
        ensureAtLeast(previewBorderWidth, 0, "The border width must be at least 0");
        this.context = context;
        this.colorPalette = colorPalette;
        this.previewLoader =
                new ColorPreviewLoader(context, previewBackground, previewShape, previewSize,
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

    @Override
    public final int getCount() {
        return colorPalette.length;
    }

    @Override
    public final Integer getItem(final int position) {
        return colorPalette[position];
    }

    @Override
    public final long getItemId(final int position) {
        return position;
    }

    @Override
    public final View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflateView(parent);
        }

        int color = getItem(position);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        previewLoader.load(color, viewHolder.colorView);

        return view;
    }

}