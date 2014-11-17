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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import de.mrapp.android.dialog.MaterialDialogBuilder;
import de.mrapp.android.preference.view.SeekBar;

/**
 * A preference, which allows to select a value from a continuous range via a
 * seek bar. When interacting with the preference, the seek bar is shown within
 * a dialog. The chosen value will only be persisted, if confirmed by the user.
 * The preference can be used to select floating point values if a specific
 * number of decimals or integer values. Furthermore it is possible to customize
 * the appearance of the dialog and to set a step size, the currently chosen
 * value is increased or decreased by, when moving the seek bar.
 * 
 * @author Michael Rapp
 * 
 * @since 1.0.0
 */
public class SeekBarPreference extends AbstractDialogPreference {

	/**
	 * A data structure, which allows to save the internal state of a
	 * {@link SeekBarPreference}.
	 */
	public static class SavedState extends BaseSavedState {

		/**
		 * A creator, which allows to create instances of the class
		 * {@link SeekBarPreference} from parcels.
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
		 * The saved value of the attribute "value".
		 */
		public float value;

		/**
		 * The saved value of the attribute "seekBarValue".
		 */
		public float seekBarValue;

		/**
		 * The saved value of the attribute "minValue".
		 */
		public int minValue;

		/**
		 * The saved value of the attribute "maxValue".
		 */
		public int maxValue;

		/**
		 * The saved value of the attribute "stepSize".
		 */
		public int stepSize;

		/**
		 * The saved value of the attribute "decimals".
		 */
		public int decimals;

		/**
		 * The saved value of the attribute "suffix".
		 */
		public String suffix;

		/**
		 * The saved value of the attribute "floatingPointSeparator".
		 */
		public String floatingPointSeparator;

		/**
		 * The saved value of the attribute "summaries".
		 */
		public String[] summaries;

		/**
		 * The saved value of the attribute "showProgress".
		 */
		public boolean showProgress;

		/**
		 * Creates a new data structure, which allows to store the internal
		 * state of a {@link SeekBarPreference}. This constructor is called by
		 * derived classes when saving their states.
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
		 * state of a {@link SeekBarPreference}. This constructor is used when
		 * reading from a parcel. It reads the state of the superclass.
		 * 
		 * @param source
		 *            The parcel to read read from as a instance of the class
		 *            {@link Parcel}
		 */
		public SavedState(final Parcel source) {
			super(source);
			value = source.readFloat();
			seekBarValue = source.readFloat();
			minValue = source.readInt();
			maxValue = source.readInt();
			stepSize = source.readInt();
			decimals = source.readInt();
			suffix = source.readString();
			floatingPointSeparator = source.readString();
			showProgress = source.readByte() != 0;
			summaries = source.createStringArray();
		}

		@Override
		public final void writeToParcel(final Parcel destination,
				final int flags) {
			super.writeToParcel(destination, flags);
			destination.writeFloat(value);
			destination.writeFloat(seekBarValue);
			destination.writeInt(minValue);
			destination.writeInt(maxValue);
			destination.writeInt(stepSize);
			destination.writeInt(decimals);
			destination.writeString(suffix);
			destination.writeString(floatingPointSeparator);
			destination.writeByte((byte) (showProgress ? 1 : 0));
			destination.writeStringArray(summaries);
		}

	};

	/**
	 * The numeric system, which is used.
	 */
	private static final double NUMERIC_SYSTEM = 10.0d;

	/**
	 * The default value of the seek bar.
	 */
	protected static final float DEFAULT_VALUE = 50.0f;

	/**
	 * The default minimum value of the seek bar.
	 */
	protected static final int DEFAULT_MIN_VALUE = 0;

	/**
	 * The default maximum value of the seek bar.
	 */
	protected static final int DEFAULT_MAX_VALUE = 100;

	/**
	 * The default step size, the value is increased or decreased by when moving
	 * the seek bar.
	 */
	protected static final int DEFAULT_STEP_SIZE = -1;

	/**
	 * The default number of decimal numbers.
	 */
	protected static final int DEFAULT_DECIMALS = 1;

