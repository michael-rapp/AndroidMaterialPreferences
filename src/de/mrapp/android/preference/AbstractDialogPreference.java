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

/**
 * An abstract base class for all preferences, which will show a dialog when
 * clicked by the user.
 * 
 * @author Michael Rapp
 * 
 * @since 1.0.0
 */
public abstract class AbstractDialogPreference extends Preference implements
		OnClickListener, OnDismissListener {

	/**
	 * A data structure, which allows to save the internal state of an
	 * {@link AbstractDialogPreference}.
	 */
	public static class SavedState extends BaseSavedState {

		/**
		 * A creator, which allows to create instances of the class
		 * {@link AbstractDialogPreference} from parcels.
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
		 * True, if the dialog of the {@link AbstractDialogPreference}, whose
		 * state is saved by the data structure, is currently shown, false
		 * otherwise.
		 */
		public boolean dialogShown;

		/**
		 * The saved state of the dialog of the {@link AbstractDialogPreference}
		 * , whose state is saved by the data structure.
		 */
		public Bundle dialogState;

		/**
		 * The saved value of the attribute "showValueAsSummary".
		 */
		public boolean showValueAsSummary;

		/**
		 * Creates a new data structure, which allows to store the internal
		 * state of an {@link AbstractDialogPreference}. This constructor is
		 * used when reading from a parcel. It reads the state of the
		 * superclass.
		 * 
		 * @param source
		 *            The parcel to read read from as a instance of the class
		 *            {@link Parcel}
		 */
		public SavedState(final Parcel source) {
			super(source);
			dialogShown = source.readInt() == 1;
			dialogState = source.readBundle();
			showValueAsSummary = source.readByte() != 0;
		}

		/**
		 * Creates a new data structure, which allows to store the internal
		 * state of an {@link AbstractDialogPreference}. This constructor is
		 * called by derived classes when saving their states.
		 * 
		 * @param superState
		 *            The state of the superclass of this view, as an instance
		 *            of the type {@link Parcelable}
		 */
		public SavedState(final Parcelable superState) {
			super(superState);
		}

		@Override
		public final void writeToParcel(final Parcel destination,
				final int flags) {
			super.writeToParcel(destination, flags);
			destination.writeInt(dialogShown ? 1 : 0);
			destination.writeBundle(dialogState);
			destination.writeByte((byte) (showValueAsSummary ? 1 : 0));
		}

	};

	/**
	 * The default value, which specifies, whether the currently persisted value
	 * should be shown instead of the summary, or not.
	 */
	protected static final boolean DEFAULT_SHOW_VALUE_AS_SUMMARY = false;

	/**
	 * The preference's dialog.
	 */
	private Dialog dialog;

	/**
	 * True, if the preference's dialog has been closed affirmatively, false
	 * otherwise.
	 */
	private boolean dialogResultPositive;

	/**
	 * The title of the preference's dialog.
	 */
	private CharSequence dialogTitle;

	/**
	 * The message of the preference's dialog.
	 */
	private CharSequence dialogMessage;

	/**
	 * The icon of the preference's dialog.
	 */
	private Drawable dialogIcon;

	/**
	 * The text of the positive button of the preference's dialog.
	 */
	private CharSequence positiveButtonText;

	/**
	 * The text of the negative button of the preference's dialog.
	 */
	private CharSequence negativeButtonText;

	/**
	 * The color of the title of the preference's dialog.
	 */
	private int dialogTitleColor;

	/**
	 * The color of the button text of the preference's dialog.
	 */
	private int dialogButtonTextColor;

	/**
	 * True, if the currently persisted value should be shown as the summary,
	 * instead of the given summaries, false otherwise.
	 */
	private boolean showValueAsSummary;

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
			obtainShowValueAsSummary(typedArray);
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
				.getText(R.styleable.DialogPreference_android_dialogTitle);

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
				.getText(R.styleable.DialogPreference_android_dialogMessage));
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
				R.styleable.DialogPreference_android_dialogIcon, 0);

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

	/**
	 * Obtains the boolean value, which specifies whether the currently
	 * persisted value should be shown as the summary, instead of the given
	 * summaries, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the boolean value should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainShowValueAsSummary(final TypedArray typedArray) {
		if (typedArray != null) {
			showValueAsSummary(typedArray.getBoolean(
					R.styleable.DialogPreference_showValueAsSummary,
					DEFAULT_SHOW_VALUE_AS_SUMMARY));
		} else {
			showValueAsSummary(DEFAULT_SHOW_VALUE_AS_SUMMARY);
		}
	}

	/**
	 * Shows the preference's dialog.
	 * 
	 * @param dialogState
	 *            The dialog's saved state as an instance of the class
	 *            {@link Bundle} or null, if the dialog should be created from
	 *            scratch
	 */
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

	/**
	 * Requests the soft input method to be shown.
	 */
	private void requestInputMode() {
		if (needInputMethod()) {
			Window window = dialog.getWindow();
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	/**
	 * The method, which is invoked on subclasses to determine, whether the soft
	 * input mode should be requested when the preference's dialog becomes
	 * shown, or not.
	 * 
	 * @return True, if the soft input mode should be requested when the
	 *         preference's dialog becomes shown, false otherwise
	 */
	protected abstract boolean needInputMethod();

	/**
	 * The method, which is invoked on subclasses when the preference's dialog
	 * is about to be created. The builder, which is passed as a method
	 * parameter may be manipulated by subclasses in order to change the
	 * appearance of the dialog.
	 * 
	 * @param dialogBuilder
	 *            The builder, which is used to create the preference's dialog,
	 *            as an instance of the class {@link MaterialDialogBuilder}
	 */
	protected abstract void onPrepareDialog(MaterialDialogBuilder dialogBuilder);

	/**
	 * The method, which is invoked on subclasses when the preference's dialog
	 * has been closed. This method may be used within subclasses to evaluate
	 * the changes, which have been made while the dialog was shown.
	 * 
	 * @param positiveResult
	 *            True, if the dialog has been close affirmatively, false
	 *            otherwise
	 */
	protected abstract void onDialogClosed(final boolean positiveResult);

	/**
	 * Creates a new preference, which will show a dialog when clicked by the
	 * user.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 */
	public AbstractDialogPreference(final Context context) {
		this(context, null);
	}

	/**
	 * Creates a new preference, which will show a dialog when clicked by the
	 * user.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the
	 *            preference, as an instance of the type {@link AttributeSet}
	 */
	public AbstractDialogPreference(final Context context,
			final AttributeSet attributeSet) {
		this(context, attributeSet, android.R.attr.dialogPreferenceStyle);
	}

	/**
	 * Creates a new preference, which will show a dialog when clicked by the
	 * user.
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
	public AbstractDialogPreference(final Context context,
			final AttributeSet attributeSet, final int defaultStyle) {
		super(context, attributeSet, defaultStyle);
		obtainStyledAttributes(context, attributeSet);
	}

	/**
	 * Creates a new preference, which will show a dialog when clicked by the
	 * user.
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
	public AbstractDialogPreference(final Context context,
			final AttributeSet attributeSet, final int defaultStyle,
			final int defaultStyleResource) {
		super(context, attributeSet, defaultStyle, defaultStyleResource);
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
		if (isDialogShown()) {
			return dialog;
		} else {
			return null;
		}
	}

	/**
	 * Returns, whether the preference's dialog is currently shown, or not.
	 * 
	 * @return True, if the preference's dialog is currently shown, false
	 *         otherwise
	 */
	public final boolean isDialogShown() {
		return dialog != null && dialog.isShowing();
	}

	/**
	 * Returns the title of the preference's dialog.
	 * 
	 * @return The title of the preference's dialog as an instance of the class
	 *         {@link CharSequence} or null, if the preference's title is used
	 *         instead
	 */
	public final CharSequence getDialogTitle() {
		return dialogTitle;
	}

	/**
	 * Sets the title of the preference's dialog.
	 * 
	 * @param dialogTitle
	 *            The title, which should be set, as an instance of the class
	 *            {@link CharSequence} or null, if the preference's title should
	 *            be used instead
	 */
	public final void setDialogTitle(final CharSequence dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	/**
	 * Sets the title of the preference's dialog.
	 * 
	 * @param resourceId
	 *            The resource id of the title, which should be set, as an
	 *            {@link Integer} value. The resource id must correspond to a
	 *            valid string resource
	 */
	public final void setDialogTitle(final int resourceId) {
		setDialogTitle(getContext().getString(resourceId));
	}

	/**
	 * Returns the message of the preference's dialog.
	 * 
	 * @return The message of the preference's dialog as an instance of the
	 *         class {@link CharSequence} or null, if no title is used
	 */
	public final CharSequence getDialogMessage() {
		return dialogMessage;
	}

	/**
	 * Sets the message of the preference's dialog.
	 * 
	 * @param dialogMessage
	 *            The message, which should be set, as an instance of the class
	 *            {@link CharSequence} or null, if no title should be used
	 */
	public final void setDialogMessage(final CharSequence dialogMessage) {
		this.dialogMessage = dialogMessage;
	}

	/**
	 * Sets the message of the preference's dialog.
	 * 
	 * @param resourceId
	 *            The resource id of the message, which should be set, as an
	 *            {@link Integer} value. The resource id must correspond to a
	 *            valid string resource
	 */
	public final void setDialogMessage(final int resourceId) {
		setDialogMessage(getContext().getString(resourceId));
	}

	/**
	 * Returns the icon of the preference's dialog.
	 * 
	 * @return The icon of the preference's dialog as an instance of the class
	 *         {@link Drawable} or null, if no icon is used
	 */
	public final Drawable getDialogIcon() {
		return dialogIcon;
	}

	/**
	 * Sets the icon of the preference's dialog.
	 * 
	 * @param dialogIcon
	 *            The dialog, which should be set, as an instance of the class
	 *            {@link Drawable} or null, if no icon should be used
	 */
	public final void setDialogIcon(final Drawable dialogIcon) {
		this.dialogIcon = dialogIcon;
	}

	/**
	 * Sets the icon of the preference's dialog.
	 * 
	 * @param resourceId
	 *            The resource id of the icon, which should be set, as an
	 *            {@link Integer} value. The resource id must correspond to a
	 *            valid drawable resource
	 */
	public final void setDialogIcon(final int resourceId) {
		dialogIcon = getContext().getResources().getDrawable(resourceId);
	}

	/**
	 * Returns the text of the positive button of the preference's dialog.
	 * 
	 * @return The text of the positive button as an instance of the class
	 *         {@link CharSequence} or null, if no positive button should be
	 *         used
	 */
	public final CharSequence getPositiveButtonText() {
		return positiveButtonText;
	}

	/**
	 * Sets the text of the positive button of the preference's dialog.
	 * 
	 * @param positiveButtonText
	 *            The text, which should be set, as an instance of the class
	 *            {@link CharSequence} or null, if no positive button should be
	 *            used
	 */
	public final void setPositiveButtonText(
			final CharSequence positiveButtonText) {
		this.positiveButtonText = positiveButtonText;
	}

	/**
	 * Sets the text of the positive button of the preference's dialog.
	 * 
	 * @param resourceId
	 *            The resource id of the text, which should be set, as an
	 *            {@link Integer} value. The resource id must correspond to a
	 *            valid string resource
	 */
	public final void setPositiveButtonText(final int resourceId) {
		setPositiveButtonText(getContext().getString(resourceId));
	}

	/**
	 * Returns the text of the negative button of the preference's dialog.
	 * 
	 * @return The text of the negative button as an instance of the class
	 *         {@link CharSequence} or null, if no negative button should be
	 *         shown
	 */
	public final CharSequence getNegativeButtonText() {
		return negativeButtonText;
	}

	/**
	 * Sets the text of the negative button of the preference's dialog.
	 * 
	 * @param negativeButtonText
	 *            The text, which should be set, as an instance of the class
	 *            {@link CharSequence} or null, if no negative button should be
	 *            used
	 */
	public final void setNegativeButtonText(
			final CharSequence negativeButtonText) {
		this.negativeButtonText = negativeButtonText;
	}

	/**
	 * Sets the text of the negative button of the preference's dialog.
	 * 
	 * @param resourceId
	 *            The resource id of the text, which should be set, as an
	 *            {@link Integer} value. The resource id must correspond to a
	 *            valid string resource
	 */
	public final void setNegativeButtonText(final int resourceId) {
		setNegativeButtonText(getContext().getString(resourceId));
	}

	/**
	 * Returns the color of the title of the preference's dialog.
	 * 
	 * @return The color of the title as an {@link Integer} value
	 */
	public final int getDialogTitleColor() {
		return dialogTitleColor;
	}

	/**
	 * Sets the color of the title of the preference's dialog.
	 * 
	 * @param color
	 *            The color, which should be set, as an {@link Integer} value
	 */
	public final void setDialogTitleColor(final int color) {
		this.dialogTitleColor = color;
	}

	/**
	 * Returns the color of the button text of the preference's dialog.
	 * 
	 * @return The color of the button text as an {@link Integer} value
	 */
	public final int getDialogButtonTextColor() {
		return dialogButtonTextColor;
	}

	/**
	 * Sets the color of the button text of the preference's dialog.
	 * 
	 * @param color
	 *            The color, which should be set, as an {@link Integer} value
	 */
	public final void setDialogButtonTextColor(final int color) {
		this.dialogButtonTextColor = color;
	}

	/**
	 * Returns, whether the currently persisted value is shown instead of the
	 * summary, or not.
	 * 
	 * @return True, if the currently persisted value is shown instead of the
	 *         summary, false otherwise
	 */
	public final boolean isValueShownAsSummary() {
		return showValueAsSummary;
	}

	/**
	 * Sets, whether the currently persisted value should be shown instead of
	 * the summary, or not.
	 * 
	 * @param showValueAsSummary
	 *            True, if the currently persisted value should be shown instead
	 *            of the summary, false otherwise
	 */
	public final void showValueAsSummary(final boolean showValueAsSummary) {
		this.showValueAsSummary = showValueAsSummary;
	}

	@Override
	public final void onClick(final DialogInterface dialog, final int which) {
		dialogResultPositive = (which == Dialog.BUTTON_POSITIVE);
	}

	@Override
	public final void onDismiss(final DialogInterface dialog) {
		this.dialog = null;
		onDialogClosed(dialogResultPositive);
	}

	@Override
	protected final void onClick() {
		if (!isDialogShown()) {
			showDialog(null);
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable parcelable = super.onSaveInstanceState();
		SavedState savedState = new SavedState(parcelable);
		savedState.showValueAsSummary = isValueShownAsSummary();
		savedState.dialogShown = isDialogShown();

		if (isDialogShown()) {
			savedState.dialogState = dialog.onSaveInstanceState();
			dialog.dismiss();
			dialog = null;
		}

		return savedState;
	}

	@Override
	protected void onRestoreInstanceState(final Parcelable state) {
		if (state != null && state instanceof SavedState) {
			SavedState savedState = (SavedState) state;
			showValueAsSummary(savedState.showValueAsSummary);

			if (savedState.dialogShown) {
				showDialog(savedState.dialogState);
			}

			super.onRestoreInstanceState(savedState.getSuperState());
		} else {
			super.onRestoreInstanceState(state);
		}
	}

}