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

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * An utility class, which offers methods for calculations, depending on the
 * device's display metrics.
 * 
 * @author Michael Rapp
 * 
 * @since 1.4.0
 */
public final class DisplayUtil {

	/**
	 * The ratio between pixel and dp measurement.
	 */
	protected static final float PIXEL_DP_RATIO = 160.0f;

	/**
	 * Creates a new utility class, which offers methods for calculations,
	 * depending on the device's display metrics.
	 */
	private DisplayUtil() {

	}

	/**
	 * Converts a pixel value to a dp value.
	 * 
	 * @param context
	 *            The context, which should be used to retrieve the device's
	 *            display metrics, as an instance of the class {@link Context}.
	 *            The context may not be null
	 * @param pixels
	 *            The pixel value, which should be converted, as an
	 *            {@link Integer} value
	 * @return The calculated dp value as an {@link Integer} value. The value
	 *         may be rounded
	 */
	public static int convertPixelsToDp(final Context context, final int pixels) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return Math.round(pixels / (metrics.densityDpi / PIXEL_DP_RATIO));
	}

	/**
	 * Converts a dp value to a pixel value.
	 * 
	 * @param context
	 *            The context, which should be used to retrieve the device's
	 *            display metrics, as an instance of the class {@link Context}.
	 *            The context may not be null
	 * @param dp
	 *            The dp value, which should be converted, as an {@link Integer}
	 *            value
	 * @return The calculated pixel value as an {@link Integer} value. The value
	 *         may be rounded
	 */
	public static int convertDpToPixels(final Context context, final int dp) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return Math.round(dp * (metrics.densityDpi / PIXEL_DP_RATIO));
	}

}