	/**
	 * The default suffix, which is attached to the current value for textual
	 * representation.
	 */
	protected static final String DEFAULT_SUFFIX = null;

	/**
	 * The default symbol, which is used to separate floating point numbers for
	 * textual representation.
	 */
	protected static final String DEFAULT_FLOATING_POINT_SEPARATOR = null;

	/**
	 * The default value, which specifies, whether the progress of the seek bar
	 * should be shown, or not.
	 */
	protected static final boolean DEFAULT_SHOW_PROGRESS = true;

	/**
	 * The default, which are shown depending on the currently persisted value.
	 */
	protected static final String[] DEFAULT_SUMMARIES = null;

	/**
	 * The currently persisted value.
	 */
	private float value;

	/**
	 * The current value of the seek bar.
	 */
	private float seekBarValue;

	/**
	 * The maximum value of the seek bar.
	 */
	private int minValue;

	/**
	 * The minimum value of the seek bar.
	 */
	private int maxValue;

	/**
	 * The default value of the seek bar.
	 */
	private float defaultValue;

	/**
	 * The step size, the value is increased or decreased by when moving the
	 * seek bar.
	 */
	private int stepSize;

	/**
	 * The number of decimal numbers of the floating point numbers, the
	 * preference allows to choose.
	 */
	private int decimals;

	/**
	 * The suffix, which is attached to the current value for textual
	 * representation.
	 */
	private String suffix;

	/**
	 * The separator, which is used to show floating point values.
	 */
	private String floatingPointSeparator;

	/**
	 * A string array, which contains the summaries, which should be shown
	 * depending on the currently persisted value.
	 */
	private String[] summaries;

	/**
	 * True, if the progress of the seek bar should be shown, false otherwise.
	 */
	private boolean showProgress;

	/**
	 * Initializes the preference.
	 * 
	 * @param context
	 *            The context, which should be used to obtain the attributes, as
	 *            an instance of the class {@link Context}
	 * @param attributeSet
	 *            The attribute set, the attributes should be obtained from, as
	 *            an instance of the type {@link AttributeSet}
	 */
	private void initialize(final Context context,
			final AttributeSet attributeSet) {
		obtainStyledAttributes(context, attributeSet);
		setValue(getPersistedFloat(defaultValue));
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
	}

	/**
	 * Obtains all attributes from a specific attribute set.
	 * 
	 * @param context
	 *            The context, which should be used to obtain the attributes, as
	 *            an instance of the class {@link Context}
	 * @param attributeSet
	 *            The attribute set, the attributes should be obtained from, as
	 *            an instance of the type {@link AttributeSet}
	 */
	private void obtainStyledAttributes(final Context context,
			final AttributeSet attributeSet) {
		TypedArray typedArray = context.obtainStyledAttributes(attributeSet,
				R.styleable.SeekBarPreference);
		try {
			obtainDecimals(typedArray);
			obtainMaxValue(typedArray);
			obtainMinValue(typedArray);
			obtainDefaultValue(typedArray);
			obtainStepSize(typedArray);
			obtainSuffix(typedArray);
			obtainFloatingPointSeparator(typedArray);
			obtainShowProgress(typedArray);
			obtainSummaries(typedArray);
		} finally {
			typedArray.recycle();
		}
	}

	/**
	 * Obtains the number of decimals of the floating point numbers, the
	 * preference allows to choose, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the number of decimals should be obtained
	 *            from, as an instance of the class {@link TypedArray}
	 */
	private void obtainDecimals(final TypedArray typedArray) {
		setDecimals(typedArray.getInteger(
				R.styleable.SeekBarPreference_decimals, DEFAULT_DECIMALS));
	}

