package de.mrapp.android.preference;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Window;
import android.view.WindowManager;
import de.mrapp.android.dialog.MaterialDialogBuilder;

public abstract class AbstractDialogPreference extends Preference implements
		OnClickListener, OnDismissListener {

	public static class SavedState extends BaseSavedState {

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}

		};

		private boolean dialogShown;

		private Bundle dialogState;

		public SavedState(Parcel source) {
			super(source);
			dialogShown = source.readInt() == 1;
			dialogState = source.readBundle();
		}

		public SavedState(Parcelable superState, boolean dialogShown,
				Bundle dialogState) {
			super(superState);
			this.dialogShown = dialogShown;
			this.dialogState = dialogState;
		}

		public boolean isDialogShown() {
			return dialogShown;
		}

		public Bundle getDialogState() {
			return dialogState;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(dialogShown ? 1 : 0);
			dest.writeBundle(dialogState);
		}

	};

	private Dialog dialog;

	private boolean dialogResultPositive;

	private CharSequence dialogTitle;

	private CharSequence dialogMessage;

	private Drawable dialogIcon;

	private CharSequence positiveButtonText;

	private CharSequence negativeButtonText;

	private int dialogTitleColor;

	private int dialogButtonTextColor;

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
				R.styleable.DialogPreference);
		try {
			obtainDialogTitle(typedArray);
			obtainDialogMessage(typedArray);
			obtainDialogIcon(typedArray);
			obtainPositiveButtonText(typedArray);
			obtainNegativeButtonText(typedArray);
			obtainDialogTitleColor(typedArray);
			obtainDialogButtonTextColor(typedArray);
		} finally {
			typedArray.recycle();
		}
	}

	/**
	 * Obtains the title of the dialog, which is shown by the preference, from a
	 * specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the title should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainDialogTitle(final TypedArray typedArray) {
		CharSequence title = typedArray
				.getText(R.styleable.DialogPreference_dialogTitle);

		if (title == null) {
			title = getTitle();
		}

		setDialogTitle(title);
	}

	/**
	 * Obtains the message of the dialog, which is shown by the preference, from
	 * a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the message should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainDialogMessage(final TypedArray typedArray) {
		setDialogMessage(typedArray
				.getText(R.styleable.DialogPreference_dialogMessage));
	}

	/**
	 * Obtains the icon of the dialog, which is shown by the preference, from a
	 * specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the icon should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainDialogIcon(final TypedArray typedArray) {
		int resourceId = typedArray.getResourceId(
				R.styleable.DialogPreference_dialogIcon, 0);

		if (resourceId != 0) {
			setDialogIcon(resourceId);
		}
	}

	/**
	 * Obtains the positive button text of the dialog, which is shown by the
	 * preference, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the positive button text should be obtained
	 *            from, as an instance of the class {@link TypedArray}
	 */
	private void obtainPositiveButtonText(final TypedArray typedArray) {
		setPositiveButtonText(typedArray
				.getText(R.styleable.DialogPreference_android_positiveButtonText));
	}

	/**
	 * Obtains the negative button text of the dialog, which is shown by the
	 * preference, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the negative button text should be obtained
	 *            from, as an instance of the class {@link TypedArray}
	 */
	private void obtainNegativeButtonText(final TypedArray typedArray) {
		setNegativeButtonText(typedArray
				.getText(R.styleable.DialogPreference_android_negativeButtonText));
	}

	/**
	 * Obtains the title color of the dialog, which is shown by the preference,
	 * from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the title color should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainDialogTitleColor(final TypedArray typedArray) {
		int color = typedArray.getColor(
				R.styleable.DialogPreference_dialogTitleColor, 0);

		if (color == 0) {
			int resourceId = typedArray.getResourceId(
					R.styleable.DialogPreference_dialogTitleColor, 0);

			if (resourceId != 0) {
				color = getContext().getResources().getColor(resourceId);
			}
		}

		setDialogTitleColor(color);
	}

	/**
	 * Obtains the button text color of the dialog, which is shown by the
	 * preference, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the button text color should be obtained
	 *            from, as an instance of the class {@link TypedArray}
	 */
	private void obtainDialogButtonTextColor(final TypedArray typedArray) {
		int color = typedArray.getColor(
				R.styleable.DialogPreference_dialogButtonTextColor, 0);

		if (color == 0) {
			int resourceId = typedArray.getResourceId(
					R.styleable.DialogPreference_dialogButtonTextColor, 0);

			if (resourceId != 0) {
				color = getContext().getResources().getColor(resourceId);
			}
		}

		setDialogButtonTextColor(color);
	}

	private void showDialog(final Bundle dialogState) {
		MaterialDialogBuilder dialogBuilder = new MaterialDialogBuilder(
				getContext());
		dialogBuilder.setTitle(getDialogTitle());
		dialogBuilder.setMessage(getDialogMessage());
		dialogBuilder.setIcon(getDialogIcon());
		dialogBuilder.setPositiveButton(getPositiveButtonText(), this);
		dialogBuilder.setNegativeButton(getNegativeButtonText(), this);
		dialogBuilder.setTitleColor(getDialogTitleColor());
		dialogBuilder.setButtonTextColor(getDialogButtonTextColor());

		onPrepareDialog(dialogBuilder);

		dialog = dialogBuilder.create();
		dialog.setOnDismissListener(this);

		if (dialogState != null) {
			dialog.onRestoreInstanceState(dialogState);
		}

		requestInputMode();
		dialogResultPositive = false;
		dialog.show();
	}

	private void requestInputMode() {
		if (needInputMethod()) {
			Window window = dialog.getWindow();
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	protected abstract boolean needInputMethod();

	protected abstract void onPrepareDialog(MaterialDialogBuilder dialogBuilder);

	protected abstract void onDialogClosed(final boolean positiveResult);

	public AbstractDialogPreference(final Context context) {
		this(context, null);
	}

	public AbstractDialogPreference(final Context context,
			final AttributeSet attributeSet) {
		this(context, attributeSet, android.R.attr.dialogPreferenceStyle);
	}

	public AbstractDialogPreference(final Context context,
			final AttributeSet attributeSet, final int defStyleAttr) {
		super(context, attributeSet, defStyleAttr);
		obtainStyledAttributes(context, attributeSet);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public AbstractDialogPreference(final Context context,
			final AttributeSet attributeSet, final int defStyleAttr,
			final int defStyleRes) {
		super(context, attributeSet, defStyleAttr, defStyleRes);
		obtainStyledAttributes(context, attributeSet);
	}

	/**
	 * Returns the dialog, which is shown by the preference.
	 * 
	 * @return The dialog, which is shown by the preference, as an instance of
	 *         the class {@link Dialog} or null, if the dialog is currently not
	 *         shown
	 */
	public final Dialog getDialog() {
		if (isDialogShowing()) {
			return dialog;
		} else {
			return null;
		}
	}

	public final boolean isDialogShowing() {
		return dialog != null && dialog.isShowing();
	}

	public final void setDialogTitle(final CharSequence dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public final void setDialogTitle(final int resourceId) {
		setDialogTitle(getContext().getString(resourceId));
	}

	public final CharSequence getDialogTitle() {
		return dialogTitle;
	}

	public final void setDialogMessage(final CharSequence dialogMessage) {
		this.dialogMessage = dialogMessage;
	}

	public final void setDialogMessage(final int resourceId) {
		setDialogMessage(getContext().getString(resourceId));
	}

	public final CharSequence getDialogMessage() {
		return dialogMessage;
	}

	public final void setDialogIcon(final Drawable dialogIcon) {
		this.dialogIcon = dialogIcon;
	}

	public final void setDialogIcon(final int resourceId) {
		dialogIcon = getContext().getResources().getDrawable(resourceId);
	}

	public final Drawable getDialogIcon() {
		return dialogIcon;
	}

	public final void setPositiveButtonText(
			final CharSequence positiveButtonText) {
		this.positiveButtonText = positiveButtonText;
	}

	public final void setPositiveButtonText(final int resourceId) {
		setPositiveButtonText(getContext().getString(resourceId));
	}

	public final CharSequence getPositiveButtonText() {
		return positiveButtonText;
	}

	public final void setNegativeButtonText(
			final CharSequence negativeButtonText) {
		this.negativeButtonText = negativeButtonText;
	}

	public final void setNegativeButtonText(final int resourceId) {
		setNegativeButtonText(getContext().getString(resourceId));
	}

	public final CharSequence getNegativeButtonText() {
		return negativeButtonText;
	}

	public final void setDialogTitleColor(final int color) {
		this.dialogTitleColor = color;
	}

	public final int getDialogTitleColor() {
		return dialogTitleColor;
	}

	public final void setDialogButtonTextColor(final int color) {
		this.dialogButtonTextColor = color;
	}

	public final int getDialogButtonTextColor() {
		return dialogButtonTextColor;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialogResultPositive = (which == Dialog.BUTTON_POSITIVE);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		dialog = null;
		onDialogClosed(dialogResultPositive);
	}

	@Override
	protected final void onClick() {
		if (!isDialogShowing()) {
			showDialog(null);
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable parcelable = super.onSaveInstanceState();

		if (isDialogShowing()) {
			SavedState savedState = new SavedState(parcelable, true,
					dialog.onSaveInstanceState());
			dialog.dismiss();
			return savedState;
		}

		return parcelable;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state != null && state instanceof SavedState) {
			SavedState savedState = (SavedState) state;

			if (savedState.isDialogShown()) {
				showDialog(savedState.getDialogState());
			}

			super.onRestoreInstanceState(savedState.getSuperState());
		} else {
			super.onRestoreInstanceState(state);
		}
	}

}