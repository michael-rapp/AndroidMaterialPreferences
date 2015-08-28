/*
 * AndroidMaterialPreferences Copyright 2014 - 2015 Michael Rapp
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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

/**
 * An abstract base class for all preferences, which allow to choose decimal
 * numbers via {@link NumberPicker} widgets.
 * 
 * @author Michael Rapp
 * 
 * @since 1.1.0
 */
public abstract class AbstractNumberPickerPreference extends AbstractDialogPreference {

	/**
	 * A data structure, which allows to save the internal state of an
	 * {@link AbstractNumberPickerPreference}.
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
		 * The saved value of the attribute "number".
		 */
		public int number;

		/**
		 * The saved value of the attribute "defaultNumber".
		 */
		public int defaultNumber;

		/**
		 * The saved value of the attribute "useInputMethod".
		 */
		public boolean useInputMethod;

		/**
		 * The saved value of the attribute "wrapSelectorWheel".
		 */
		public boolean wrapSelectorWheel;

		/**
		 * Creates a new data structure, which allows to store the internal
		 * state of an {@link AbstractNumberPickerPreference}. This constructor
		 * is called by derived classes when saving their states.
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
		 * state of an {@link AbstractNumberPickerPreference}. This constructor
		 * is used when reading from a parcel. It reads the state of the
		 * superclass.
		 * 
		 * @param source
		 *            The parcel to read read from as a instance of the class
		 *            {@link Parcel}
		 */
		public SavedState(final Parcel source) {
			super(source);
			number = source.readInt();
			defaultNumber = source.readInt();
			useInputMethod = source.readInt() > 0;
			wrapSelectorWheel = source.readInt() > 0;
		}

