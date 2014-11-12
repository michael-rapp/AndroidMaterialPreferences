package de.mrapp.android.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import de.mrapp.android.dialog.MaterialDialogBuilder;

public class EditTextPreference extends AbstractDialogPreference {

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

		private String text;

		public SavedState(Parcel source) {
			super(source);
			text = source.readString();
		}

		public SavedState(Parcelable superState, String text) {
			super(superState);
			this.text = text;
		}

		public final String getText() {
			return text;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeString(text);
		}

	};

	private EditText editText;

	private String text;

	private void initialize() {
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
	}

	public EditTextPreference(final Context context) {
		super(context);
		initialize();
	}

	public EditTextPreference(final Context context,
			final AttributeSet attributeSet) {
		super(context, attributeSet);
		initialize();
	}

	public EditTextPreference(final Context context,
			final AttributeSet attributeSet, final int defStyleAttr) {
		super(context, attributeSet, defStyleAttr);
		initialize();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public EditTextPreference(final Context context,
			final AttributeSet attributeSet, final int defStyleAttr,
			final int defStyleRes) {
		super(context, attributeSet, defStyleAttr, defStyleRes);
		initialize();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		boolean hasDisabledDependents = shouldDisableDependents();
		this.text = text;
		persistString(text);
		boolean isDisabelingDependents = shouldDisableDependents();

		if (isDisabelingDependents != hasDisabledDependents) {
			notifyDependencyChange(isDisabelingDependents);
		}
	}

	@Override
	public boolean shouldDisableDependents() {
		return TextUtils.isEmpty(getText()) || super.shouldDisableDependents();
	}

	@Override
	protected void onPrepareDialog(MaterialDialogBuilder dialogBuilder) {
		editText = (EditText) View.inflate(getContext(), R.layout.edit_text,
				null);
		editText.setText(getText());
		dialogBuilder.setView(editText);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			String newValue = editText.getText().toString();

			if (callChangeListener(newValue)) {
				setText(newValue);
			}
		}

		editText = null;
	}

	@Override
	protected boolean needInputMethod() {
		return true;
	}

	@Override
	protected Object onGetDefaultValue(TypedArray typedArray, int index) {
		return typedArray.getString(index);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		setText(restoreValue ? getPersistedString(getText())
				: (String) defaultValue);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable parcelable = super.onSaveInstanceState();

		if (!isPersistent()) {
			SavedState savedState = new SavedState(parcelable, getText());
			return savedState;
		}

		return parcelable;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state != null && state instanceof SavedState) {
			SavedState savedState = (SavedState) state;
			setText(savedState.getText());
			super.onRestoreInstanceState(savedState.getSuperState());
		} else {
			super.onRestoreInstanceState(state);
		}
	}

}