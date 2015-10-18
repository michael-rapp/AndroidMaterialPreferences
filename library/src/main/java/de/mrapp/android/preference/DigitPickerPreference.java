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

/**
 * A preference, which allows to choose a decimal number via multiple {@link NumberPicker} widgets.
 * Each widget allows to choose one digit of the number individually. The chosen number will only be
 * persisted, if confirmed by the user.
 *
 * @author Michael Rapp
 * @since 1.1.0
 */
public class DigitPickerPreference extends AbstractNumberPickerPreference {

    /**
     * A data structure, which allows to save the internal state of an {@link
     * DigitPickerPreference}.
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
         * DigitPickerPreference}. This constructor is called by derived classes when saving their
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
     * The numeric system, which is used.
     */
    private static final int NUMERIC_SYTEM = 10;

    /**
     * The {@link NumberPicker} widgets, which are used by the preference.
     */
    private NumberPicker[] numberPickers;

    /**
     * The current number, consisting of the {@link NumberPicker} widgets, which are used by the
     * preference.
     */
    private int currentNumber;

    /**
     * The number of digits of the numbers, the preference allows to choose.
     */
    private int numberOfDigits;

    /**
     * Initializes the preference.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet}
     */
    private void initialize(final AttributeSet attributeSet) {
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
                .obtainStyledAttributes(attributeSet, R.styleable.NumberPickerPreference);
        try {
            obtainNumberOfDigits(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the number of digits of the numbers, the preference allows to choose, from a specific
     * typed array.
     *
     * @param typedArray
     *         The typed array, the number of digits should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainNumberOfDigits(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.digit_picker_preference_default_number_of_digits);
        setNumberOfDigits(typedArray
                .getInteger(R.styleable.DigitPickerPreference_numberOfDigits, defaultValue));
    }

    /**
     * Creates and returns a listener, which allows to observe the {@link NumberPicker} widgets,
     * which are used by the preference.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnValueChangeListener}
     */
    private OnValueChangeListener createNumberPickerListener() {
        return new OnValueChangeListener() {

            @Override
            public void onValueChange(final android.widget.NumberPicker picker, final int oldValue,
                                      final int newValue) {
                int value = 0;

                for (int i = 0; i < numberPickers.length; i++) {
                    int exponent = numberPickers.length - i - 1;
                    value += numberPickers[i].getValue() * Math.pow(NUMERIC_SYTEM, exponent);
                }

                currentNumber = value;
            }

        };
    }

    /**
     * Returns the the digit, which corresponds to a specific index of a number.
     *
     * @param index
     *         The index of the digit, which should be retrieved, as an {@link Integer} value
     * @param number
     *         The number, which contains the digit, which should be retrieved, as an {@link
     *         Integer} value
     * @return The digit, which corresponds to the given index, as an {@link Integer} value
     */
    private int getDigit(final int index, final int number) {
        String format = "%0" + getNumberOfDigits() + "d";
        String formattedNumber = String.format(format, number);
        String digit = formattedNumber.substring(index, index + 1);
        return Integer.valueOf(digit);
    }

    /**
     * Returns the maximum number, which can be created with a specific number of digits.
     *
     * @param numberOfDigits
     *         The number of digits as an {@link Integer} value
     * @return The maximum number, which can be created with the given number of digits, as an
     * {@link Integer} value
     */
    private int getMaxNumber(final int numberOfDigits) {
        int result = 0;

        for (int i = 0; i < numberOfDigits; i++) {
            result += (NUMERIC_SYTEM - 1) * Math.pow(NUMERIC_SYTEM, i);
        }

        return result;
    }

    /**
     * Returns the current number of the {@link NumberPicker} widget.
     *
     * @return The current number of the {@link NumberPicker} widget as an {@link Integer} value
     */
    protected final int getCurrentNumber() {
        return currentNumber;
    }

    /**
     * Creates a new preference, which allows to choose a decimal number via multiple {@link
     * NumberPicker} widgets.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public DigitPickerPreference(@NonNull final Context context) {
        super(context);
        initialize(null);
    }

    /**
     * Creates a new preference, which allows to choose a decimal number via multiple {@link
     * NumberPicker} widgets.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public DigitPickerPreference(@NonNull final Context context,
                                 @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(attributeSet);
    }

    /**
     * Creates a new preference, which allows to choose a decimal number via multiple {@link
     * NumberPicker} widgets.
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
    public DigitPickerPreference(@NonNull final Context context,
                                 @Nullable final AttributeSet attributeSet,
                                 final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet);
    }

    /**
     * Creates a new preference, which allows to choose a decimal number via multiple {@link
     * NumberPicker} widgets.
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
    public DigitPickerPreference(@NonNull final Context context,
                                 @Nullable final AttributeSet attributeSet, final int defaultStyle,
                                 final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet);
    }

    /**
     * Returns the number of digits of the numbers, the preference allows to choose.
     *
     * @return The number of digits of the numbers, the preference allows to choose, as an {@link
     * Integer} value
     */
    public final int getNumberOfDigits() {
        return numberOfDigits;
    }

    /**
     * Sets the number of digits of the numbers, the preference should allow to choose.
     *
     * @param numberOfDigits
     *         The number of digits, which should be set, as an {@link Integer} value. The number of
     *         digits must be at least 1
     */
    public final void setNumberOfDigits(final int numberOfDigits) {
        ensureAtLeast(numberOfDigits, 1, "The number of digits must be at least 1");
        this.numberOfDigits = numberOfDigits;
        setNumber(Math.min(getNumber(), getMaxNumber(numberOfDigits)));
    }

    @Override
    public final void setNumber(final int number) {
        ensureAtMaximum(Integer.toString(number).length(), getNumberOfDigits(),
                "The number must have at maximum " + getNumberOfDigits() + " digits");
        currentNumber = number;
        super.setNumber(number);
    }

    @Override
    public final void useInputMethod(final boolean useInputMethod) {
        super.useInputMethod(useInputMethod);

        if (numberPickers != null) {
            for (NumberPicker numberPicker : numberPickers) {
                numberPicker.setDescendantFocusability(
                        useInputMethod ? NumberPicker.FOCUS_BEFORE_DESCENDANTS :
                                NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }
        }
    }

    @Override
    public final void wrapSelectorWheel(final boolean wrapSelectorWheel) {
        super.wrapSelectorWheel(wrapSelectorWheel);

        if (numberPickers != null) {
            for (NumberPicker numberPicker : numberPickers) {
                numberPicker.setWrapSelectorWheel(wrapSelectorWheel);
            }
        }
    }

    @Override
    protected final void onPrepareDialog(@NonNull final MaterialDialogBuilder dialogBuilder) {
        int digitPickerWidth =
                getContext().getResources().getDimensionPixelSize(R.dimen.digit_picker_width);
        LayoutParams layoutParams = new LayoutParams(digitPickerWidth, LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(), R.layout.number_picker, null);
        LinearLayout container = (LinearLayout) view.findViewById(R.id.number_picker_container);
        numberPickers = new NumberPicker[getNumberOfDigits()];

        for (int i = 0; i < getNumberOfDigits(); i++) {
            NumberPicker numberPicker = new NumberPicker(getContext());
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(NUMERIC_SYTEM - 1);
            numberPicker.setValue(getDigit(i, getCurrentNumber()));
            numberPicker.setWrapSelectorWheel(isSelectorWheelWrapped());
            numberPicker.setDescendantFocusability(
                    isInputMethodUsed() ? NumberPicker.FOCUS_BEFORE_DESCENDANTS :
                            NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numberPicker.setOnValueChangedListener(createNumberPickerListener());
            numberPickers[i] = numberPicker;
            container.addView(numberPicker, i, layoutParams);
        }

        TextView unitTextView = (TextView) container.findViewById(R.id.unit_text_view);
        unitTextView.setText(getUnit());
        unitTextView.setVisibility(TextUtils.isEmpty(getUnit()) ? View.GONE : View.VISIBLE);

        dialogBuilder.setView(view);
    }

    @Override
    protected final void onDialogClosed(final boolean positiveResult) {
        if (positiveResult && callChangeListener(getCurrentNumber())) {
            setNumber(getCurrentNumber());
        } else {
            currentNumber = getNumber();
        }

        numberPickers = null;
    }

    @Override
    protected final Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentNumber = getCurrentNumber();
        return savedState;
    }

    @Override
    protected final void onRestoreInstanceState(final Parcelable state) {
        if (state != null && state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            currentNumber = savedState.currentNumber;
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}