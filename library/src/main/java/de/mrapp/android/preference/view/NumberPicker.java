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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.reflect.Field;

import de.mrapp.android.preference.R;
import de.mrapp.android.util.ThemeUtil;

/**
 * A custom view, which is extended from the view {@link android.widget.NumberPicker} in order to
 * provide a setter method, which allows to set the color of the number picker's selectionDivider.
 *
 * @author Michael Rapp
 * @since 1.1.0
 */
public class NumberPicker extends android.widget.NumberPicker {

    /**
     * The tag, which is used for logging.
     */
    private static final String TAG = NumberPicker.class.getName();

    /**
     * The color of the number picker's divider.
     */
    private int seekBarColor;

    /**
     * Applies the attributes of the context's theme on the number picker.
     */
    private void applyTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setDividerColor(ThemeUtil.getColor(getContext(), R.attr.colorAccent));
        }
    }

    /**
     * Creates a new number picker.
     *
     * @param context
     *         The context, which should be used by the number picker, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public NumberPicker(@NonNull final Context context) {
        super(context);
        applyTheme();
    }

    /**
     * Creates a new number picker.
     *
     * @param context
     *         The context, which should be used by the number picker, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the view, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes are available
     */
    public NumberPicker(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        applyTheme();
    }

    /**
     * Creates a new number picker.
     *
     * @param context
     *         The context, which should be used by the number picker, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the view, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes are available
     * @param defaultStyle
     *         The default style to apply to this view. If 0, no style will be applied (beyond what
     *         is included in the theme). This may either be an attribute resource, whose value will
     *         be retrieved from the current theme, or an explicit style resource
     */
    public NumberPicker(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                        @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        applyTheme();
    }

    /**
     * Creates a new number picker.
     *
     * @param context
     *         The context, which should be used by the number picker, as an instance of the class
     *         {@link Context}. The context may not be null
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
    public NumberPicker(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                        @AttrRes final int defaultStyle, @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        applyTheme();
    }

    /**
     * Returns the color of the number pickers's divider.
     *
     * @return The color of the number picker's divider as an {@link Integer} value
     */
    public final int getDividerColor() {
        return seekBarColor;
    }

    /**
     * Sets the color of the number picker's divider.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setDividerColor(@ColorInt final int color) {
        this.seekBarColor = color;

        try {
            Field divider = getClass().getSuperclass().getDeclaredField("mSelectionDivider");
            divider.setAccessible(true);
            divider.set(this, new ColorDrawable(color));
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            Log.w(TAG, "Failed to set divider color", e);
        }
    }

}