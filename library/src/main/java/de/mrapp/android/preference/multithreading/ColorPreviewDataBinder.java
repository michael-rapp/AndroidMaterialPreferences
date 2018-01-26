/*
 * Copyright 2014 - 2018 Michael Rapp
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
package de.mrapp.android.preference.multithreading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import de.mrapp.android.preference.AbstractColorPickerPreference.PreviewShape;
import de.mrapp.android.util.multithreading.AbstractDataBinder;

import static de.mrapp.android.util.BitmapUtil.clipCircle;
import static de.mrapp.android.util.BitmapUtil.clipSquare;
import static de.mrapp.android.util.BitmapUtil.drawableToBitmap;
import static de.mrapp.android.util.BitmapUtil.tile;
import static de.mrapp.android.util.BitmapUtil.tint;
import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A data loader, which allows to load the previews of colors.
 *
 * @author Michael Rapp
 * @since 1.7.0
 */
public class ColorPreviewDataBinder extends AbstractDataBinder<Bitmap, Integer, ImageView, Void> {

    /**
     * The background of the preview.
     */
    private Drawable background;

    /**
     * The shape of the preview.
     */
    private PreviewShape shape;

    /**
     * The size of the preview in pixels.
     */
    private int size;

    /**
     * The border width of the preview in pixels.
     */
    private int borderWidth;

    /**
     * The border color of the preview.
     */
    private int borderColor;

    /**
     * Creates a new data loader, which allows to create previews of colors.
     *
     * @param context
     *         The context, which should be used by the data loader, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param background
     *         The background of the preview as an instance of the class {@link Drawable} or null,
     *         if no background should be shown
     * @param shape
     *         The shape of the preview as a value of the enum {@link PreviewShape}. The shape may
     *         not be null
     * @param size
     *         The size of the preview as an {@link Integer} value in pixels. The size must be at
     *         least 1
     * @param borderWidth
     *         The border width of the preview as an {@link Integer} value in pixels. The border
     *         width must be at least 0
     * @param borderColor
     *         The border color of the preview as an {@link Integer} value
     */
    public ColorPreviewDataBinder(@NonNull final Context context,
                                  @Nullable final Drawable background,
                                  @NonNull final PreviewShape shape, final int size,
                                  final int borderWidth, @ColorInt final int borderColor) {
        super(context);
        setBackground(background);
        setShape(shape);
        setSize(size);
        setBorderWidth(borderWidth);
        setBorderColor(borderColor);
    }

    /**
     * Returns the background of the preview.
     *
     * @return The background of the preview as an instance of the class {@link Drawable}
     */
    public final Drawable getBackground() {
        return background;
    }

    /**
     * Sets the background of the preview.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Drawable} or
     *         null, if no background should be shown
     */
    public final void setBackground(@Nullable final Drawable background) {
        this.background = background;
    }

    /**
     * Returns the shape of the preview.
     *
     * @return The shape of the preview as a value of the enum {@link PreviewShape}. The shape may
     * either be <code>CIRCLE</code> or <code>SQUARE</code>
     */
    public final PreviewShape getShape() {
        return shape;
    }

    /**
     * Sets the shape of the preview.
     *
     * @param shape
     *         The shape, which should be set, as a value of the enum {@link PreviewShape}. The
     *         shape may not be null
     */
    public final void setShape(@NonNull final PreviewShape shape) {
        ensureNotNull(shape, "The shape may not be null");
        this.shape = shape;
    }

    /**
     * Returns the size of the preview.
     *
     * @return The size of the preview as an {@link Integer} value in pixels
     */
    public final int getSize() {
        return size;
    }

    /**
     * Sets the size of the preview.
     *
     * @param size
     *         The size, which should be set, as an {@link Integer} value in pixels. The size must
     *         be at least 1
     */
    public final void setSize(final int size) {
        ensureAtLeast(size, 1, "The size must be at least 1");
        this.size = size;
    }

    /**
     * Returns the border width of the preview.
     *
     * @return The border width of the preview as an {@link Integer} value in pixels
     */
    public final int getBorderWidth() {
        return borderWidth;
    }

    /**
     * Sets the border width of the preview.
     *
     * @param borderWidth
     *         The border width, which should be set, as an {@link Integer} value in pixels. The
     *         border width must be at least 0
     */
    public final void setBorderWidth(final int borderWidth) {
        ensureAtLeast(borderWidth, 0, "The border width must be at least 0");
        this.borderWidth = borderWidth;
    }

    /**
     * Returns the border color of the preview.
     *
     * @return The border color of the preview as an {@link Integer} value
     */
    public final int getBorderColor() {
        return borderColor;
    }

    /**
     * Sets the border color of the preview.
     *
     * @param borderColor
     *         The border color, which should be set, as an {@link Integer} value
     */
    public final void setBorderColor(@ColorInt final int borderColor) {
        this.borderColor = borderColor;
    }

    @Nullable
    @Override
    protected final Bitmap doInBackground(@NonNull final Integer key,
                                          @NonNull final Void... params) {
        Bitmap preview;

        if (getBackground() != null) {
            Bitmap tile = drawableToBitmap(getBackground());
            preview = tile(tile, getSize(), getSize());
        } else {
            preview = Bitmap.createBitmap(getSize(), getSize(), Bitmap.Config.ARGB_8888);
        }

        preview = tint(preview, key);

        if (getShape() == PreviewShape.CIRCLE) {
            return clipCircle(preview, getSize(), getBorderWidth(), getBorderColor());
        } else {
            return clipSquare(preview, getSize(), getBorderWidth(), getBorderColor());
        }
    }

    @Override
    protected final void onPostExecute(@NonNull final ImageView view, @Nullable final Bitmap data,
                                       final long duration, @NonNull final Void... params) {
        view.setImageBitmap(data);
    }

}