	/**
	 * Obtains the minimum value, the preference allows to choose, from a
	 * specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the minimum value should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainMinValue(final TypedArray typedArray) {
		setMinValue(typedArray.getInteger(R.styleable.SeekBarPreference_min,
				DEFAULT_MIN_VALUE));
	}

	/**
	 * Obtains the maximum value, the preference allows to choose, from a
	 * specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the maximum value should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainMaxValue(final TypedArray typedArray) {
		setMaxValue(typedArray.getInteger(
				R.styleable.SeekBarPreference_android_max, DEFAULT_MAX_VALUE));
	}

	/**
	 * Obtains the default value of the preference from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the default value should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainDefaultValue(final TypedArray typedArray) {
		defaultValue = typedArray.getFloat(
				R.styleable.SeekBarPreference_android_defaultValue,
				DEFAULT_VALUE);
	}

	/**
	 * Obtains the step size, the value is increased or decreased by when moving
	 * the seek bar, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the step size should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainStepSize(final TypedArray typedArray) {
		setStepSize(typedArray.getInteger(
				R.styleable.SeekBarPreference_stepSize, DEFAULT_STEP_SIZE));
	}

	/**
	 * Obtains the suffix, which is appended to the current value for textual
	 * representation, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the suffix should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainSuffix(final TypedArray typedArray) {
		String obtainedSuffix = typedArray
				.getString(R.styleable.SeekBarPreference_suffix);
		setSuffix(obtainedSuffix != null ? obtainedSuffix : DEFAULT_SUFFIX);
	}

	/**
	 * Obtains the symbol, which is used to separate floating point numbers for
	 * textual representation, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the symbol should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainFloatingPointSeparator(final TypedArray typedArray) {
		String obtainedFloatingPointSeparator = typedArray
				.getString(R.styleable.SeekBarPreference_floatingPointSeparator);
		setFloatingPointSeparator(obtainedFloatingPointSeparator != null ? obtainedFloatingPointSeparator
				: DEFAULT_FLOATING_POINT_SEPARATOR);
	}

	/**
	 * Obtains the boolean value, which specifies whether the progress of the
	 * seek bar should be shown, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the boolean value should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainShowProgress(final TypedArray typedArray) {
		if (typedArray != null) {
			showProgress(typedArray.getBoolean(
					R.styleable.SeekBarPreference_showProgress,
					DEFAULT_SHOW_PROGRESS));
		} else {
			showProgress(DEFAULT_SHOW_PROGRESS);
		}
	}

	/**
	 * Obtains the summaries, which are shown depending on the currently
	 * persisted value, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the summaries should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainSummaries(final TypedArray typedArray) {
		try {
			CharSequence[] charSequences = typedArray
					.getTextArray(R.styleable.SeekBarPreference_android_summary);

			String[] obtainedSummaries = new String[charSequences.length];

			for (int i = 0; i < charSequences.length; i++) {
				obtainedSummaries[i] = charSequences[i].toString();
			}

			setSummaries(obtainedSummaries);
		} catch (NullPointerException e) {
			setSummaries(DEFAULT_SUMMARIES);
		}
	}

	/**
	 * Rounds a specific value to the number of decimals, which are currently
	 * set.
	 * 
	 * @param value
	 *            The value, which should be rounded, as a {@link Float} value
	 * @return The rounded value as a {@link Float} value
	 */
	private float roundToDecimals(final float value) {
		return (float) (Math.round(getMultiplier() * value) / (float) getMultiplier());
	}

	/**
	 * Returns the value, a floating point value has to be multiplied with to
	 * transform it to an integer value which is able to encode all of its
	 * decimals. By dividing such an integer value by the return value of this
	 * method, the integer value can be transformed back to the original
	 * floating point value.
	 * 
	 * @return The multiplier as an {@link Integer} value
	 */
	private int getMultiplier() {
		return (int) Math.pow(NUMERIC_SYSTEM, getDecimals());
	}

