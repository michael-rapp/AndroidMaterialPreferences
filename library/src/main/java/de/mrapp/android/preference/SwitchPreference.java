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
package de.mrapp.android.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

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
    private SwitchCompat switchCompat;

    /**
     * Obtains all attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet) {
        TypedArray typedArray =
                getContext().obtainStyledAttributes(attributeSet, R.styleable.SwitchPreference);

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
        if (switchCompat != null) {
            switchCompat.setTextOn(getSwitchTextOn());
            switchCompat.setTextOff(getSwitchTextOff());
            switchCompat.setShowText(!TextUtils.isEmpty(getSwitchTextOn()) ||
                    !TextUtils.isEmpty(getSwitchTextOff()));
            switchCompat.setChecked(isChecked());
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
                setChecked(isChecked);
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
        super(context, attributeSet);
        obtainStyledAttributes(attributeSet);
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
                            @Nullable final AttributeSet attributeSet, final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        obtainStyledAttributes(attributeSet);
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
                            @Nullable final AttributeSet attributeSet, final int defaultStyle,
                            final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        obtainStyledAttributes(attributeSet);
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
    protected final View onCreateView(final ViewGroup parent) {
        View view = super.onCreateView(parent);
        LinearLayout widgetFrame = (LinearLayout) view.findViewById(android.R.id.widget_frame);
        widgetFrame.setVisibility(View.VISIBLE);
        switchCompat = new SwitchCompat(getContext());
        switchCompat.setFocusable(false);
        switchCompat.setOnCheckedChangeListener(createCheckedChangeListener());
        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        widgetFrame.addView(switchCompat, layoutParams);
        adaptSwitch();
        return view;
    }

}