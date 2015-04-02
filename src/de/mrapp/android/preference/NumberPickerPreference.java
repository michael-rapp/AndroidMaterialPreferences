/*
 * AndroidMaterialPreferences Copyright 2014 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>. 
 */
package de.mrapp.android.preference;

import static de.mrapp.android.preference.util.Condition.ensureAtLeast;
import static de.mrapp.android.preference.util.Condition.ensureAtMaximum;
import static de.mrapp.android.preference.util.Condition.ensureGreaterThan;
import static de.mrapp.android.preference.util.Condition.ensureLessThan;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker.OnValueChangeListener;
import de.mrapp.android.dialog.MaterialDialogBuilder;
import de.mrapp.android.preference.view.NumberPicker;

/**
 * A preference, which allows to choose a decimal number via a
 * {@link NumberPicker} widget. The chosen number will only be persisted, if
 * confirmed by the user.
 * 
 * @author Michael Rapp
 *
 * @since 1.1.0
 */
public class NumberPickerPreference extends AbstractNumberPickerPreference {

	/**
	 * A data structure, which allows to save the internal state of an
	 * {@link NumberPickerPreference}.
	 */
	public static class SavedState extends BaseSavedState {

		/**
		 * A creator, which allows to create instances of the class
		 * {@link SavedState} from parcels.
		 */
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

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
		 * The saved value of the attribute "minNumber".
		 */
		public int minNumber;

		/**
		 * The saved value of the attribute "maxNumber".
		 */
		public int maxNumber;

		/**
		 * Creates a new data structure, which allows to store the internal
		 * state of an {@link NumberPickerPreference}. This constructor is
		 * called by derived classes when saving their states.
		 * 
		 * @param superState
		 *            The state of the superclass of this view, as an instance
		 *            of the type {@link Parcelable}
		 */
		public SavedState(final Parcelable superState) {
			super(superState);
		}

		/**
		 * Creates a new data structure, which allows to store the internal
		 * state of an {@link NumberPickerPreference}. This constructor is used
		 * when reading from a parcel. It reads the state of the superclass.
		 * 
		 * @param source
		 *            The parcel to read read from as a instance of the class
		 *            {@link Parcel}
		 */
		public SavedState(final Parcel source) {
			super(source);
			currentNumber = source.readInt();
			minNumber = source.readInt();
			maxNumber = source.readInt();
		}

		@Override
		public final void writeToParcel(final Parcel destination,
				final int flags) {
			super.writeToParcel(destination, flags);
			destination.writeInt(currentNumber);
			destination.writeInt(minNumber);
			destination.writeInt(maxNumber);
		}

	};

	/**
	 * The default minimum number, the preference allows to choose.
	 */
	protected static final int DEFAULT_MIN_NUMBER = 0;

	/**
	 * The default maximum number, the preference allows to choose.
	 */
	protected static final int DEFAULT_MAX_NUMBER = 10;

	/**
	 * The {@link NumberPicker} widget, which allows to choose a decimal number.
	 */
	private NumberPicker numberPicker;

	/**
	 * The current number of the {@link NumberPicker} widget.
	 */
	private int currentNumber;

	/**
	 * The minimum number, the preference allows to choose.
	 */
	private int minNumber;

	/**
	 * The maximum number, the preference allows to choose.
	 */
	private int maxNumber;

	/**
	 * Initializes the preference.
	 * 
	 * @param attributeSet
	 *            The attribute set, the attributes should be obtained from, as
	 *            an instance of the type {@link AttributeSet}
	 */
	private void initialize(final AttributeSet attributeSet) {
		obtainStyledAttributes(attributeSet);
	}

	/**
	 * Obtains all attributes from a specific attribute set.
	 * 
	 * @param attributeSet
	 *            The attribute set, the attributes should be obtained from, as
	 *            an instance of the type {@link AttributeSet}
	 */
	private void obtainStyledAttributes(final AttributeSet attributeSet) {
		TypedArray typedArray = getContext().obtainStyledAttributes(
				attributeSet, R.styleable.NumberPickerPreference);
		try {
			obtainMaxNumber(typedArray);
			obtainMinNumber(typedArray);
		} finally {
			typedArray.recycle();
		}
	}