	/**
	 * Creates and returns a listener, which allows to display the currently
	 * chosen value of the pereference's seek bar. The current value is
	 * internally stored, but it will not become persisted, until the user's
	 * confirmation.
	 * 
	 * @param progressTextView
	 *            The text view, which should be used to display the currently
	 *            chosen value, as an instance of the class {@link TextView}.
	 *            The text view may not be null
	 * @return The listener, which has been created, as an instance of the type
	 *         {@link OnSeekBarChangeListener}
	 */
	private OnSeekBarChangeListener createSeekBarListener(
			final TextView progressTextView) {
		return new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(final android.widget.SeekBar seekBar) {
				return;
			}

			@Override
			public void onStartTrackingTouch(
					final android.widget.SeekBar seekBar) {
				return;
			}

			@Override
			public void onProgressChanged(final android.widget.SeekBar seekBar,
					final int progress, final boolean fromUser) {
				seekBarValue = (float) getMinValue() + (float) progress
						/ (float) getMultiplier();
				seekBarValue = adaptToStepSize(seekBarValue);
				progressTextView.setText(getProgressText());

			}

		};
	}

	/**
	 * Adapts a specific value to the step size, which is currently set. The
	 * value will be decreased to the nearest value, which matches the step
	 * size.
	 * 
	 * @param value
	 *            The value, which should be adapter to the step size, as a
	 *            {@link Float} value
	 * @return The adapted value as a {@link Float} value
	 */
	private float adaptToStepSize(final float value) {
		float result = value;

		if (getStepSize() != -1) {
			int minValueMod = getMinValue() % stepSize;
			float mod = result % stepSize;
			result = result - mod + minValueMod;
			result = Math.min(result, getMaxValue());
		}

		return result;
	}

	/**
	 * Returns a textual representation of the current value of the seek bar.
	 * The text is formatted depending on the decimal separator, which is
	 * currently set and contains the suffix, if currently set.
	 * 
	 * @return A textual representation of the current value of the seek bar as
	 *         a {@link String}
	 */
	private String getProgressText() {
		NumberFormat numberFormat = NumberFormat.getInstance();

		if (getFloatingPointSeparator() != null
				&& numberFormat instanceof DecimalFormat) {
			DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
			decimalFormatSymbols
					.setDecimalSeparator(getFloatingPointSeparator().charAt(0));
			((DecimalFormat) numberFormat)
					.setDecimalFormatSymbols(decimalFormatSymbols);
		}

		numberFormat.setMinimumFractionDigits(getDecimals());
		numberFormat.setMaximumFractionDigits(getDecimals());
		String valueString = numberFormat.format(seekBarValue);

		if (getSuffix() != null && getSuffix().length() > 0) {
			return valueString + " " + getSuffix();
		}

		return valueString;
	}

	/**
	 * Returns the current value of the seek bar.
	 * 
	 * @return The current value of the seek bar as a {@link Float} value
	 */
	protected final float getSeekBarValue() {
		return seekBarValue;
	}

	/**
	 * Creates a new preference, which allows to select a value from a
	 * continuous range via a seek bar.
	 * 
	 * @param context
	 *            The context in which to store the preference's value as an
	 *            instance of the class {@link Context}. The context may not be
	 *            null
	 */
	public SeekBarPreference(final Context context) {
		this(context, null);
	}

	/**
	 * Creates a new preference, which allows to select a value from a
	 * continuous range via a seek bar. This constructor is called when a
	 * preference is being constructed from an XML file, supplying attributes
	 * that were specified in the XML file. This version uses a default style of
	 * 0, so the only attribute values applied are those in the context's theme
	 * and the given attribute set.
	 * 
	 * @param context
	 *            The Context this preference is associated with and through
	 *            which it can access the current theme, resources,
	 *            SharedPreferences, etc, as an instance of the class
	 *            {@link Context}. The context may not be null
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the
	 *            preference, as an instance of the type {@link AttributeSet}.
	 *            The attribute set may not be null
	 */
	public SeekBarPreference(final Context context,
			final AttributeSet attributeSet) {
		super(context, attributeSet);
		initialize(context, attributeSet);
	}

	/**
	 * Creates a new preference, which allows to select a value from a
	 * continuous range via a seek bar. This constructor allows subclasses to
	 * use their own base style when they are inflating.
	 * 
	 * @param context
	 *            The context in which to store Preference value as an instance
	 *            of the class {@link Context}
	 * @param attributeSet
	 *            The attribute set, the preference's attributes should be
	 *            obtained from, as an instance of the type {@link AttributeSet}
	 * @param defaultStyle
	 *            The default style to apply to this preference. If 0, no style
	 *            will be applied (beyond what is included in the theme). This
	 *            may either be an attribute resource, whose value will be
	 *            retrieved from the current theme, or an explicit style
	 *            resource
	 */
	public SeekBarPreference(final Context context,
			final AttributeSet attributeSet, final int defaultStyle) {
		super(context, attributeSet, defaultStyle);
		initialize(context, attributeSet);
	}

	/**
	 * Creates a new preference, which allows to select a value from a
	 * continuous range via a seek bar. This constructor allows subclasses to
	 * use their own base style when they are inflating.
	 * 
	 * @param context
	 *            The context in which to store Preference value as an instance
	 *            of the class {@link Context}
	 * @param attributeSet
	 *            The attribute set, the preference's attributes should be
	 *            obtained from, as an instance of the type {@link AttributeSet}
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
	public SeekBarPreference(final Context context,
			final AttributeSet attributeSet, final int defaultStyle,
			final int defaultStyleResource) {
		super(context, attributeSet, defaultStyle, defaultStyleResource);
		initialize(context, attributeSet);
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
	 * Sets the current value of the preference. By setting a value, it will be
	 * persisted.
	 * 
	 * @param value
	 *            The value, which should be set, as a {@link Float} value. The
	 *            value must be between the minimum and the maximum value, the
	 *            preference allows to select
	 */
	public final void setValue(final float value) {
		ensureAtLeast(value, getMinValue(),
				"The value must be at least the minimum value");
		ensureAtMaximum(value, getMaxValue(),
				"The value must be at maximum the maximum value");
		float roundedValue = roundToDecimals(value);

		if (this.value != roundedValue) {
			this.value = roundedValue;
			this.seekBarValue = roundedValue;
			persistFloat(roundedValue);
			notifyChanged();
		}
	}

	/**
	 * Returns the minimum value, the preference allows to choose.
	 * 
	 * @return The minimum value, the preference allows to choose, as an
	 *         {@link Integer} value
	 */
	public final int getMinValue() {
		return minValue;
	}

	/**
	 * Sets the minimum value, the preference should allow to choose.
	 * 
	 * @param minValue
	 *            The minimum value, which should be set, as an {@link Integer}
	 *            value. The value must be between 0 and the maximum value, the
	 *            preference allows to choose
	 */
	public final void setMinValue(final int minValue) {
		ensureAtLeast(minValue, 0, "The minimum value must be at least 0");
		ensureLessThan(minValue, getMaxValue(),
				"The minimum value must be less than the maximum value");
		this.minValue = minValue;
		setValue(Math.max(getValue(), minValue));
	}

	/**
	 * Returns the maximum value, the preference allows to choose.
	 * 
	 * @return The maximum value, the preference allows to choose, as an
	 *         {@link Integer} value
	 */
	public final int getMaxValue() {
		return maxValue;
	}

	/**
	 * Sets the maximum value, the preference should allow to choose.
	 * 
	 * @param maxValue
	 *            The maximum value, which should be set, as an {@link Integer}
	 *            value. The value must be greater than the minimum value, that
	 *            preference allows to choose
	 */
	public final void setMaxValue(final int maxValue) {
		ensureGreaterThan(maxValue, getMinValue(),
				"The maximum value must be greater than the minimum value");
		this.maxValue = maxValue;
		setValue(Math.min(getValue(), maxValue));
	}

	/**
	 * Returns the default value of the preference.
	 * 
	 * @return The default value of the preference, as a {@link Float} value
	 */
	public final float getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Returns the range of values, the preference allows to choose from.
	 * 
	 * @return The range of values, the preference allows to choose from, as an
	 *         {@link Integer} value
	 */
	public final int getRange() {
		return maxValue - minValue;
	}

	/**
	 * Returns the step size, the value is increased or decreased by, when
	 * moving the seek bar.
	 * 
	 * @return The step size, the value is increased or decreased by, when
	 *         moving the seek bar, as an {@link Integer} value
	 */
	public final int getStepSize() {
		return stepSize;
	}

	/**
	 * Sets the step size, the value should be increased or decreased by, when
	 * moving the seek bar.
	 * 
	 * @param stepSize
	 *            The step size, which should be set, as an {@link Integer}
	 *            value. The value must be between 1 and the maximum value or
	 *            -1, if the preference should allow to select a value from a
	 *            continuous range
	 */
	public final void setStepSize(final int stepSize) {
		if (stepSize != -1) {
			ensureAtLeast(stepSize, 1, "The step size must be at least 1");
			ensureAtMaximum(stepSize, getMaxValue(),
					"The step size must be at maximum the maximum value");
		}
		this.stepSize = stepSize;
		setValue(adaptToStepSize(getValue()));
	}

	/**
	 * Returns the number of decimals of the floating point numbers, the
	 * preference allows to choose. If the number of decimals is set to 0, the
	 * preference only allows to choose integer values.
	 * 
	 * @return The number of decimals of the floating point numbers, the
	 *         preference allows to choose, as an {@link Integer} value
	 */
	public final int getDecimals() {
		return decimals;
	}

	/**
	 * Sets the number of decimals of the floating point numbers, the preference
	 * should allow to choose. If the number of decimals is set to 0, the
	 * preference will only allow to choose integer values.
	 * 
	 * @param decimals
	 *            The number of decimals, which should be set, as an
	 *            {@link Integer} value. The value must be at least 0. If the
	 *            value is set to 0, the preference will only allow to choose
	 *            integer values
	 */
	public final void setDecimals(final int decimals) {
		ensureAtLeast(decimals, 0, "The decimals must be at least 0");
		this.decimals = decimals;
		setValue(roundToDecimals(getValue()));
	}

	/**
	 * Returns the suffix, which is attached to the current value for textual
	 * representation.
	 * 
	 * @return The suffix, which is attached to the current value for textual
	 *         representation, as a {@link String} or null, if no suffix is
	 *         attached
	 */
	public final String getSuffix() {
		return suffix;
	}

	/**
	 * Sets the suffix, which should be attached to the current value for
	 * textual representation.
	 * 
	 * @param suffix
	 *            The suffix, which should be set, as a {@link String} or null,
	 *            if no suffix should be attached
	 */
	public final void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Sets the suffix, which should be attached to the current value for
	 * textual representation.
	 * 
	 * @param suffixResId
	 *            The resource id of the suffix, which should be set, as an
	 *            {@link Integer} value
	 */
	public final void setSuffix(final int suffixResId) {
		setSuffix(getContext().getResources().getString(suffixResId));
	}

	/**
	 * Returns the symbol, which is used to separate floating point numbers for
	 * textual representation.
	 * 
	 * @return The symbol, which is used to separate floating point numbers for
	 *         textual representation, as a {@link String}
	 */
	public final String getFloatingPointSeparator() {
		return floatingPointSeparator;
	}

	/**
	 * Sets the symbol, which should be used to separate floating point numbers
	 * for textual representation.
	 * 
	 * @param floatingPointSeparator
	 *            The symbol, which should be set, as a {@link String}. The
	 *            length of the string must be 1
	 */
	public final void setFloatingPointSeparator(
			final String floatingPointSeparator) {
		if (floatingPointSeparator != null) {
			ensureAtMaximum(floatingPointSeparator.length(), 1,
					"The floating point separator's length must be 1");
		}
		this.floatingPointSeparator = floatingPointSeparator;
	}

	/**
	 * Sets the symbol, which should be used to separate floating point numbers
	 * for textual representation.
	 * 
	 * @param floatingPointSeparatorResId
	 *            The resource id of the symbol, which should be set, as an
	 *            {@link Integer} value
	 */
	public final void setFloatingPointSeparator(
			final int floatingPointSeparatorResId) {
		setFloatingPointSeparator(getContext().getResources().getString(
				floatingPointSeparatorResId));
	}

	/**
	 * Returns, whether the currently selected value of the seek bar is shown,
	 * or not.
	 * 
	 * @return True, if the currently selected value of the seek bar is shown,
	 *         false otherwise
	 */
	public final boolean isProgressShown() {
		return showProgress;
	}

	/**
	 * Sets, whether the currently selected value of the seek bar should be
	 * shown, or not.
	 * 
	 * @param showProgress
	 *            True, if the currently selected value of the seek bar should
	 *            be shown, false otherwise
	 */
	public final void showProgress(final boolean showProgress) {
		this.showProgress = showProgress;
	}

	/**
	 * Returns the summaries, which are shown depending on the currently
	 * persisted value.
	 * 
	 * @return The summaries, which are shown depending on the currently
	 *         persisted value, as a {@link String} array or null, if no
	 *         summaries are shown depending on the currently persisted value
	 */
	public final String[] getSummaries() {
		return summaries;
	}

	/**
	 * Sets the summaries, which should be shown depending on the currently
	 * persisted value.
	 * 
	 * @param summaries
	 *            The summaries, which should be set, as a {@link String} array
	 *            or null, if no summaries should be shown depending on the
	 *            currently persisted value
	 */
	public final void setSummaries(final String[] summaries) {
		this.summaries = summaries;
	}

	@Override
	public final CharSequence getSummary() {
		if (isValueShownAsSummary()) {
			return getProgressText();
		} else if (getSummaries() != null && getSummaries().length > 0) {
			float interval = (float) getRange() / (float) getSummaries().length;
			int index = (int) Math.floor((getValue() - getMinValue())
					/ interval);
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
	public final void setSummary(final int summaryResId) {
		try {
			setSummaries(getContext().getResources().getStringArray(
					summaryResId));
		} catch (Exception e) {
			super.setSummary(summaryResId);
		}
	}

	@Override
	protected final Object onGetDefaultValue(final TypedArray a, final int index) {
		return a.getFloat(index, DEFAULT_VALUE);
	}

	@Override
	protected final void onSetInitialValue(final boolean restoreValue,
			final Object defaultValue) {
		if (restoreValue) {
			setValue(getPersistedFloat(DEFAULT_VALUE));
		} else {
			setValue((Float) defaultValue);
		}
	}

	@Override
	protected final boolean needInputMethod() {
		return false;
	}

	@Override
	protected final void onPrepareDialog(
			final MaterialDialogBuilder dialogBuilder) {
		View layout = View.inflate(getContext(), R.layout.seek_bar, null);

		TextView progressTextView = (TextView) layout
				.findViewById(R.id.progress_text);
		progressTextView.setText(getProgressText());
		progressTextView.setVisibility(isProgressShown() ? View.VISIBLE
				: View.GONE);

		SeekBar seekBar = (SeekBar) layout.findViewById(R.id.seek_bar);
		seekBar.setMax(getRange() * getMultiplier());
		seekBar.setProgress(Math.round((getValue() - getMinValue())
				* getMultiplier()));
		seekBar.setOnSeekBarChangeListener(createSeekBarListener(progressTextView));

		dialogBuilder.setView(layout);
	}

	@Override
	protected final void onDialogClosed(final boolean positiveResult) {
		if (positiveResult && callChangeListener(seekBarValue)) {
			setValue(seekBarValue);
		} else {
			seekBarValue = getValue();
		}
	}

	@Override
	protected final Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.value = value;
		savedState.seekBarValue = seekBarValue;
		savedState.minValue = minValue;
		savedState.maxValue = maxValue;
		savedState.stepSize = stepSize;
		savedState.decimals = decimals;
		savedState.suffix = suffix;
		savedState.floatingPointSeparator = floatingPointSeparator;
		savedState.showProgress = showProgress;
		savedState.summaries = summaries;
		return savedState;
	}

	@Override
	protected final void onRestoreInstanceState(final Parcelable state) {
		if (state != null && state instanceof SavedState) {
			SavedState savedState = (SavedState) state;
			value = savedState.value;
			seekBarValue = savedState.seekBarValue;
			minValue = savedState.minValue;
			maxValue = savedState.maxValue;
			stepSize = savedState.stepSize;
			decimals = savedState.decimals;
			suffix = savedState.suffix;
			floatingPointSeparator = savedState.floatingPointSeparator;
			showProgress = savedState.showProgress;
			summaries = savedState.summaries;
			super.onRestoreInstanceState(savedState.getSuperState());
		} else {
			super.onRestoreInstanceState(state);
		}
	}

}