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
package de.mrapp.android.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A material-styled preference.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class Preference extends android.preference.Preference {

    /**
     * The color state list, which is used to tint the preference's icon.
     */
    private ColorStateList tintList;

    /**
     * The mode, which is used to tint the preference's icon.
     */
    private PorterDuff.Mode tintMode;

    /**
     * Initializes the preference.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet}
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the
     *         preference, used only if the default style is 0 or can not be found in the theme. Can
     *         be 0 to not look for defaults
     */
    private void initialize(final AttributeSet attributeSet, @AttrRes final int defaultStyle,
                            @StyleRes final int defaultStyleResource) {
        this.tintList = null;
        this.tintMode = PorterDuff.Mode.SRC_ATOP;
        obtainStyledAttributes(attributeSet, defaultStyle, defaultStyleResource);
    }

    /**
     * Obtains all attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the
     *         preference, used only if the default style is 0 or can not be found in the theme. Can
     *         be 0 to not look for defaults
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet,
                                        @AttrRes final int defaultStyle,
                                        @StyleRes final int defaultStyleResource) {
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attributeSet, R.styleable.Preference, defaultStyle,
                        defaultStyleResource);

        try {
            obtainIcon(typedArray);
            obtainTint(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the preference's icon from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the icon should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainIcon(@NonNull final TypedArray typedArray) {
        int resourceId = typedArray.getResourceId(R.styleable.Preference_android_icon, -1);

        if (resourceId != -1) {
            Drawable icon = AppCompatResources.getDrawable(getContext(), resourceId);
            setIcon(icon);
        }
    }

    /**
     * Obtains the color state list to tint the preference's icon from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color state list should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainTint(@NonNull final TypedArray typedArray) {
        setIconTintList(typedArray.getColorStateList(R.styleable.Preference_android_tint));
    }

    /**
     * Adapts the tint of the preference's icon.
     */
    private void adaptIconTint() {
        Drawable icon = getIcon();

        if (icon != null) {
            DrawableCompat.setTintList(icon, tintList);
            DrawableCompat.setTintMode(icon, tintMode);
        }
    }

    /**
     * Creates a new material-styled preference.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public Preference(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new material-styled preference.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public Preference(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /**
     * Creates a new material-styled preference.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     */
    public Preference(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                      @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet, defaultStyle, 0);
    }

    /**
     * Creates a new material-styled preference.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the
     *         preference, used only if the default style is 0 or can not be found in the theme. Can
     *         be 0 to not look for defaults
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Preference(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                      @AttrRes final int defaultStyle, @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet, defaultStyle, defaultStyleResource);
    }

    /**
     * Performs a click on the preference.
     */
    public void performClick() {

    }

    /**
     * Returns the color state list, which is used to tint the preference's icon.
     *
     * @return The color state list, which is used to tint the preference's icon as an instance of
     * the class {@link ColorStateList} or null, if no color state list has been set
     */
    public final ColorStateList getIconTintList() {
        return tintList;
    }

    /**
     * Sets the color, which should be used to tint the preference's icon.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setIconTint(@ColorInt final int color) {
        setIconTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the color state list, which should be used to tint the preference's icon.
     *
     * @param tintList
     *         The color state list, which should be set, as an instance of the class {@link
     *         ColorStateList} or null, if no color state list should be set
     */
    public final void setIconTintList(@Nullable final ColorStateList tintList) {
        this.tintList = tintList;
        adaptIconTint();
    }

    /**
     * Returns the mode, which is used to tint the preference's icon.
     *
     * @return The mode, which is used to tint the preference's icon, as a value of the enum {@link
     * PorterDuff.Mode}. The mode may not be null
     */
    @NonNull
    public final PorterDuff.Mode getIconTintMode() {
        return tintMode;
    }

    /**
     * Sets the mode, which should be used to tint the preference's icon.
     *
     * @param mode
     *         The mode, which should be set, as a value of the enum {@link PorterDuff.Mode}. The
     *         mode may not be null
     */
    public final void setIconTintMode(@NonNull final PorterDuff.Mode mode) {
        ensureNotNull(mode, "The icon tint mode may not be null");
        this.tintMode = mode;
        adaptIconTint();
    }

    @Override
    public void setIcon(@Nullable final Drawable icon) {
        super.setIcon(icon);
        adaptIconTint();
    }

}