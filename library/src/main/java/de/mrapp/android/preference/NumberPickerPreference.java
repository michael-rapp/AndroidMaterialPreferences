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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import de.mrapp.android.dialog.MaterialDialogBuilder;
import de.mrapp.android.preference.view.NumberPicker;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureAtMaximum;
import static de.mrapp.android.util.Condition.ensureGreater;
import static de.mrapp.android.util.Condition.ensureSmaller;

/**
 * A preference, which allows to choose a decimal number via a {@link NumberPicker} widget. The
 * chosen number will only be persisted, if confirmed by the user.
 *
 * @author Michael Rapp
 * @since 1.1.0
 */
public class NumberPickerPreference extends AbstractNumberPickerPreference {

    /**
     * A data structure, which allows to save the internal state of an {@link
     * NumberPickerPreference}.
     */
    public static class SavedState extends BaseSavedState {

        /**
         * A creator, which allows to create instances of the class {@link SavedState} from
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
         * The saved value of the attribute "currentNumber".
         */
        public int currentNumber;

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * NumberPickerPreference}. This constructor is called by derived classes when saving their
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
         * Creates a new data structure, which allows to store the internal state of an {@link
         * NumberPickerPreference}. This constructor is used when reading from a parcel. It reads
         * the state of the superclass.
         *
         * @param source
         *         The parcel to read read from as a instance of the class {@link Parcel}. The
         *         parcel may not be null
         */
        public SavedState(@NonNull final Parcel source) {
            super(source);
            currentNumber = source.readInt();
        }

