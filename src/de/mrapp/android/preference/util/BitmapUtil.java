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
package de.mrapp.android.preference.util;

import static de.mrapp.android.preference.util.Condition.ensureAtLeast;
import static de.mrapp.android.preference.util.Condition.ensureNotNull;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * An utility class, which provides various methods for editing bitmaps.
 * 
 * @author Michael Rapp
 * 
 * @since 1.4.0
 */
public final class BitmapUtil {

	/**
	 * The angle of an arc, when it should be painted completely.
	 */
	private static final int COMPLETE_ARC_ANGLE = 360;

	/**
	 * Clips the corners of a bitmap in order to transform it into a round shape
	 * and resizes it to a specific size. Bitmaps, whose width and height are
	 * not equal, will be clipped to a square beforehand.
	 * 
	 * @param bitmap
	 *            The bitmap, which should be clipped, as an instance of the
	 *            class {@link Bitmap}. The bitmap may not be null
	 * @param size
	 *            The size, the bitmap should be resized to, as an
	 *            {@link Integer} value in pixels. The size must be at least 1
	 * @return The clipped bitmap as an instance of the class {@link Bitmap}
	 */
	private static Bitmap clipCircle(final Bitmap bitmap, final int size) {
		Bitmap squareBitmap = clipSquare(bitmap, size);
		int squareSize = squareBitmap.getWidth();
		float radius = squareSize / 2.0f;
		Bitmap clippedBitmap = Bitmap.createBitmap(squareSize, squareSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(clippedBitmap);
		Paint paint = new Paint();
		paint.setFilterBitmap(false);
		paint.setAntiAlias(true);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		Path path = new Path();
		path.addCircle(radius, radius, radius, Path.Direction.CCW);
		canvas.clipPath(path);
		canvas.drawBitmap(squareBitmap, new Rect(0, 0, squareSize, squareSize), new Rect(0, 0, squareSize, squareSize),
				paint);
		return clippedBitmap;
	}

	/**
	 * Clips the long edge of a bitmap, if its width and height are not equal,
	 * in order to transform it into a square and resizes it to a specific size.
	 * 
	 * @param bitmap
	 *            The bitmap, which should be clipped, as an instance of the
	 *            class {@link Bitmap}. The bitmap may not be null
	 * @param size
	 *            The size, the bitmap should be resized to, as an
	 *            {@link Integer} value in pixels. The size must be at least 1
	 * @return The clipped bitmap as an instance of the class {@link Bitmap}
	 */
	private static Bitmap clipSquare(final Bitmap bitmap, final int size) {
		ensureNotNull(bitmap, "The bitmap may not be null");
		Bitmap clippedBitmap = bitmap;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		if (width > height) {
			clippedBitmap = Bitmap.createBitmap(bitmap, width / 2 - height / 2, 0, height, height);
		} else if (bitmap.getWidth() < bitmap.getHeight()) {
			clippedBitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2 - width / 2, width, width);
		}

		if (size != -1 && clippedBitmap.getWidth() != size) {
			clippedBitmap = resize(clippedBitmap, size, size);
		}

		return clippedBitmap;
	}

	/**
	 * Resizes a bitmap to a specific width and height. If the ratio between
	 * width and height differs from the bitmap's original ration, the bitmap
	 * will be stretched.
	 * 
	 * @param bitmap
	 *            The bitmap, which should be resized, as an instance of the
	 *            class {@link Bitmap}. The bitmap may not be null
	 * @param width
	 *            The width, the bitmap should be resized to, as an
	 *            {@link Integer} value in pixels. The width must be at least 1
	 * @param height
	 *            The height, the bitmap should be resized to, as an
	 *            {@link Integer} value in pixels. The height must be at least 1
	 * @return The resized bitmap as an instance of the class {@link Bitmap}
	 */
	private static Bitmap resize(final Bitmap bitmap, final int width, final int height) {
		ensureNotNull(bitmap, "The bitmap may not be null");
		ensureAtLeast(width, 1, "The width must be at least 1");
		ensureAtLeast(height, 1, "The height must be at least 1");
		return Bitmap.createScaledBitmap(bitmap, width, height, false);
	}

	/**
	 * Creates a new utility class, which provides various methods for editing
	 * bitmaps.
	 */
	private BitmapUtil() {

	}

