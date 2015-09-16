/*
 * AndroidMaterialPreferences Copyright 2014 - 2015 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>. 
 */
package de.mrapp.android.preference.multithreading;

import static de.mrapp.android.preference.util.Condition.ensureAtLeast;
import static de.mrapp.android.preference.util.Condition.ensureNotNull;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import de.mrapp.android.preference.AbstractColorPickerPreference.PreviewShape;
import de.mrapp.android.preference.util.BitmapUtil;

/**
 * A data loader, which allows to load the previews of colors.
 * 
 * @author Michael Rapp
 *
 * @since 1.7.0
 */
public class ColorPreviewLoader extends AbstractDataLoader<Bitmap, Integer, ImageView, Void> {

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
	 *            The context, which should be used by the data loader, as an
	 *            instance of the class {@link Context}. The context may not be
	 *            null
	 * @param background
	 *            The background of the preview as an instance of the class
	 *            {@link Drawable} or null, if no background should be shown
	 * @param shape
	 *            The shape of the preview as a value of the enum
	 *            {@link PreviewShape}. The shape may not be null
	 * @param size
	 *            The size of the preview as an {@link Integer} value in pixels.
	 *            The size must be at least 1
	 * @param borderWidth
	 *            The border width of the preview as an {@link Integer} value in
	 *            pixels. The border width must be at least 0
	 * @param borderColor
	 *            The border color of the preview as an {@link Integer} value
	 */
	public ColorPreviewLoader(final Context context, final Drawable background, final PreviewShape shape,
			final int size, final int borderWidth, final int borderColor) {
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
	 * @return The background of the preview as an instance of the class
	 *         {@link Drawable}
	 */
	public final Drawable getBackground() {
		return background;
	}

	/**
	 * Sets the background of the preview.
	 * 
	 * @param background
	 *            The background, which should be set, as an instance of the
	 *            class {@link Drawable} or null, if no background should be
	 *            shown
	 */
	public final void setBackground(final Drawable background) {
		this.background = background;
	}

	/**
	 * Returns the shape of the preview.
	 * 
	 * @return The shape of the preview as a value of the enum
	 *         {@link PreviewShape}. The shape may either be <code>CIRCLE</code>
	 *         or <code>SQUARE</code>
	 */
	public final PreviewShape getShape() {
		return shape;
	}

	/**
	 * Sets the shape of the preview.
	 * 
	 * @param shape
	 *            The shape, which should be set, as a value of the enum
	 *            {@link PreviewShape}. The shape may not be null
	 */
	public final void setShape(final PreviewShape shape) {
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
	 *            The size, which should be set, as an {@link Integer} value in
	 *            pixels. The size must be at least 1
	 */
	public final void setSize(final int size) {
		ensureAtLeast(size, 1, "The size must be at least 1");
		this.size = size;
	}

	/**
	 * Returns the border width of the preview.
	 * 
	 * @return The border width of the preview as an {@link Integer} value in
	 *         pixels
	 */
	public final int getBorderWidth() {
		return borderWidth;
	}

	/**
	 * Sets the border width of the preview.
	 * 
	 * @param borderWidth
	 *            The border width, which should be set, as an {@link Integer}
	 *            value in pixels. The border width must be at least 0
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
	 *            The border color, which should be set, as an {@link Integer}
	 *            value
	 */
	public final void setBorderColor(final int borderColor) {
		this.borderColor = borderColor;
	}

	@Override
	protected final Bitmap loadData(final Integer color, final Void... params) {
		Bitmap preview = null;

		if (getBackground() != null) {
			Bitmap tile = BitmapUtil.convertDrawableToBitmap(getBackground());
			preview = BitmapUtil.createTiledBitmap(tile, getSize(), getSize());
		} else {
			preview = Bitmap.createBitmap(getSize(), getSize(), Bitmap.Config.ARGB_8888);
		}

		preview = BitmapUtil.tint(preview, color);

		if (getShape() == PreviewShape.CIRCLE) {
			return BitmapUtil.clipCircle(preview, getSize(), getBorderWidth(), getBorderColor());
		} else {
			return BitmapUtil.clipSquare(preview, getSize(), getBorderWidth(), getBorderColor());
		}
	}

	@Override
	protected final void showData(final ImageView imageView, final Bitmap preview) {
		imageView.setImageBitmap(preview);
	}

}