	/**
	 * Obtains the maximum number, the preference allows to choose, from a
	 * specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the maximum number should be obtained from,
	 *            as an instance of the class {@link TypedArray}
	 */
	private void obtainMaxNumber(final TypedArray typedArray) {
		setMaxNumber(typedArray.getInteger(
				R.styleable.NumberPickerPreference_maxNumber,
				DEFAULT_MAX_NUMBER));
	}

	/**
	 * Obtains the minimum number, the preference allows to choose, from a
	 * specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the minimum number should be obtained from,
	 *            as an instance of the class {@link TypedArray}
	 */
	private void obtainMinNumber(final TypedArray typedArray) {
		setMinNumber(typedArray.getInteger(
				R.styleable.NumberPickerPreference_minNumber,
				DEFAULT_MIN_NUMBER));
	}

	/**
	 * Creates and returns a listener, which allows to observe the
	 * {@link NumberPicker}, which is used by the preference.
	 * 
	 * @return The listener, which has been created, as an
	 *         {@link OnValueChangeListener}
	 */
	private OnValueChangeListener createNumberPickerListener() {
		return new OnValueChangeListener() {

			@Override
			public void onValueChange(
					final android.widget.NumberPicker numberPicker,
					final int oldValue, final int newValue) {
				currentNumber = newValue;
			}

		};
	}

	/**
	 * Returns the current number of the {@link NumberPicker} widget.
	 * 
	 * @return The current number of the {@link NumberPicker} widget as an
	 *         {@link Integer} value
	 */
	protected final int getCurrentNumber() {
		return currentNumber;
	}

	/**
	 * Creates a new preference, which allows to choose a decimal number via a
	 * {@link NumberPicker} widget.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 */
	public NumberPickerPreference(final Context context) {
		super(context);
		initialize(null);
	}

	/**
	 * Creates a new preference, which allows to choose a decimal number via a
	 * {@link NumberPicker} widget.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the
	 *            preference, as an instance of the type {@link AttributeSet}
	 */
	public NumberPickerPreference(final Context context,
			final AttributeSet attributeSet) {
		super(context, attributeSet);
		initialize(attributeSet);
	}

	/**
	 * Creates a new preference, which allows to choose a decimal number via a
	 * {@link NumberPicker} widget.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the
	 *            preference, as an instance of the type {@link AttributeSet}
	 * @param defaultStyle
	 *            The default style to apply to this preference. If 0, no style
	 *            will be applied (beyond what is included in the theme). This
	 *            may either be an attribute resource, whose value will be
	 *            retrieved from the current theme, or an explicit style
	 *            resource
	 */
	public NumberPickerPreference(final Context context,
			final AttributeSet attributeSet, final int defaultStyle) {
		super(context, attributeSet, defaultStyle);
		initialize(attributeSet);
	}

	/**
	 * Creates a new preference, which allows to choose a decimal number via a
	 * {@link NumberPicker} widget.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the
	 *            preference, as an instance of the type {@link AttributeSet}
	 * @param defaultStyle
	 *            The default style to apply to this preference. If 0, no style
	 *            will be applied (beyond what is included in the theme). This
	 *            may either be an attribute resource, whose value will be
	 *            retrieved from the current theme, or an explicit style
	 *            resource
	 * @param defaultStyleResource
	 *            A resource identifier of a style resource that supplies
	 *            default values for the preference, used only if the default
	 *            style is 0 or can not be found in the theme. Can be 0 to not
	 *            look for defaults
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public NumberPickerPreference(final Context context,
			final AttributeSet attributeSet, final int defaultStyle,
			final int defaultStyleResource) {
		super(context, attributeSet, defaultStyle, defaultStyleResource);
		initialize(attributeSet);
	}

	/**
	 * Returns the minimum number, the preference allows to choose.
	 * 
	 * @return The minimum number, the preference allows to choose, as an
	 *         {@link Integer} value
	 */
	public final int getMinNumber() {
		return minNumber;
	}