		@Override
		public final void writeToParcel(final Parcel destination, final int flags) {
			super.writeToParcel(destination, flags);
			destination.writeInt(number);
			destination.writeInt(defaultNumber);
			destination.writeInt(useInputMethod ? 1 : 0);
			destination.writeInt(wrapSelectorWheel ? 1 : 0);
		}

	};

	/**
	 * The default number.
	 */
	protected static final int DEFAULT_NUMBER = 0;

	/**
	 * True, if an input method should be used by default, false otherwise.
	 */
	protected static final boolean DEFAULT_USE_INPUT_METHOD = false;

	/**
	 * True, if the selector wheel should be wrapped, false otherwise.
	 */
	protected static final boolean DEFAULT_WRAP_SELECTOR_WHEEL = true;

	/**
	 * The currently persisted number.
	 */
	private int number;

	/**
	 * The default number.
	 */
	private int defaultNumber;

	/**
	 * True, if an input method is used, false otherwise.
	 */
	private boolean useInputMethod;

	/**
	 * True, if the selector wheel is wrapped, false otherwise.
	 */
	private boolean wrapSelectorWheel;

	/**
	 * Initializes the preference.
	 * 
	 * @param attributeSet
	 *            The attribute set, the attributes should be obtained from, as
	 *            an instance of the type {@link AttributeSet}
	 */
	private void initialize(final AttributeSet attributeSet) {
		obtainStyledAttributes(attributeSet);
		setNumber(getPersistedInt(defaultNumber));
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
	}

	/**
	 * Obtains all attributes from a specific attribute set.
	 * 
	 * @param attributeSet
	 *            The attribute set, the attributes should be obtained from, as
	 *            an instance of the type {@link AttributeSet}
	 */
	private void obtainStyledAttributes(final AttributeSet attributeSet) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet,
				R.styleable.AbstractNumberPickerPreference);
		try {
			obtainDefaultNumber(typedArray);
			obtainUseInputMethod(typedArray);
			obtainWrapSelectorWheel(typedArray);
		} finally {
			typedArray.recycle();
		}
	}

	/**
	 * Obtains the default number of the preference from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the default number should be obtained from,
	 *            as an instance of the class {@link TypedArray}
	 */
	private void obtainDefaultNumber(final TypedArray typedArray) {
		defaultNumber = typedArray.getInteger(R.styleable.AbstractNumberPickerPreference_android_defaultValue,
				DEFAULT_NUMBER);
	}

	/**
	 * Obtains, whether an input method should be used, or not, from a specific
	 * typed array.
	 * 
	 * @param typedArray
	 *            The typed array, which should be used to retrieve, whether an
	 *            input method should be used, or not, as an instance of the
	 *            class {@link TypedArray}
	 */
	private void obtainUseInputMethod(final TypedArray typedArray) {
		useInputMethod(typedArray.getBoolean(R.styleable.AbstractNumberPickerPreference_useInputMethod,
				DEFAULT_USE_INPUT_METHOD));
	}

	/**
	 * Obtains, whether the selection wheel of the selection wheel of the
	 * preference's {@link NumberPicker} should be wrapped, or not, from a
	 * specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, which should be used to retrieve, whether the
	 *            selection wheel of the preference's {@link NumberPicker}
	 *            should be wrapped, or not, as an instance of the class
	 *            {@link TypedArray}
	 */
	private void obtainWrapSelectorWheel(final TypedArray typedArray) {
		wrapSelectorWheel(typedArray.getBoolean(R.styleable.AbstractNumberPickerPreference_wrapSelectorWheel,
				DEFAULT_WRAP_SELECTOR_WHEEL));
	}

	/**
	 * Creates a new preference, which allows to choose a decimal number via a
	 * {@link NumberPicker} widget.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 */
	public AbstractNumberPickerPreference(final Context context) {
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
	public AbstractNumberPickerPreference(final Context context, final AttributeSet attributeSet) {
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
	public AbstractNumberPickerPreference(final Context context, final AttributeSet attributeSet,
			final int defaultStyle) {
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
	public AbstractNumberPickerPreference(final Context context, final AttributeSet attributeSet,
			final int defaultStyle, final int defaultStyleResource) {
		super(context, attributeSet, defaultStyle, defaultStyleResource);
		initialize(attributeSet);
	}

	/**
	 * Returns the currently persisted number of the preference.
	 * 
	 * @return The currently persisted number as an {@link Integer} value
	 */
	public final int getNumber() {
		return number;
	}

	/**
	 * Sets the current number of the preference. By setting a value, it will be
	 * persisted.
	 * 
	 * @param number
	 *            The number, which should be set, as an {@link Integer} value
	 */
	public void setNumber(final int number) {
		if (this.number != number) {
			this.number = number;
			persistInt(number);
			notifyChanged();
		}
	}

	/**
	 * Returns the default number of the preference.
	 * 
	 * @return The default number of the preference as an {@link Integer} value
	 */
	public final int getDefaultNumber() {
		return defaultNumber;
	}

	/**
	 * Returns, whether an input method is used by the preference, false
	 * otherwise.
	 * 
	 * @return True, if an input method is used, false otherwise
	 */
	public final boolean isInputMethodUsed() {
		return useInputMethod;
	}

	/**
	 * Sets, whether an input method should be used by the preference, or not.
	 * 
	 * @param useInputMethod
	 *            True, if an input method should be used, false otherwise
	 */
	public void useInputMethod(final boolean useInputMethod) {
		this.useInputMethod = useInputMethod;
	}

	/**
	 * Returns, whether the selector wheel of the preference's
	 * {@link NumberPicker} is wrapped, or not.
	 * 
	 * @return True, if the selector wheel of the preference's
	 *         {@link NumberPicker} is wrapped, false otherwise
	 */
	public final boolean isSelectorWheelWrapped() {
		return wrapSelectorWheel;
	}

	/**
	 * Sets, whether the selector wheel of the preference's {@link NumberPicker}
	 * should be wrapped, or not.
	 * 
	 * @param wrapSelectorWheel
	 *            True, if the selector wheel of the preference's
	 *            {@link NumberPicker} should be wrapped, false otherwise
	 */
	public void wrapSelectorWheel(final boolean wrapSelectorWheel) {
		this.wrapSelectorWheel = wrapSelectorWheel;
	}

	@Override
	public final CharSequence getSummary() {
		if (isValueShownAsSummary()) {
			return Integer.toString(getNumber());
		} else {
			return super.getSummary();
		}
	}

	@Override
	protected final boolean needInputMethod() {
		return isInputMethodUsed();
	}

	@Override
	protected final Object onGetDefaultValue(final TypedArray typedArray, final int index) {
		return typedArray.getInt(index, DEFAULT_NUMBER);
	}

	@Override
	protected final void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
		if (restoreValue) {
			setNumber(getPersistedInt(DEFAULT_NUMBER));
		} else {
			setNumber((Integer) defaultValue);
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable parcelable = super.onSaveInstanceState();
		SavedState savedState = new SavedState(parcelable);
		savedState.number = getNumber();
		savedState.defaultNumber = getDefaultNumber();
		savedState.useInputMethod = isInputMethodUsed();
		savedState.wrapSelectorWheel = isSelectorWheelWrapped();
		return savedState;
	}

	@Override
	protected void onRestoreInstanceState(final Parcelable state) {
		if (state != null && state instanceof SavedState) {
			SavedState savedState = (SavedState) state;
			number = savedState.number;
			defaultNumber = savedState.defaultNumber;
			useInputMethod = savedState.useInputMethod;
			wrapSelectorWheel = savedState.wrapSelectorWheel;
			super.onRestoreInstanceState(savedState.getSuperState());
		} else {
			super.onRestoreInstanceState(state);
		}
	}

}