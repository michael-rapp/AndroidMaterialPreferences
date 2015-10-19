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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.Window;
import android.view.WindowManager;

import de.mrapp.android.dialog.MaterialDialogBuilder;

/**
 * An abstract base class for all preferences, which will show a dialog when clicked by the user.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public abstract class AbstractDialogPreference extends Preference
        implements OnClickListener, OnDismissListener {

    /**
     * A data structure, which allows to save the internal state of an {@link
     * AbstractDialogPreference}.
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
         * True, if the dialog of the {@link AbstractDialogPreference}, whose state is saved by the
         * data structure, is currently shown, false otherwise.
         */
        public boolean dialogShown;

        /**
         * The saved state of the dialog of the {@link AbstractDialogPreference} , whose state is
         * saved by the data structure.
         */
        public Bundle dialogState;

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * AbstractDialogPreference}. This constructor is used when reading from a parcel. It reads
         * the state of the superclass.
         *
         * @param source
         *         The parcel to read read from as a instance of the class {@link Parcel}. The
         *         parcel may not be null
         */
        public SavedState(@NonNull final Parcel source) {
            super(source);
            dialogShown = source.readInt() == 1;
            dialogState = source.readBundle();
        }

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * AbstractDialogPreference}. This constructor is called by derived classes when saving
         * their states.
         *
         * @param superState
         *         The state of the superclass of this view, as an instance of the type {@link
         *         Parcelable}. The state may not be null
         */
        public SavedState(@NonNull final Parcelable superState) {
            super(superState);
        }

        @Override
        public final void writeToParcel(final Parcel destination, final int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(dialogShown ? 1 : 0);
            destination.writeBundle(dialogState);
        }

    }

    /**
     * The preference's dialog.
     */
    private Dialog dialog;

    /**
     * True, if the preference's dialog has been closed affirmatively, false otherwise.
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
     * The color of the message of the preference's dialog.
     */
    private int dialogMessageColor;

    /**
     * The color of the button text of the preference's dialog.
     */
    private int dialogButtonTextColor;

    /**
     * The background of the preference's dialog.
     */
    private Drawable dialogBackground;

    /**
     * True, if the currently persisted value should be shown as the summary, instead of the given
     * summaries, false otherwise.
     */
    private boolean showValueAsSummary;

    /**
     * Obtains all attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet) {
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attributeSet, R.styleable.AbstractDialogPreference);
        try {
            obtainDialogTitle(typedArray);
            obtainDialogMessage(typedArray);
            obtainDialogIcon(typedArray);
            obtainPositiveButtonText(typedArray);
            obtainNegativeButtonText(typedArray);
            obtainDialogTitleColor(typedArray);
            obtainDialogMessageColor(typedArray);
            obtainDialogButtonTextColor(typedArray);
            obtainDialogBackground(typedArray);
            obtainShowValueAsSummary(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the title of the dialog, which is shown by the preference, from a specific typed
     * array.
     *
     * @param typedArray
     *         The typed array, the title should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogTitle(@NonNull final TypedArray typedArray) {
        CharSequence title =
                typedArray.getText(R.styleable.AbstractDialogPreference_android_dialogTitle);
        setDialogTitle(title != null ? title : getTitle());
    }

    /**
     * Obtains the message of the dialog, which is shown by the preference, from a specific typed
     * array.
     *
     * @param typedArray
     *         The typed array, the message should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogMessage(@NonNull final TypedArray typedArray) {
        setDialogMessage(
                typedArray.getText(R.styleable.AbstractDialogPreference_android_dialogMessage));
    }

    /**
     * Obtains the icon of the dialog, which is shown by the preference, from a specific typed
     * array.
     *
     * @param typedArray
     *         The typed array, the icon should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainDialogIcon(@NonNull final TypedArray typedArray) {
        int resourceId = typedArray
                .getResourceId(R.styleable.AbstractDialogPreference_android_dialogIcon, -1);

        if (resourceId != -1) {
            setDialogIcon(resourceId);
        }
    }

    /**
     * Obtains the positive button text of the dialog, which is shown by the preference, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the positive button text should be obtained from, as an instance of
     *         the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainPositiveButtonText(@NonNull final TypedArray typedArray) {
        setPositiveButtonText(typedArray
                .getText(R.styleable.AbstractDialogPreference_android_positiveButtonText));
    }

    /**
     * Obtains the negative button text of the dialog, which is shown by the preference, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the negative button text should be obtained from, as an instance of
     *         the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainNegativeButtonText(@NonNull final TypedArray typedArray) {
        setNegativeButtonText(typedArray
                .getText(R.styleable.AbstractDialogPreference_android_negativeButtonText));
    }

    /**
     * Obtains the title color of the dialog, which is shown by the preference, from a specific
     * typed array.
     *
     * @param typedArray
     *         The typed array, the title color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogTitleColor(@NonNull final TypedArray typedArray) {
        setDialogTitleColor(
                typedArray.getColor(R.styleable.AbstractDialogPreference_dialogTitleColor, -1));
    }

    /**
     * Obtains the message color of the dialog, which is shown by the preference, from a specific
     * typed array.
     *
     * @param typedArray
     *         The typed array, the message color should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogMessageColor(@NonNull final TypedArray typedArray) {
        setDialogMessageColor(
                typedArray.getColor(R.styleable.AbstractDialogPreference_dialogMessageColor, -1));
    }

    /**
     * Obtains the button text color of the dialog, which is shown by the preference, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the button text color should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogButtonTextColor(@NonNull final TypedArray typedArray) {
        setDialogButtonTextColor(typedArray
                .getColor(R.styleable.AbstractDialogPreference_dialogButtonTextColor, -1));
    }

    /**
     * Obtains the background of the dialog, which is shown by the preference, from a specific typed
     * array.
     *
     * @param typedArray
     *         The typed array, the background should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    @SuppressWarnings("deprecation")
    private void obtainDialogBackground(@NonNull final TypedArray typedArray) {
        int backgroundColor =
                typedArray.getColor(R.styleable.AbstractDialogPreference_dialogBackground, -1);

        if (backgroundColor != -1) {
            setDialogBackgroundColor(backgroundColor);
        } else {
            int resourceId = typedArray
                    .getResourceId(R.styleable.AbstractDialogPreference_dialogBackground, -1);
            Drawable background = null;

            if (resourceId != -1) {
                background = getContext().getResources().getDrawable(resourceId);
            }

            setDialogBackground(background);
        }
    }

    /**
     * Obtains the boolean value, which specifies whether the currently persisted value should be
     * shown as the summary, instead of the given summaries, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the boolean value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainShowValueAsSummary(@NonNull final TypedArray typedArray) {
        boolean defaultValue = getContext().getResources()
                .getBoolean(R.bool.dialog_preference_default_show_value_as_summary);
        showValueAsSummary(typedArray
                .getBoolean(R.styleable.AbstractDialogPreference_showValueAsSummary, defaultValue));
    }

    /**
     * Shows the preference's dialog.
     *
     * @param dialogState
     *         The dialog's saved state as an instance of the class {@link Bundle} or null, if the
     *         dialog should be created from scratch
     */
    private void showDialog(@Nullable final Bundle dialogState) {
        MaterialDialogBuilder dialogBuilder = new MaterialDialogBuilder(getContext());
        dialogBuilder.setTitle(getDialogTitle());
        dialogBuilder.setMessage(getDialogMessage());
        dialogBuilder.setIcon(getDialogIcon());
        dialogBuilder.setPositiveButton(getPositiveButtonText(), this);
        dialogBuilder.setNegativeButton(getNegativeButtonText(), this);
        dialogBuilder.setTitleColor(getDialogTitleColor());
        dialogBuilder.setMessageColor(getDialogMessageColor());
        dialogBuilder.setButtonTextColor(getDialogButtonTextColor());
        dialogBuilder.setBackground(getDialogBackground());

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
     * The method, which is invoked on subclasses to determine, whether the soft input mode should
     * be requested when the preference's dialog becomes shown, or not.
     *
     * @return True, if the soft input mode should be requested when the preference's dialog becomes
     * shown, false otherwise
     */
    protected abstract boolean needInputMethod();

    /**
     * The method, which is invoked on subclasses when the preference's dialog is about to be
     * created. The builder, which is passed as a method parameter may be manipulated by subclasses
     * in order to change the appearance of the dialog.
     *
     * @param dialogBuilder
     *         The builder, which is used to create the preference's dialog, as an instance of the
     *         class MaterialDialogBuilder
     */
    protected abstract void onPrepareDialog(@NonNull MaterialDialogBuilder dialogBuilder);

    /**
     * The method, which is invoked on subclasses when the preference's dialog has been closed. This
     * method may be used within subclasses to evaluate the changes, which have been made while the
     * dialog was shown.
     *
     * @param positiveResult
     *         True, if the dialog has been close affirmatively, false otherwise
     */
    protected abstract void onDialogClosed(final boolean positiveResult);

    /**
     * Creates a new preference, which will show a dialog when clicked by the user.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public AbstractDialogPreference(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new preference, which will show a dialog when clicked by the user.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public AbstractDialogPreference(@NonNull final Context context,
                                    @Nullable final AttributeSet attributeSet) {
        this(context, attributeSet, android.R.attr.dialogPreferenceStyle);
    }

    /**
     * Creates a new preference, which will show a dialog when clicked by the user.
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
    public AbstractDialogPreference(@NonNull final Context context,
                                    @Nullable final AttributeSet attributeSet,
                                    final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        obtainStyledAttributes(attributeSet);
    }

    /**
     * Creates a new preference, which will show a dialog when clicked by the user.
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
    public AbstractDialogPreference(@NonNull final Context context,
                                    @Nullable final AttributeSet attributeSet,
                                    final int defaultStyle, final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        obtainStyledAttributes(attributeSet);
    }

    /**
     * Returns the dialog, which is shown by the preference.
     *
     * @return The dialog, which is shown by the preference, as an instance of the class {@link
     * Dialog} or null, if the dialog is currently not shown
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
     * @return True, if the preference's dialog is currently shown, false otherwise
     */
    public final boolean isDialogShown() {
        return dialog != null && dialog.isShowing();
    }

    /**
     * Returns the title of the preference's dialog.
     *
     * @return The title of the preference's dialog as an instance of the class {@link CharSequence}
     * or null, if the preference's title is used instead
     */
    public final CharSequence getDialogTitle() {
        return dialogTitle;
    }

    /**
     * Sets the title of the preference's dialog.
     *
     * @param dialogTitle
     *         The title, which should be set, as an instance of the class {@link CharSequence} or
     *         null, if the preference's title should be used instead
     */
    public final void setDialogTitle(@Nullable final CharSequence dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    /**
     * Sets the title of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the title, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setDialogTitle(@StringRes final int resourceId) {
        setDialogTitle(getContext().getString(resourceId));
    }

    /**
     * Returns the message of the preference's dialog.
     *
     * @return The message of the preference's dialog as an instance of the class {@link
     * CharSequence} or null, if no message is shown in the dialog
     */
    public final CharSequence getDialogMessage() {
        return dialogMessage;
    }

    /**
     * Sets the message of the preference's dialog.
     *
     * @param dialogMessage
     *         The message, which should be set, as an instance of the class {@link CharSequence} or
     *         null, if no message should be shown in the dialog
     */
    public final void setDialogMessage(@Nullable final CharSequence dialogMessage) {
        this.dialogMessage = dialogMessage;
    }

    /**
     * Sets the message of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the message, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setDialogMessage(@StringRes final int resourceId) {
        setDialogMessage(getContext().getString(resourceId));
    }

    /**
     * Returns the icon of the preference's dialog.
     *
     * @return The icon of the preference's dialog as an instance of the class {@link Drawable} or
     * null, if no icon is shown in the dialog
     */
    public final Drawable getDialogIcon() {
        return dialogIcon;
    }

    /**
     * Sets the icon of the preference's dialog.
     *
     * @param dialogIcon
     *         The dialog, which should be set, as an instance of the class {@link Drawable} or
     *         null, if no icon should be shown in the dialog
     */
    public final void setDialogIcon(@Nullable final Drawable dialogIcon) {
        this.dialogIcon = dialogIcon;
    }

    /**
     * Sets the icon of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the icon, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid drawable resource
     */
    @SuppressWarnings("deprecation")
    public final void setDialogIcon(@DrawableRes final int resourceId) {
        dialogIcon = getContext().getResources().getDrawable(resourceId);
    }

    /**
     * Returns the text of the positive button of the preference's dialog.
     *
     * @return The text of the positive button as an instance of the class {@link CharSequence} or
     * null, if no positive button is shown in the dialog
     */
    public final CharSequence getPositiveButtonText() {
        return positiveButtonText;
    }

    /**
     * Sets the text of the positive button of the preference's dialog.
     *
     * @param positiveButtonText
     *         The text, which should be set, as an instance of the class {@link CharSequence} or
     *         null, if no positive button should be shown in the dialog
     */
    public final void setPositiveButtonText(@Nullable final CharSequence positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
    }

    /**
     * Sets the text of the positive button of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setPositiveButtonText(@StringRes final int resourceId) {
        setPositiveButtonText(getContext().getString(resourceId));
    }

    /**
     * Returns the text of the negative button of the preference's dialog.
     *
     * @return The text of the negative button as an instance of the class {@link CharSequence} or
     * null, if no negative button is shown in the dialog
     */
    public final CharSequence getNegativeButtonText() {
        return negativeButtonText;
    }

    /**
     * Sets the text of the negative button of the preference's dialog.
     *
     * @param negativeButtonText
     *         The text, which should be set, as an instance of the class {@link CharSequence} or
     *         null, if no negative button should be shown in the dialog
     */
    public final void setNegativeButtonText(@Nullable final CharSequence negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
    }

    /**
     * Sets the text of the negative button of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setNegativeButtonText(@StringRes final int resourceId) {
        setNegativeButtonText(getContext().getString(resourceId));
    }

    /**
     * Returns the color of the title of the preference's dialog.
     *
     * @return The color of the title as an {@link Integer} value or -1, if no custom title color is
     * set
     */
    public final int getDialogTitleColor() {
        return dialogTitleColor;
    }

    /**
     * Sets the color of the title of the preference's dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom title
     *         should be set
     */
    public final void setDialogTitleColor(@ColorInt final int color) {
        this.dialogTitleColor = color;
    }

    /**
     * Returns the color of the message of the preference's dialog.
     *
     * @return The color of the message as an {@link Integer} value or -1, if no custom message
     * color is set
     */
    public final int getDialogMessageColor() {
        return dialogMessageColor;
    }

    /**
     * Sets the color of the message of the preference's dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom
     *         message color should be set
     */
    public final void setDialogMessageColor(@ColorInt final int color) {
        this.dialogMessageColor = color;
    }

    /**
     * Returns the color of the button text of the preference's dialog.
     *
     * @return The color of the button text as an {@link Integer} value or -1, if no custom text
     * color is set
     */
    public final int getDialogButtonTextColor() {
        return dialogButtonTextColor;
    }

    /**
     * Sets the color of the button text of the preference's dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom text
     *         color should be set
     */
    public final void setDialogButtonTextColor(@ColorInt final int color) {
        this.dialogButtonTextColor = color;
    }

    /**
     * Returns the background of the preference's dialog.
     *
     * @return The background of the preference's dialog as an instance of the class {@link
     * Drawable} or null, if no custom background is set
     */
    public final Drawable getDialogBackground() {
        return dialogBackground;
    }

    /**
     * Sets the background of the preference's dialog.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Drawable} or
     *         null, if no custom background should be set
     */
    public final void setDialogBackground(@Nullable final Drawable background) {
        this.dialogBackground = background;
    }

    /**
     * Sets the background of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     */
    @SuppressWarnings("deprecation")
    public final void setDialogBackground(@DrawableRes final int resourceId) {
        setDialogBackground(getContext().getResources().getDrawable(resourceId));
    }

    /**
     * Sets the background color of the preference's dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom
     *         background color should be set
     */
    public final void setDialogBackgroundColor(@ColorInt final int color) {
        setDialogBackground(color != -1 ? new ColorDrawable(color) : null);
    }

    /**
     * Returns, whether the currently persisted value is shown instead of the summary, or not.
     *
     * @return True, if the currently persisted value is shown instead of the summary, false
     * otherwise
     */
    public final boolean isValueShownAsSummary() {
        return showValueAsSummary;
    }

    /**
     * Sets, whether the currently persisted value should be shown instead of the summary, or not.
     *
     * @param showValueAsSummary
     *         True, if the currently persisted value should be shown instead of the summary, false
     *         otherwise
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
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
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

            if (savedState.dialogShown) {
                showDialog(savedState.dialogState);
            }

            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}