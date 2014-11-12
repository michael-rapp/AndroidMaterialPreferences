package de.mrapp.android.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import de.mrapp.android.dialog.MaterialDialogBuilder;

public class ListPreference extends AbstractDialogPreference {

	public static class SavedState extends BaseSavedState {

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};

		private String value;

		public SavedState(Parcel source) {
			super(source);
			value = source.readString();
		}

		public SavedState(Parcelable superState, final String value) {
			super(superState);
			this.value = value;
		}

		public final String getValue() {
			return value;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeString(value);
		}

	};

	private CharSequence[] entries;

	private CharSequence[] entryValues;

	private String value;

	private int selectedIndex;

	/**
	 * Initializes the list preference.
	 * 
	 * @param context
	 *            The context, which should be used to initialize the list
	 *            preference, as an instance of the class {@link Context}
	 * @param attributeSet
	 *            The attribute set, which should be used to initialize the list
	 *            preferences, as an instance of the type {@link AttributeSet}
	 */
	private void initialize(final Context context,
			final AttributeSet attributeSet) {
		setNegativeButtonText(android.R.string.cancel);

		if (attributeSet != null) {
			obtainStyledAttributes(context, attributeSet);
		}
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
				R.styleable.ListPreference);
		try {
			obtainEntries(typedArray);
			obtainEntryValues(typedArray);
		} finally {
			typedArray.recycle();
		}
	}

	/**
	 * Obtains the the entries of the list preference from a specific typed
	 * array.
	 * 
	 * @param typedArray
	 *            The typed array, the entries should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainEntries(final TypedArray typedArray) {
		setEntries(typedArray
				.getTextArray(R.styleable.ListPreference_android_entries));
	}

	/**
	 * Obtains the the values, which correspond to the entries of the list
	 * preference from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the entry values should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainEntryValues(final TypedArray typedArray) {
		setEntryValues(typedArray
				.getTextArray(R.styleable.ListPreference_android_entryValues));
	}

	private int getSelectedIndex() {
		if (value != null && entryValues != null) {
			for (int i = entryValues.length - 1; i >= 0; i--) {
				if (entryValues[i].equals(value)) {
					return i;
				}
			}
		}

		return -1;
	}

	private OnClickListener createListItemListener() {
		return new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedIndex = which;
				ListPreference.this.onClick(dialog,
						DialogInterface.BUTTON_POSITIVE);
				dialog.dismiss();
			}
		};
	}

	public ListPreference(final Context context) {
		super(context);
		initialize(context, null);
	}

	public ListPreference(final Context context, final AttributeSet attributeSet) {
		super(context, attributeSet);
		initialize(context, attributeSet);
	}

	public ListPreference(final Context context,
			final AttributeSet attributeSet, final int defStyleAttr) {
		super(context, attributeSet, defStyleAttr);
		initialize(context, attributeSet);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ListPreference(final Context context,
			final AttributeSet attributeSet, final int defStyleAttr,
			final int defStyleRes) {
		super(context, attributeSet, defStyleAttr, defStyleRes);
		initialize(context, attributeSet);
	}

	public CharSequence[] getEntries() {
		return entries;
	}

	public void setEntries(CharSequence[] entries) {
		this.entries = entries;
	}

	public void setEntries(int resourceId) {
		setEntries(getContext().getResources().getTextArray(resourceId));
	}

	public CharSequence[] getEntryValues() {
		return entryValues;
	}

	public void setEntryValues(CharSequence[] entryValues) {
		this.entryValues = entryValues;
	}

	public void setEntryValues(int resourceId) {
		setEntryValues(getContext().getResources().getTextArray(resourceId));
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (!TextUtils.equals(this.value, value)) {
			this.value = value;
			persistString(value);
			notifyChanged();
		}
	}

	public void setValueIndex(int index) {
		if (entryValues != null) {
			setValue(entryValues[index].toString());
		}
	}

	public CharSequence getEntry() {
		int index = getSelectedIndex();
		return index >= 0 && entries != null ? entries[index] : null;
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getString(index);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		setValue(restoreValue ? getPersistedString(value)
				: (String) defaultValue);
	}

	@Override
	protected boolean needInputMethod() {
		return false;
	}

	@Override
	protected void onPrepareDialog(MaterialDialogBuilder dialogBuilder) {
		if (entries == null || entryValues == null) {
			throw new IllegalStateException("ListPreference requires an "
					+ "entries array and an entryValues array");
		}

		selectedIndex = getSelectedIndex();
		dialogBuilder.setSingleChoiceItems(entries, selectedIndex,
				createListItemListener());
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult && selectedIndex >= 0 && entryValues != null) {
			String value = entryValues[selectedIndex].toString();

			if (callChangeListener(value)) {
				setValue(value);
			}
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable parcelable = super.onSaveInstanceState();

		if (!isPersistent()) {
			SavedState savedState = new SavedState(parcelable, getValue());
			return savedState;
		}

		return parcelable;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state != null && state instanceof SavedState) {
			SavedState savedState = (SavedState) state;
			setValue(savedState.getValue());
			super.onRestoreInstanceState(savedState.getSuperState());
		} else {
			super.onRestoreInstanceState(state);
		}
	}

}