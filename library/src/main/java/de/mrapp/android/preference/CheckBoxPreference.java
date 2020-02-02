/*
 * Copyright 2014 - 2020 Michael Rapp
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
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

/**
 * A preference, which provides a two-state toggleable option using a CheckBox widget.
 *
 * @author Michael Rapp
 * @since 5.2.0
 */
public class CheckBoxPreference extends AbstractCompoundButtonPreference {

    /**
     * Initializes the preference.
     */
    private void initialize() {
        setWidgetLayoutResource(R.layout.check_box_widget);
    }

    /**
     * Creates a new preference, which provides a two-state toggleable option using a CheckBox
     * widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public CheckBoxPreference(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new preference, which provides a two-state toggleable option using a CheckBox
     * widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public CheckBoxPreference(@NonNull final Context context,
                              @Nullable final AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.checkBoxPreferenceStyle);
    }

    /**
     * Creates a new preference, which provides a two-state toggleable option using a CheckBox
     * widget.
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
    public CheckBoxPreference(@NonNull final Context context,
                              @Nullable final AttributeSet attributeSet,
                              @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize();
    }

    /**
     * Creates a new preference, which provides a two-state toggleable option using a CheckBox
     * widget.
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
    public CheckBoxPreference(@NonNull final Context context,
                              @Nullable final AttributeSet attributeSet,
                              @AttrRes final int defaultStyle,
                              @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize();
    }

}
