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
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * A preference, which provides a two-state toggleable option using a SwitchCompat widget.
 *
 * @author Michael Rapp
 * @since 1.4.0
 */
public class SwitchPreference extends AbstractTwoStatePreference {

    /**
     * The text, which is displayed on the preference's switch, when it is checked.
     */
    private CharSequence switchTextOn;

    /**
     * The text, which is displayed on the preference's switch, when it is not checked.
     */
    private CharSequence switchTextOff;

    /**
     * The switch, which allows to toggle the preference's value.
     */
    private SwitchCompat switchWidget;

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
        setWidgetLayoutResource(R.layout.switch_widget);
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
                .obtainStyledAttributes(attributeSet, R.styleable.SwitchPreference, defaultStyle,
                        defaultStyleResource);

        try {
            obtainSwitchTextOn(typedArray);
            obtainSwitchTextOff(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the text, which should be displayed on the preference's switch, when it is checked,
     * from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the text should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainSwitchTextOn(@NonNull final TypedArray typedArray) {
        setSwitchTextOn(typedArray.getText(R.styleable.SwitchPreference_android_switchTextOn));
    }

    /**
     * Obtains the text, which should be displayed on the preference's switch, when it is not
     * checked, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the text should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainSwitchTextOff(@NonNull final TypedArray typedArray) {
        setSwitchTextOff(typedArray.getText(R.styleable.SwitchPreference_android_switchTextOff));
    }

    /**
     * Adapts the preference's switch, depending on the preference's properties and on whether it is
     * currently checked or not.
     */
    private void adaptSwitch() {
        if (switchWidget != null) {
            switchWidget.setTextOn(getSwitchTextOn());
            switchWidget.setTextOff(getSwitchTextOff());
            switchWidget.setShowText(!TextUtils.isEmpty(getSwitchTextOn()) ||
                    !TextUtils.isEmpty(getSwitchTextOff()));
            switchWidget.setChecked(isChecked());
        }
    }

    /**
     * Creates and returns a listener, which allows to change the preference's value, depending on
     * the preference's switch's state.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnCheckedChangeListener}
     */
    private OnCheckedChangeListener createCheckedChangeListener() {
        return new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (getOnPreferenceChangeListener() == null || getOnPreferenceChangeListener()
                        .onPreferenceChange(SwitchPreference.this, isChecked)) {
                    setChecked(isChecked);
                } else {
                    setChecked(!isChecked);
                }
            }

        };
    }

    /**
     * Creates a new preference, which provides a two-state toggleable option using a SwitchCompat
     * widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public SwitchPreference(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new preference, which provides a two-state toggleable option using a SwitchCompat
     * widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public SwitchPreference(@NonNull final Context context,
                            @Nullable final AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.preferenceStyle);
    }

    /**
     * Creates a new preference, which provides a two-state toggleable option using a SwitchCompat
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
    public SwitchPreference(@NonNull final Context context,
                            @Nullable final AttributeSet attributeSet,
                            @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet, defaultStyle, 0);
    }

    /**
     * Creates a new preference, which provides a two-state toggleable option using a SwitchCompat
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
    public SwitchPreference(@NonNull final Context context,
                            @Nullable final AttributeSet attributeSet,
                            @AttrRes final int defaultStyle,
                            @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet, defaultStyle, defaultStyleResource);
    }

    /**
     * Returns the text, which is displayed on the preference's switch, when it is checked.
     *
     * @return The text, which is displayed on the preference's switch, when it is checked, as an
     * instance of the type {@link CharSequence} or null, if no dedicated text is displayed when the
     * preference's switch is checked
     */
    public final CharSequence getSwitchTextOn() {
        return switchTextOn;
    }

    /**
     * Sets the text, which should be displayed on the preference's switch, when it is checked. The
     * text should be very short - one word if possible.
     *
     * @param switchTextOn
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no dedicated text should be displayed when the preference's switch is
     *         checked
     */
    public final void setSwitchTextOn(@Nullable final CharSequence switchTextOn) {
        this.switchTextOn = switchTextOn;
        adaptSwitch();
    }

    /**
     * Sets the text, which should be displayed on the preference's switch, when it is checked. The
     * text should be very short - one word if possible.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setSwitchTextOn(@StringRes final int resourceId) {
        setSwitchTextOn(getContext().getString(resourceId));
    }

    /**
     * Returns the text, which is displayed on the preference's switch, when it is not checked.
     *
     * @return The text, which is displayed on the preference's switch, when it is not checked, as
     * an instance of the type {@link CharSequence} or null, if no dedicated text is displayed when
     * the preference's switch is not checked
     */
    public final CharSequence getSwitchTextOff() {
        return switchTextOff;
    }

    /**
     * Sets the text, which should be displayed on the preference's switch, when it is not checked.
     * The text should be very short - one word if possible.
     *
     * @param switchTextOff
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no dedicated text should be displayed when the preference's switch is not
     *         checked
     */
    public final void setSwitchTextOff(@Nullable final CharSequence switchTextOff) {
        this.switchTextOff = switchTextOff;
        adaptSwitch();
    }

    /**
     * Sets the text, which should be displayed on the preference's switch, when it is not checked.
     * The text should be very short - one word if possible.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setSwitchTextOff(@StringRes final int resourceId) {
        setSwitchTextOff(getContext().getString(resourceId));
    }

    @Override
    public final void setChecked(final boolean checked) {
        super.setChecked(checked);

        if (switchWidget != null) {
            switchWidget.setOnCheckedChangeListener(null);
            switchWidget.setChecked(checked);
            switchWidget.setOnCheckedChangeListener(createCheckedChangeListener());
        }
    }

    @Override
    public final void onBindViewHolder(final PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        switchWidget = (SwitchCompat) holder.findViewById(R.id.switch_widget);
        switchWidget.setOnCheckedChangeListener(createCheckedChangeListener());
        adaptSwitch();
    }

}