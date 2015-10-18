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
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import de.mrapp.android.dialog.MaterialDialogBuilder;
import de.mrapp.android.preference.view.SeekBar;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureAtMaximum;
import static de.mrapp.android.util.Condition.ensureGreater;
import static de.mrapp.android.util.Condition.ensureSmaller;

/**
 * A preference, which allows to select a value from a continuous range via a seek bar. When
 * interacting with the preference, the seek bar is shown within a dialog. The chosen value will
 * only be persisted, if confirmed by the user. The preference can be used to select floating point
 * values if a specific number of decimals or integer values. Furthermore it is possible to
 * customize the appearance of the dialog and to set a step size, the currently chosen value is
 * increased or decreased by, when moving the seek bar.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SeekBarPreference extends AbstractDialogPreference {

    /**
     * A data structure, which allows to save the internal state of a {@link SeekBarPreference}.
     */
    public static class SavedState extends BaseSavedState {

        /**
         * A creator, which allows to create instances of the class {@link SeekBarPreference} from
         * parcels.
         */
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    @Override
                    public SavedState createFromParcel(final Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(final int size) {
                        return new SavedState[size];
                    }
                };

        /**
         * The saved value of the attribute "value".
         */
        public float value;

        /**
         * The saved value of the attribute "currentValue".
         */
        public float currentValue;

        /**
         * Creates a new data structure, which allows to store the internal state of a {@link
         * SeekBarPreference}. This constructor is called by derived classes when saving their
         * states.
         *
         * @param superState
         *         The state of the superclass of this view, as an instance of the type {@link
         *         Parcelable}. The state may not be null
         */
        public SavedState(@NonNull final Parcelable superState) {
            super(superState);
        }

        /**
         * Creates a new data structure, which allows to store the internal state of a {@link
         * SeekBarPreference}. This constructor is used when reading from a parcel. It reads the
         * state of the superclass.
         *
         * @param source
         *         The parcel to read read from as a instance of the class {@link Parcel}. The
         *         parcel may not be null
         */
        public SavedState(@NonNull final Parcel source) {
            super(source);
            value = source.readFloat();
            currentValue = source.readFloat();
        }

        @Override
        public final void writeToParcel(final Parcel destination, final int flags) {
            super.writeToParcel(destination, flags);
            destination.writeFloat(value);
            destination.writeFloat(currentValue);
        }

    }

    /**
     * The numeric system, which is used.
     */
    private static final double NUMERIC_SYSTEM = 10.0d;

    /**
     * The currently persisted value.
     */
    private float value;

    /**
     * The current value of the seek bar.
     */
    private float currentValue;

    /**
     * The maximum value of the seek bar.
     */
    private int minValue;

    /**
     * The minimum value of the seek bar.
     */
    private int maxValue;

    /**
     * The step size, the value is increased or decreased by when moving the seek bar.
     */
    private int stepSize;

    /**
     * The number of decimal numbers of the floating point numbers, the preference allows to
     * choose.
     */
    private int decimals;

    /**
     * The unit, which is used for textual representation of the preference's value.
     */
    private CharSequence unit;

    /**
     * The separator, which is used to show floating point values.
     */
    private CharSequence floatingPointSeparator;

    /**
     * An array, which contains the summaries, which should be shown depending on the currently
     * persisted value.
     */
    private CharSequence[] summaries;

    /**
     * True, if the progress of the seek bar should be shown, false otherwise.
     */
    private boolean showProgress;

    /**
     * Initializes the preference.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void initialize(@Nullable final AttributeSet attributeSet) {
        obtainStyledAttributes(attributeSet);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    /**
     * Obtains all attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet) {
        TypedArray seekBarTypedArray =
                getContext().obtainStyledAttributes(attributeSet, R.styleable.SeekBarPreference);
        TypedArray unitTypedArray = getContext()
                .obtainStyledAttributes(attributeSet, R.styleable.AbstractUnitPreference);
        TypedArray numericTypedArray = getContext()
                .obtainStyledAttributes(attributeSet, R.styleable.AbstractNumericPreference);

        try {
            obtainDecimals(seekBarTypedArray);
            obtainMaxValue(numericTypedArray);
            obtainMinValue(numericTypedArray);
            obtainStepSize(numericTypedArray);
            obtainUnit(unitTypedArray);
            obtainFloatingPointSeparator(seekBarTypedArray);
            obtainShowProgress(seekBarTypedArray);
            obtainSummaries(seekBarTypedArray);
        } finally {
            seekBarTypedArray.recycle();
        }
    }

    /**
     * Obtains the number of decimals of the floating point numbers, the preference allows to
     * choose, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the number of decimals should be obtained from, as an instance of
     *         the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainDecimals(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_decimals);
        setDecimals(typedArray.getInteger(R.styleable.SeekBarPreference_decimals, defaultValue));
    }

    /**
     * Obtains the minimum value, the preference allows to choose, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the minimum value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainMinValue(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_min_value);
        setMinValue(typedArray.getInteger(R.styleable.AbstractNumericPreference_min, defaultValue));
    }

    /**
     * Obtains the maximum value, the preference allows to choose, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the maximum value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainMaxValue(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_max_value);
        setMaxValue(typedArray
                .getInteger(R.styleable.AbstractNumericPreference_android_max, defaultValue));
    }

    /**
     * Obtains the step size, the value is increased or decreased by when moving the seek bar, from
     * a specific typed array.
     *
     * @param typedArray
     *         The typed array, the step size should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainStepSize(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_step_size);
        setStepSize(typedArray
                .getInteger(R.styleable.AbstractNumericPreference_stepSize, defaultValue));
    }

    /**
     * Obtains the unit, which is used for textual representation of the preference's value, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the unit should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainUnit(@NonNull final TypedArray typedArray) {
        setUnit(typedArray.getText(R.styleable.AbstractUnitPreference_unit));
    }

    /**
     * Obtains the symbol, which is used to separate floating point numbers for textual
     * representation, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the symbol should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainFloatingPointSeparator(@NonNull final TypedArray typedArray) {
        setFloatingPointSeparator(
                typedArray.getText(R.styleable.SeekBarPreference_floatingPointSeparator));
    }

    /**
     * Obtains the boolean value, which specifies whether the progress of the seek bar should be
     * shown, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the boolean value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainShowProgress(@NonNull final TypedArray typedArray) {
        boolean defaultValue = getContext().getResources()
                .getBoolean(R.bool.seek_bar_preference_default_show_progress);
        showProgress(
                typedArray.getBoolean(R.styleable.SeekBarPreference_showProgress, defaultValue));
    }

    /**
     * Obtains the summaries, which are shown depending on the currently persisted value, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the summaries should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainSummaries(@NonNull final TypedArray typedArray) {
        try {
            setSummaries(typedArray.getTextArray(R.styleable.SeekBarPreference_android_summary));
        } catch (NullPointerException e) {
            setSummaries(null);
        }
    }

    /**
     * Rounds a specific value to the number of decimals, which are currently set.
     *
     * @param value
     *         The value, which should be rounded, as a {@link Float} value
     * @return The rounded value as a {@link Float} value
     */
    private float roundToDecimals(final float value) {
        return Math.round(getMultiplier() * value) / (float) getMultiplier();
    }

    /**
     * Returns the value, a floating point value has to be multiplied with to transform it to an
     * integer value which is able to encode all of its decimals. By dividing such an integer value
     * by the return value of this method, the integer value can be transformed back to the original
     * floating point value.
     *
     * @return The multiplier as an {@link Integer} value
     */
    private int getMultiplier() {
        return (int) Math.pow(NUMERIC_SYSTEM, getDecimals());
    }

    /**
     * Creates and returns a listener, which allows to display the currently chosen value of the
     * pereference's seek bar. The current value is internally stored, but it will not become
     * persisted, until the user's confirmation.
     *
     * @param progressTextView
     *         The text view, which should be used to display the currently chosen value, as an
     *         instance of the class {@link TextView}. The text view may not be null
     * @return The listener, which has been created, as an instance of the type {@link
     * OnSeekBarChangeListener}
     */
    private OnSeekBarChangeListener createSeekBarListener(
            @NonNull final TextView progressTextView) {
        return new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(final android.widget.SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(final android.widget.SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(final android.widget.SeekBar seekBar, final int progress,
                                          final boolean fromUser) {
                currentValue = (float) getMinValue() + (float) progress / (float) getMultiplier();
                currentValue = adaptToStepSize(currentValue);
                progressTextView.setText(getProgressText(currentValue));
            }

        };
    }

    /**
     * Adapts a specific value to the step size, which is currently set. The value will be decreased
     * or increased to the numerically closest value, which matches the step size.
     *
     * @param value
     *         The value, which should be adapted to the step size, as a {@link Float} value
     * @return The adapted value as a {@link Float} value
     */
    private float adaptToStepSize(final float value) {
        if (getStepSize() > 0) {
            float offset = value - getMinValue();
            float mod = offset % getStepSize();
            float halfStepSize = (float) getStepSize() / 2.0f;
            float result = ((mod > halfStepSize) ? offset + getStepSize() - mod : offset - mod) +
                    getMinValue();
            return Math.min(result, getMaxValue());
        }

        return value;
    }

    /**
     * Returns a textual representation of a specific value. The text is formatted depending on the
     * decimal separator, which is currently set and contains the unit, if currently set.
     *
     * @param value
     *         The value, whose textual representation should be returned, as a {@link Float} value
     * @return A textual representation of the given value as a {@link String}
     */
    private String getProgressText(final float value) {
        NumberFormat numberFormat = NumberFormat.getInstance();

        if (getFloatingPointSeparator() != null && numberFormat instanceof DecimalFormat) {
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            decimalFormatSymbols.setDecimalSeparator(getFloatingPointSeparator().charAt(0));
            ((DecimalFormat) numberFormat).setDecimalFormatSymbols(decimalFormatSymbols);
        }

        numberFormat.setMinimumFractionDigits(getDecimals());
        numberFormat.setMaximumFractionDigits(getDecimals());
        String valueString = numberFormat.format(value);

        if (!TextUtils.isEmpty(getUnit())) {
            return valueString + " " + getUnit();
        }

        return valueString;
    }

    /**
     * Returns the current value of the seek bar.
     *
     * @return The current value of the seek bar as a {@link Float} value
     */
    protected final float getCurrentValue() {
        return currentValue;
    }

    /**
     * Creates a new preference, which allows to select a value from a continuous range via a seek
     * bar.
     *
     * @param context
     *         The context in which to store the preference's value as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public SeekBarPreference(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new preference, which allows to select a value from a continuous range via a seek
     * bar. This constructor is called when a preference is being constructed from an XML file,
     * supplying attributes that were specified in the XML file. This version uses a default style
     * of 0, so the only attribute values applied are those in the context's theme and the given
     * attribute set.
     *
     * @param context
     *         The Context this preference is associated with and through which it can access the
     *         current theme, resources, SharedPreferences, etc, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public SeekBarPreference(@NonNull final Context context,
                             @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(attributeSet);
    }

    /**
     * Creates a new preference, which allows to select a value from a continuous range via a seek
     * bar. This constructor allows subclasses to use their own base style when they are inflating.
     *
     * @param context
     *         The context in which to store Preference value as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the preference's attributes should be obtained from, as an
     *         instance of the type {@link AttributeSet} or null, if no attributes are available
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     */
    public SeekBarPreference(@NonNull final Context context,
                             @Nullable final AttributeSet attributeSet, final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet);
    }

    /**
     * Creates a new preference, which allows to select a value from a continuous range via a seek
     * bar. This constructor allows subclasses to use their own base style when they are inflating.
     *
     * @param context
     *         The context in which to store Preference value as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the preference's attributes should be obtained from, as an
     *         instance of the type {@link AttributeSet} or null, if no attributes are available
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
    public SeekBarPreference(@NonNull final Context context,
                             @Nullable final AttributeSet attributeSet, final int defaultStyle,
                             final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet);
    }

    /**
     * Returns the currently persisted value of the preference.
     *
     * @return The currently persisted value as a {@link Float} value
     */
    public final float getValue() {
        return value;
    }

    /**
     * Sets the current value of the preference. By setting a value, it will be persisted.
     *
     * @param value
     *         The value, which should be set, as a {@link Float} value. The value must be between
     *         the minimum and the maximum value, the preference allows to select
     */
    public final void setValue(final float value) {
        ensureAtLeast(value, getMinValue(), "The value must be at least the minimum value");
        ensureAtMaximum(value, getMaxValue(), "The value must be at maximum the maximum value");
        float roundedValue = adaptToStepSize(roundToDecimals(value));

        if (this.value != roundedValue) {
            this.value = roundedValue;
            this.currentValue = roundedValue;
            persistFloat(roundedValue);
            notifyChanged();
        }
    }

    /**
     * Returns the minimum value, the preference allows to choose.
     *
     * @return The minimum value, the preference allows to choose, as an {@link Integer} value
     */
    public final int getMinValue() {
        return minValue;
    }

    /**
     * Sets the minimum value, the preference should allow to choose.
     *
     * @param minValue
     *         The minimum value, which should be set, as an {@link Integer} value. The value must
     *         be between 0 and the maximum value, the preference allows to choose
     */
    public final void setMinValue(final int minValue) {
        ensureAtLeast(minValue, 0, "The minimum value must be at least 0");
        ensureSmaller(minValue, getMaxValue(),
                "The minimum value must be less than the maximum value");
        this.minValue = minValue;
        setValue(Math.max(getValue(), minValue));
    }

    /**
     * Returns the maximum value, the preference allows to choose.
     *
     * @return The maximum value, the preference allows to choose, as an {@link Integer} value
     */
    public final int getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the maximum value, the preference should allow to choose.
     *
     * @param maxValue
     *         The maximum value, which should be set, as an {@link Integer} value. The value must
     *         be greater than the minimum value, that preference allows to choose
     */
    public final void setMaxValue(final int maxValue) {
        ensureGreater(maxValue, getMinValue(),
                "The maximum value must be greater than the minimum value");
        this.maxValue = maxValue;
        setValue(Math.min(getValue(), maxValue));
    }

    /**
     * Returns the range of values, the preference allows to choose from.
     *
     * @return The range of values, the preference allows to choose from, as an {@link Integer}
     * value
     */
    public final int getRange() {
        return maxValue - minValue;
    }

    /**
     * Returns the step size, the value is increased or decreased by, when moving the seek bar.
     *
     * @return The step size, the value is increased or decreased by, when moving the seek bar, as
     * an {@link Integer} value
     */
    public final int getStepSize() {
        return stepSize;
    }

    /**
     * Sets the step size, the value should be increased or decreased by, when moving the seek bar.
     *
     * @param stepSize
     *         The step size, which should be set, as an {@link Integer} value. The value must be at
     *         least 1 and at maximum the value of the method <code>getRange():int</code> or -1, if
     *         the preference should allow to select a value from a continuous range
     */
    public final void setStepSize(final int stepSize) {
        if (stepSize != -1) {
            ensureAtLeast(stepSize, 1, "The step size must be at least 1");
            ensureAtMaximum(stepSize, getRange(), "The step size must be at maximum the range");
        }

        this.stepSize = stepSize;
        setValue(adaptToStepSize(getValue()));
    }

    /**
     * Returns the number of decimals of the floating point numbers, the preference allows to
     * choose. If the number of decimals is set to 0, the preference only allows to choose integer
     * values.
     *
     * @return The number of decimals of the floating point numbers, the preference allows to
     * choose, as an {@link Integer} value
     */
    public final int getDecimals() {
        return decimals;
    }

    /**
     * Sets the number of decimals of the floating point numbers, the preference should allow to
     * choose. If the number of decimals is set to 0, the preference will only allow to choose
     * integer values.
     *
     * @param decimals
     *         The number of decimals, which should be set, as an {@link Integer} value. The value
     *         must be at least 0. If the value is set to 0, the preference will only allow to
     *         choose integer values
     */
    public final void setDecimals(final int decimals) {
        ensureAtLeast(decimals, 0, "The decimals must be at least 0");
        this.decimals = decimals;
        setValue(roundToDecimals(getValue()));
    }

    /**
     * Returns the unit, which is used for textual representation of the preference's value.
     *
     * @return The unit, which is used for textual representation of the preference's value, as an
     * instance of the type {@link CharSequence} or null, if no unit is used
     */
    public final CharSequence getUnit() {
        return unit;
    }

    /**
     * Sets the unit, which should be used for textual representation of the preference's value.
     *
     * @param unit
     *         The unit, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no unit should be used
     */
    public final void setUnit(@Nullable final CharSequence unit) {
        this.unit = unit;
    }

    /**
     * Sets the unit, which should be used for textual representation of the preference's value.
     *
     * @param resourceId
     *         The resource id of the unit, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setUnit(@StringRes final int resourceId) {
        setUnit(getContext().getText(resourceId));
    }

    /**
     * Returns the symbol, which is used to separate floating point numbers for textual
     * representation.
     *
     * @return The symbol, which is used to separate floating point numbers for textual
     * representation, as an instance of the type {@link Character} or null, if the default symbol
     * is used
     */
    public final CharSequence getFloatingPointSeparator() {
        return floatingPointSeparator;
    }

    /**
     * Sets the symbol, which should be used to separate floating point numbers for textual
     * representation.
     *
     * @param floatingPointSeparator
     *         The symbol, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if the default symbol should be used. The length of the symbol must be 1
     */
    public final void setFloatingPointSeparator(
            @Nullable final CharSequence floatingPointSeparator) {
        if (floatingPointSeparator != null) {
            ensureAtMaximum(floatingPointSeparator.length(), 1,
                    "The floating point separator's length must be 1");
        }
        this.floatingPointSeparator = floatingPointSeparator;
    }

    /**
     * Sets the symbol, which should be used to separate floating point numbers for textual
     * representation.
     *
     * @param resourceId
     *         The resource id of the symbol, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setFloatingPointSeparator(@StringRes final int resourceId) {
        setFloatingPointSeparator(getContext().getResources().getText(resourceId));
    }

    /**
     * Returns, whether the currently selected value of the seek bar is shown, or not.
     *
     * @return True, if the currently selected value of the seek bar is shown, false otherwise
     */
    public final boolean isProgressShown() {
        return showProgress;
    }

    /**
     * Sets, whether the currently selected value of the seek bar should be shown, or not.
     *
     * @param showProgress
     *         True, if the currently selected value of the seek bar should be shown, false
     *         otherwise
     */
    public final void showProgress(final boolean showProgress) {
        this.showProgress = showProgress;
    }

    /**
     * Returns the summaries, which are shown depending on the currently persisted value.
     *
     * @return The summaries, which are shown depending on the currently persisted value, as an
     * array of the type {@link CharSequence} or null, if no summaries are shown depending on the
     * currently persisted value
     */
    public final CharSequence[] getSummaries() {
        return summaries;
    }

    /**
     * Sets the summaries, which should be shown depending on the currently persisted value.
     *
     * @param summaries
     *         The summaries, which should be set, as an array of the type {@link CharSequence} or
     *         null, if no summaries should be shown depending on the currently persisted value
     */
    public final void setSummaries(@Nullable final CharSequence[] summaries) {
        this.summaries = summaries;
    }

    @Override
    public final CharSequence getSummary() {
        if (isValueShownAsSummary()) {
            return getProgressText(getValue());
        } else if (getSummaries() != null && getSummaries().length > 0) {
            float interval = (float) getRange() / (float) getSummaries().length;
            int index = (int) Math.floor((getValue() - getMinValue()) / interval);
            index = Math.min(index, getSummaries().length - 1);
            return getSummaries()[index];
        } else {
            return super.getSummary();
        }
    }

    @Override
    public final void setSummary(final CharSequence summary) {
        super.setSummary(summary);
        this.summaries = null;
    }

    @Override
    public final void setSummary(final int resourceId) {
        try {
            setSummaries(getContext().getResources().getStringArray(resourceId));
        } catch (NotFoundException e) {
            super.setSummary(resourceId);
        }
    }

    @Override
    protected final Object onGetDefaultValue(final TypedArray a, final int index) {
        return a.getFloat(index, 0);
    }

    @Override
    protected final void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
        setValue(restoreValue ? getPersistedFloat(getValue()) : (Float) defaultValue);
    }

    @Override
    protected final boolean needInputMethod() {
        return false;
    }

    @Override
    protected final void onPrepareDialog(@NonNull final MaterialDialogBuilder dialogBuilder) {
        View layout = View.inflate(getContext(), R.layout.seek_bar, null);

        TextView progressTextView = (TextView) layout.findViewById(R.id.progress_text);
        progressTextView.setText(getProgressText(getCurrentValue()));
        progressTextView.setVisibility(isProgressShown() ? View.VISIBLE : View.GONE);

        SeekBar seekBar = (SeekBar) layout.findViewById(R.id.seek_bar);
        seekBar.setMax(getRange() * getMultiplier());
        seekBar.setProgress(Math.round((getCurrentValue() - getMinValue()) * getMultiplier()));
        seekBar.setOnSeekBarChangeListener(createSeekBarListener(progressTextView));

        dialogBuilder.setView(layout);
    }

    @Override
    protected final void onDialogClosed(final boolean positiveResult) {
        if (positiveResult && callChangeListener(getCurrentValue())) {
            setValue(getCurrentValue());
        } else {
            currentValue = getValue();
        }
    }

    @Override
    protected final Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentValue = getCurrentValue();

        if (!isPersistent()) {
            savedState.value = getValue();
        } else {
            savedState.value = -1;
        }

        return savedState;
    }

    @Override
    protected final void onRestoreInstanceState(final Parcelable state) {
        if (state != null && state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            currentValue = savedState.currentValue;

            if (savedState.value != -1) {
                setValue(savedState.value);
            }

            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}