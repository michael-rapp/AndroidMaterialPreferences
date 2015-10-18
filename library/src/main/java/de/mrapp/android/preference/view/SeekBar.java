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
package de.mrapp.android.preference.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import de.mrapp.android.preference.R;

/**
 * A custom view, which is extended from the view {@link android.widget.SeekBar} in order to provide
 * a getter method, which allows to retrieve the seek bar's thumb on API versions less than 16.
 * Furthermore, a setter method, which allows to set the seek bar's seekBarColor is provided.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SeekBar extends android.widget.SeekBar {

    /**
     * The drawable, which is used to visualize the seek bar's thumb.
     */
    private Drawable thumb;

    /**
     * The seekBarColor of the seek bar.
     */
    private int seekBarColor;

    /**
     * Applies the attributes of the context's theme on the seek bar.
     */
    private void applyTheme() {
        TypedArray typedArray =
                getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.colorAccent});
        int color = typedArray.getColor(0, 0);

        if (color != 0) {
            setSeekBarColor(color);
        }
    }

    /**
     * Creates a new seek bar.
     *
     * @param context
     *         The context, which should be used by the seek bar, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public SeekBar(@NonNull final Context context) {
        super(context);
        applyTheme();
    }

    /**
     * Creates a new seek bar.
     *
     * @param context
     *         The context, which should be used by the seek bar, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the view, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes are available
     */
    public SeekBar(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        applyTheme();
    }

    /**
     * Creates a new seek bar.
     *
     * @param context
     *         The context, which should be used by the seek bar, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the view, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes are available
     * @param defaultStyle
     *         The default style to apply to this view. If 0, no style will be applied (beyond what
     *         is included in the theme). This may either be an attribute resource, whose value will
     *         be retrieved from the current theme, or an explicit style resource
     */
    public SeekBar(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                   final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        applyTheme();
    }

    /**
     * Creates a new seek bar.
     *
     * @param context
     *         The context, which should be used by the seek bar, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the view, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes are available
     * @param defaultStyle
     *         The default style to apply to this view. If 0, no style will be applied (beyond what
     *         is included in the theme). This may either be an attribute resource, whose value will
     *         be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the view,
     *         used only if the default style is 0 or can not be found in the theme. Can be 0 to not
     *         look for defaults
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SeekBar(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                   final int defaultStyle, final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        applyTheme();
    }

    /**
     * Returns the color of the seek bar.
     *
     * @return The color of the seek bar as an {@link Integer} value
     */
    public final int getSeekBarColor() {
        return seekBarColor;
    }

    /**
     * Sets the color of the seek bar.
     *
     * @param color
     *         The color, which should be set as an {@link Integer} value
     */
    public final void setSeekBarColor(@ColorInt final int color) {
        this.seekBarColor = color;
        ColorFilter colorFilter = new PorterDuffColorFilter(color, Mode.SRC_IN);
        getProgressDrawable().setColorFilter(colorFilter);
        getThumbDrawable().setColorFilter(colorFilter);
    }

    /**
     * Returns the drawable, which is used to visualize the seek bar's thumb.
     *
     * @return The drawable, which is used to visualize the seek bar's thumb, as an instance of the
     * class {@link Drawable}
     */
    public final Drawable getThumbDrawable() {
        return thumb;
    }

    @Override
    public final void setThumb(final Drawable thumb) {
        super.setThumb(thumb);
        this.thumb = thumb;
    }

}