	/**
	 * Converts a specific drawable into a bitmap.
	 * 
	 * @param drawable
	 *            The drawable, which should be converted, as an instance of the
	 *            class {@link Drawable}. The drawable may not be null
	 * @return The bitmap, which has been created, as an instance of the class
	 *         {@link Bitmap}
	 */
	public static Bitmap convertDrawableToBitmap(final Drawable drawable) {
		ensureNotNull(drawable, "The drawable may not be null");

		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

			if (bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}

		Bitmap bitmap = null;

		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Creates a new bitmap with a specific width and height by tiling another
	 * bitmap.
	 * 
	 * @param bitmap
	 *            The bitmap, which should be tiled, as an instance of the class
	 *            {@link Bitmap}. The bitmap may not be null
	 * @param width
	 *            The width of the bitmap as an {@link Integer} value in pixels.
	 *            The width must be at least 1
	 * @param height
	 *            The height of the bitmap as an {@link Integer} value in
	 *            pixels. The height must be at least 1
	 * @return The bitmap, which has been created as an instance of the class
	 *         {@link Bitmap}
	 */
	public static Bitmap createTiledBitmap(final Bitmap bitmap, final int width, final int height) {
		ensureNotNull(bitmap, "The bitmap may not be null");
		ensureAtLeast(width, 1, "The width must be at least 1");
		ensureAtLeast(height, 1, "The height must be at least 1");

		Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		int origWidth = bitmap.getWidth();
		int origHeight = bitmap.getHeight();

		for (int x = 0; x < width; x += origWidth) {
			for (int y = 0; y < width; y += origHeight) {
				int copyWidth = (width - x >= origWidth) ? origWidth : width - x;
				int copyHeight = (height - y >= origHeight) ? origHeight : height - y;
				Rect src = new Rect(0, 0, copyWidth, copyHeight);
				Rect dest = new Rect(x, y, x + copyWidth, y + copyHeight);
				canvas.drawBitmap(bitmap, src, dest, null);
			}
		}

		return result;
	}

	/**
	 * Clips the long edge of a bitmap, if its width and height are not equal,
	 * in order to transform it into a square and resizes it to a specific size.
	 * Additionally, a border will be added.
	 * 
	 * @param bitmap
	 *            The bitmap, which should be clipped, as an instance of the
	 *            class {@link Bitmap}. The bitmap may not be null
	 * @param size
	 *            The size, the bitmap should be resized to, as an
	 *            {@link Integer} value in pixels. The size must be at least 1
	 * @param borderWidth
	 *            The width of the border as an {@link Integer} value in pixels.
	 *            The width must be at least 0
	 * @param borderColor
	 *            The color of the border as an {@link Integer} value
	 * @return The clipped bitmap as an instance of the class {@link Bitmap}
	 */
	public static Bitmap clipSquare(final Bitmap bitmap, final int size, final int borderWidth, final int borderColor) {
		ensureAtLeast(borderWidth, 0, "The border width must be at least 0");
		Bitmap clippedBitmap = clipSquare(bitmap, size);
		Bitmap result = Bitmap.createBitmap(clippedBitmap.getWidth(), clippedBitmap.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		float offset = borderWidth / 2.0f;
		Rect src = new Rect(0, 0, clippedBitmap.getWidth(), clippedBitmap.getHeight());
		RectF dst = new RectF(offset, offset, result.getWidth() - offset, result.getHeight() - offset);
		canvas.drawBitmap(clippedBitmap, src, dst, null);

		if (borderWidth > 0) {
			Paint paint = new Paint();
			paint.setFilterBitmap(false);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(borderWidth);
			paint.setColor(borderColor);
			offset = borderWidth / 2.0f;
			RectF bounds = new RectF(offset, offset, result.getWidth() - offset, result.getWidth() - offset);
			canvas.drawRect(bounds, paint);
		}

		return result;
	}

	/**
	 * Clips the corners of a bitmap in order to transform it into a round shape
	 * and resizes it to a specific size. Additionally, a border will be added.
	 * Bitmaps, whose width and height are not equal, will be clipped to a
	 * square beforehand.
	 * 
	 * @param bitmap
	 *            The bitmap, which should be clipped, as an instance of the
	 *            class {@link Bitmap}. The bitmap may not be null
	 * @param size
	 *            The size, the bitmap should be resized to, as an
	 *            {@link Integer} value in pixels. The size must be at least 1
	 * @param borderWidth
	 *            The width of the border as an {@link Integer} value in pixels.
	 *            The width must be at least 0
	 * @param borderColor
	 *            The color of the border as an {@link Integer} value
	 * @return The clipped bitmap as an instance of the class {@link Bitmap}
	 */
	public static Bitmap clipCircle(final Bitmap bitmap, final int size, final int borderWidth, final int borderColor) {
		ensureAtLeast(borderWidth, 0, "The border width must be at least 0");
		Bitmap clippedBitmap = clipCircle(bitmap, size);
		Bitmap result = Bitmap.createBitmap(clippedBitmap.getWidth(), clippedBitmap.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		float offset = borderWidth / 2.0f;
		Rect src = new Rect(0, 0, clippedBitmap.getWidth(), clippedBitmap.getHeight());
		RectF dst = new RectF(offset, offset, result.getWidth() - offset, result.getHeight() - offset);
		canvas.drawBitmap(clippedBitmap, src, dst, null);

		if (borderWidth > 0) {
			Paint paint = new Paint();
			paint.setFilterBitmap(false);
			paint.setAntiAlias(true);
			paint.setFlags(Paint.ANTI_ALIAS_FLAG);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(borderWidth);
			paint.setColor(borderColor);
			offset = borderWidth / 2.0f;
			RectF bounds = new RectF(offset, offset, result.getWidth() - offset, result.getWidth() - offset);
			canvas.drawArc(bounds, 0, COMPLETE_ARC_ANGLE, false, paint);
		}

		return result;
	}

	/**
	 * Tints a bitmap by using a specific color.
	 * 
	 * @param bitmap
	 *            The bitmap, which should be tinted, as an instance of the
	 *            class {@link Bitmap}. The bitmap may not be null
	 * @param color
	 *            The color, which should be used for tinting, as an
	 *            {@link Integer} value
	 * @return The tinted bitmap as an instance of the class {@link Bitmap}
	 */
	public static Bitmap tint(final Bitmap bitmap, final int color) {
		ensureNotNull(bitmap, "The bitmap may not be null");
		Bitmap result = bitmap;

		Canvas canvas = new Canvas(result);
		Paint paint = new Paint();
		paint.setColor(color);
		canvas.drawRect(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), paint);
		return result;
	}

}