        @Override
        public final void writeToParcel(final Parcel destination, final int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(currentNumber);
        }

    }

    /**
     * The {@link NumberPicker} widget, which allows to choose a decimal number.
     */
    private NumberPicker numberPicker;

    /**
     * The currently selected index of the {@link NumberPicker} widget.
     */
    private int currentIndex;

    /**
     * The minimum number, the preference allows to choose.
     */
    private int minNumber;

    /**
     * The maximum number, the preference allows to choose.
     */
    private int maxNumber;

    /**
     * The step size, the number is increased or decreased by when moving the selector wheel.
     */
    private int stepSize;

    /**
     * Initializes the preference.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void initialize(@Nullable final AttributeSet attributeSet) {
        obtainStyledAttributes(attributeSet);
    }

    /**
     * Obtains all attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet) {
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attributeSet, R.styleable.AbstractNumericPreference);

        try {
            obtainMaxNumber(typedArray);
            obtainMinNumber(typedArray);
            obtainStepSize(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the maximum number, the preference allows to choose, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the maximum number should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainMaxNumber(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.number_picker_preference_default_max_number);
        setMaxNumber(typedArray
                .getInteger(R.styleable.AbstractNumericPreference_android_max, defaultValue));
    }

    /**
     * Obtains the minimum number, the preference allows to choose, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the minimum number should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainMinNumber(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.number_picker_preference_default_min_number);
        setMinNumber(
                typedArray.getInteger(R.styleable.AbstractNumericPreference_min, defaultValue));
    }

    /**
     * Obtains the step size, the number should be increased or decreased by when moving the
     * selector wheel, form a specific typed array.
     *
     * @param typedArray
     *         The typed array, the step size should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainStepSize(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.number_picker_preference_default_step_size);
        setStepSize(typedArray
                .getInteger(R.styleable.AbstractNumericPreference_stepSize, defaultValue));
    }

    /**
     * Adapts a specific number to the step size, which is currently set. The number will be
     * decreased or increased to the numerically closest value, which matches the step size.
     *
     * @param number
     *         The number, which should be adapted to the step size, as an {@link Integer} value
     * @return The adapted number as an {@link Integer} value
     */
    private int adaptToStepSize(final int number) {
        if (getStepSize() > 0) {
            int offset = number - getMinNumber();
            int mod = offset % getStepSize();
            float halfStepSize = (float) getStepSize() / 2.0f;
            int result = ((mod > halfStepSize) ? offset + getStepSize() - mod : offset - mod) +
                    getMinNumber();
            return Math.min(result, getMaxNumber());
        }

        return number;
    }

    /**
     * Creates and returns an array, which contains the values of the {@link NumberPicker},
     * depending on the current step size.
     *
     * @return The array, which has been creates, as a {@link String} array
     */
    private String[] createDisplayedValues() {
        int steps = 1 + (int) Math.ceil((double) getRange() / (double) getStepSize());
        String[] values = new String[steps];
        int current = getMinNumber();

        for (int i = 0; i < steps; i++) {
            values[i] = String.valueOf(current);
            current = Math.min(current + getStepSize(), getMaxNumber());
        }

        return values;
    }

    /**
     * Creates and returns a listener, which allows to observe the {@link NumberPicker}, which is
     * used by the preference.
     *
     * @return The listener, which has been created, as an {@link OnValueChangeListener}
     */
    private OnValueChangeListener createNumberPickerListener() {
        return new OnValueChangeListener() {

            @Override
            public void onValueChange(final android.widget.NumberPicker numberPicker,
                                      final int oldValue, final int newValue) {
                numberPicker.setValue(newValue);
                currentIndex = getMinNumber() + newValue * getStepSize();
            }

        };
    }

    /**
     * Returns the currently selected index of the {@link NumberPicker} widget.
     *
     * @return The currently selected index of the {@link NumberPicker} widget as an {@link Integer}
     * value
     */
    protected final int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Creates a new preference, which allows to choose a decimal number via a {@link NumberPicker}
     * widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public NumberPickerPreference(@NonNull final Context context) {
        super(context);
        initialize(null);
    }

    /**
     * Creates a new preference, which allows to choose a decimal number via a {@link NumberPicker}
     * widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public NumberPickerPreference(@NonNull final Context context,
                                  @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(attributeSet);
    }

    /**
     * Creates a new preference, which allows to choose a decimal number via a {@link NumberPicker}
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
    public NumberPickerPreference(@NonNull final Context context,
                                  @Nullable final AttributeSet attributeSet,
                                  final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet);
    }

    /**
     * Creates a new preference, which allows to choose a decimal number via a {@link NumberPicker}
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
    public NumberPickerPreference(@NonNull final Context context,
                                  @Nullable final AttributeSet attributeSet, final int defaultStyle,
                                  final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet);
    }

    /**
     * Returns the minimum number, the preference allows to choose.
     *
     * @return The minimum number, the preference allows to choose, as an {@link Integer} value
     */
    public final int getMinNumber() {
        return minNumber;
    }

    /**
     * Sets the minimum number, the preference allows to choose.
     *
     * @param minNumber
     *         The minimum number, which should be set, as an {@link Integer} value. The number must
     *         be less than the maximum number
     */
    public final void setMinNumber(final int minNumber) {
        ensureAtLeast(minNumber, 0, "The minimum number must be at least 0");
        ensureSmaller(minNumber, getMaxNumber(),
                "The minimum number must be less than the maximum number");
        this.minNumber = minNumber;
        setNumber(Math.max(getNumber(), minNumber));
    }

    /**
     * Returns the maximum number, the preference allows to choose.
     *
     * @return The maximum number, the preference allows to choose, as an {@link Integer} value
     */
    public final int getMaxNumber() {
        return maxNumber;
    }

    /**
     * Sets the maximum number, the preference allows to choose.
     *
     * @param maxNumber
     *         The maximum number, which should be set, as an {@link Integer} value. The number must
     *         be greater than the minimum number
     */
    public final void setMaxNumber(final int maxNumber) {
        ensureGreater(maxNumber, getMinNumber(),
                "The maximum number must be greater than the minimum number");
        this.maxNumber = maxNumber;
        setNumber(Math.min(getNumber(), maxNumber));
    }

    /**
     * Returns the range of numbers, the preference allows to choose from.
     *
     * @return The range of numbers, the preference allows to choose from, as an {@link Integer}
     * value
     */
    public final int getRange() {
        return maxNumber - minNumber;
    }

    /**
     * Returns the step size, the number is increased or decreased by when moving the selector
     * wheel.
     *
     * @return The step size, the number is increased or decreased by when moving the selector
     * wheel, as an {@link Integer} value
     */
    public final int getStepSize() {
        return stepSize;
    }

    /**
     * Sets the step size, the number should be increased or decreased by when moving the selector
     * wheel.
     *
     * @param stepSize
     *         The step size, which should be set, as an {@link Integer} value. The value must be
     *         between at least 1 and at maximum the value of the method
     *         <code>getRange():int</code>
     */
    public final void setStepSize(final int stepSize) {
        ensureAtLeast(stepSize, 1, "The step size must be at least 1");
        ensureAtMaximum(stepSize, getRange(), "The step size must be at maximum the range");
        this.stepSize = stepSize;
        setNumber(adaptToStepSize(getNumber()));
    }

    @Override
    public final void setNumber(final int number) {
        ensureAtLeast(number, getMinNumber(), "The number must be at least the minimum number");
        ensureAtMaximum(number, getMaxNumber(), "The number must be at maximum the maximum number");
        int roundedNumber = adaptToStepSize(number);
        this.currentIndex = roundedNumber;
        super.setNumber(roundedNumber);
    }

    @Override
    public final void useInputMethod(final boolean useInputMethod) {
        super.useInputMethod(useInputMethod);

        if (numberPicker != null) {
            numberPicker.setDescendantFocusability(
                    useInputMethod ? NumberPicker.FOCUS_BEFORE_DESCENDANTS :
                            NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        }
    }

    @Override
    public final void wrapSelectorWheel(final boolean wrapSelectorWheel) {
        super.wrapSelectorWheel(wrapSelectorWheel);

        if (numberPicker != null) {
            numberPicker.setWrapSelectorWheel(wrapSelectorWheel);
        }
    }

    @Override
    protected final void onPrepareDialog(@NonNull final MaterialDialogBuilder dialogBuilder) {
        View view = View.inflate(getContext(), R.layout.number_picker, null);
        LinearLayout container = (LinearLayout) view.findViewById(R.id.number_picker_container);

        String[] displayedValues = createDisplayedValues();
        numberPicker = new NumberPicker(getContext());
        numberPicker.setDisplayedValues(displayedValues);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(displayedValues.length - 1);
        numberPicker.setValue(
                Math.round((float) (getCurrentIndex() - getMinNumber()) / (float) getStepSize()));
        numberPicker.setWrapSelectorWheel(isSelectorWheelWrapped());
        numberPicker.setDescendantFocusability(
                isInputMethodUsed() ? NumberPicker.FOCUS_BEFORE_DESCENDANTS :
                        NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setOnValueChangedListener(createNumberPickerListener());
        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        container.addView(numberPicker, 0, layoutParams);

        TextView unitTextView = (TextView) container.findViewById(R.id.unit_text_view);
        unitTextView.setText(getUnit());
        unitTextView.setVisibility(TextUtils.isEmpty(getUnit()) ? View.GONE : View.VISIBLE);

        dialogBuilder.setView(view);
    }

    @Override
    protected final void onDialogClosed(final boolean positiveResult) {
        if (positiveResult && callChangeListener(getCurrentIndex())) {
            setNumber(getCurrentIndex());
        } else {
            currentIndex = getNumber();
        }

        numberPicker = null;
    }

    @Override
    protected final Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentNumber = getCurrentIndex();
        return savedState;
    }

    @Override
    protected final void onRestoreInstanceState(final Parcelable state) {
        if (state != null && state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            currentIndex = savedState.currentNumber;
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}