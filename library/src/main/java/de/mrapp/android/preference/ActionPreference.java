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
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceViewHolder;
import de.mrapp.util.Condition;

/**
 * A preference, which acts as a button and displays a centered title.
 *
 * @author Michael Rapp
 * @since 2.7.0
 */
public class ActionPreference extends Preference {

    /**
     * The text view, which is used to show the preference's title.
     */
    private TextView textView;

    /**
     * The text color of the preference's title.
     */
    private ColorStateList textColor;

    /**
     * Initializes the preference.
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
    private void initialize(@Nullable final AttributeSet attributeSet,
                            @AttrRes final int defaultStyle,
                            @StyleRes final int defaultStyleResource) {
        obtainStyledAttributes(attributeSet, defaultStyle, defaultStyleResource);
        setLayoutResource(R.layout.action_preference);
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
                .obtainStyledAttributes(attributeSet, R.styleable.ActionPreference, defaultStyle,
                        defaultStyleResource);

        try {
            obtainTextColor(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the text color of the preference's title from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainTextColor(@NonNull final TypedArray typedArray) {
        ColorStateList colorStateList =
                typedArray.getColorStateList(R.styleable.ActionPreference_android_textColor);

        if (colorStateList == null) {
            colorStateList = ContextCompat
                    .getColorStateList(getContext(), R.color.action_preference_text_color);
        }

        setTextColor(colorStateList);
    }

    /**
     * Adapts the text color of the preference's title.
     */
    private void adaptTextColor() {
        if (textView != null) {
            textView.setTextColor(textColor);
        }
    }

    /**
     * Creates a new preference, which acts as a button and displays a centered title.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public ActionPreference(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new preference, which acts as a button and displays a centered title.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public ActionPreference(@NonNull final Context context,
                            @Nullable final AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.preferenceStyle);
    }

    /**
     * Creates a new preference, which acts as a button and displays a centered title.
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
    public ActionPreference(@NonNull final Context context,
                            @Nullable final AttributeSet attributeSet,
                            @AttrRes final int defaultStyle) {
        this(context, attributeSet, defaultStyle, 0);
        initialize(attributeSet, defaultStyle, 0);
    }

    /**
     * Creates a new preference, which acts as a button and displays a centered title.
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
    public ActionPreference(@NonNull final Context context,
                            @Nullable final AttributeSet attributeSet,
                            @AttrRes final int defaultStyle,
                            @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet, defaultStyle, defaultStyleResource);
    }

    /**
     * Returns the text color of the preference's title.
     *
     * @return The text color of the preference's title as an instance of the class {@link
     * ColorStateList}. The color state list may not be null
     */
    @NonNull
    public final ColorStateList getTextColor() {
        return textColor;
    }

    /**
     * Sets the text color of the preference's title.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setTextColor(@ColorInt final int color) {
        setTextColor(ColorStateList.valueOf(color));
    }

    public final void setTextColor(@NonNull final ColorStateList colorStateList) {
        Condition.INSTANCE.ensureNotNull(colorStateList, "The color state list may not be null");
        this.textColor = colorStateList;
        adaptTextColor();
    }

    @Override
    public final void performClick() {
        OnPreferenceClickListener clickListener = getOnPreferenceClickListener();

        if (clickListener != null) {
            clickListener.onPreferenceClick(this);
        }
    }

    @CallSuper
    @Override
    public void onBindViewHolder(final PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        textView = (TextView) holder.findViewById(android.R.id.title);
        textView.setAllCaps(true);
        adaptTextColor();
    }

}