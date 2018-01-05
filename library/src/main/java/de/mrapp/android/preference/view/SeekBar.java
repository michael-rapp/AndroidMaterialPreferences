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
package de.mrapp.android.preference.view;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import de.mrapp.android.preference.R;
import de.mrapp.android.util.ThemeUtil;

/**
 * A custom view, which is extended from the view {@link android.widget.SeekBar} in order to provide
 * a getter method, which allows to retrieve the seek bar's thumb on API versions less than 16.
 * Furthermore, a setter method, which allows to set the seek bar's seekBarColor is provided.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SeekBar extends AppCompatSeekBar {

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSeekBarColor(ThemeUtil.getColor(getContext(), R.attr.colorAccent));
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
                   @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
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