	/**
	 * Sets the minimum number, the preference allows to choose.
	 * 
	 * @param minNumber
	 *            The minimum number, which should be set, as an {@link Integer}
	 *            value. The number must be less than the maximum number
	 */
	public final void setMinNumber(final int minNumber) {
		ensureAtLeast(minNumber, 0, "The minimum number must be at least 0");
		ensureLessThan(minNumber, getMaxNumber(),
				"The minimum number must be less than the maximum number");
		this.minNumber = minNumber;
		setNumber(Math.max(getNumber(), minNumber));
	}

	/**
	 * Returns the maximum number, the preference allows to choose.
	 * 
	 * @return The maximum number, the preference allows to choose, as an
	 *         {@link Integer} value
	 */
	public final int getMaxNumber() {
		return maxNumber;
	}

	/**
	 * Sets the maximum number, the preference allows to choose.
	 * 
	 * @param maxNumber
	 *            The maximum number, which should be set, as an {@link Integer}
	 *            value. The number must be greater than the minimum number
	 */
	public final void setMaxNumber(final int maxNumber) {
		ensureGreaterThan(maxNumber, getMinNumber(),
				"The maximum number must be greater than the minimum number");
		this.maxNumber = maxNumber;
		setNumber(Math.min(getNumber(), maxNumber));
	}

	/**
	 * Returns the range of numbers, the preference allows to choose from.
	 * 
	 * @return The range of numbers, the preference allows to choose from, as an
	 *         {@link Integer} value
	 */
	public final int getRange() {
		return maxNumber - minNumber;
	}

	@Override
	public final void setNumber(final int number) {
		ensureAtLeast(number, getMinNumber(),
				"The number must be at least the minimum number");
		ensureAtMaximum(number, getMaxNumber(),
				"The number must be at maximum the maximum number");
		currentNumber = number;
		super.setNumber(number);
	}

	@Override
	public final void useInputMethod(final boolean useInputMethod) {
		super.useInputMethod(useInputMethod);

		if (numberPicker != null) {
			numberPicker
					.setDescendantFocusability(useInputMethod ? NumberPicker.FOCUS_BEFORE_DESCENDANTS
							: NumberPicker.FOCUS_BLOCK_DESCENDANTS);
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
	protected final void onPrepareDialog(
			final MaterialDialogBuilder dialogBuilder) {
		View view = View.inflate(getContext(), R.layout.number_picker, null);
		LinearLayout container = (LinearLayout) view
				.findViewById(R.id.number_picker_container);

		numberPicker = new NumberPicker(getContext());
		numberPicker.setMinValue(getMinNumber());
		numberPicker.setMaxValue(getMaxNumber());
		numberPicker.setValue(getCurrentNumber());
		numberPicker.setWrapSelectorWheel(isSelectorWheelWrapped());
		numberPicker
				.setDescendantFocusability(isInputMethodUsed() ? NumberPicker.FOCUS_BEFORE_DESCENDANTS
						: NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		numberPicker.setOnValueChangedListener(createNumberPickerListener());
		container.addView(numberPicker, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		dialogBuilder.setView(view);
	}

	@Override
	protected final void onDialogClosed(final boolean positiveResult) {
		if (positiveResult && callChangeListener(currentNumber)) {
			setNumber(currentNumber);
		} else {
			currentNumber = getNumber();
		}

		numberPicker = null;
	}

	@Override
	protected final Parcelable onSaveInstanceState() {
		Parcelable parcelable = super.onSaveInstanceState();
		SavedState savedState = new SavedState(parcelable);
		savedState.currentNumber = getCurrentNumber();
		savedState.minNumber = getMinNumber();
		savedState.maxNumber = getMaxNumber();
		return savedState;
	}

	@Override
	protected final void onRestoreInstanceState(final Parcelable state) {
		if (state != null && state instanceof SavedState) {
			SavedState savedState = (SavedState) state;
			currentNumber = savedState.currentNumber;
			minNumber = savedState.minNumber;
			maxNumber = savedState.maxNumber;
			super.onRestoreInstanceState(savedState.getSuperState());
		} else {
			super.onRestoreInstanceState(state);
		}
	}

}