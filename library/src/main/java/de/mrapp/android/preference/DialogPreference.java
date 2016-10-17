/*
 * Copyright 2014 - 2016 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.preference;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnShowListener;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import de.mrapp.android.dialog.MaterialDialog;
import de.mrapp.android.util.view.AbstractSavedState;

/**
 * An abstract base class for all preferences, which will show a dialog when clicked by the user.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class DialogPreference extends Preference
        implements OnClickListener, OnShowListener, OnDismissListener, OnCancelListener,
        OnKeyListener {

    /**
     * A data structure, which allows to save the internal state of an {@link DialogPreference}.
     */
    public static class SavedState extends AbstractSavedState {

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
         * The saved state of the dialog of the {@link DialogPreference} , whose state is saved by
         * the data structure.
         */
        public Bundle dialogState;

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * DialogPreference}. This constructor is used when reading from a parcel. It reads the
         * state of the superclass.
         *
         * @param source
         *         The parcel to read read from as a instance of the class {@link Parcel}. The
         *         parcel may not be null
         */
        public SavedState(@NonNull final Parcel source) {
            super(source);
            dialogState = source.readBundle(getClass().getClassLoader());
        }

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * DialogPreference}. This constructor is called by derived classes when saving their
         * states.
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
            destination.writeBundle(dialogState);
        }

    }

    /**
     * The preference's dialog.
     */
    private Dialog dialog;

    /**
     * The resource id of the theme, which is used by the preference's dialog.
     */
    private int dialogTheme;

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
     * The bitmap of the icon the preference's dialog.
     */
    private Bitmap dialogIconBitmap;

    /**
     * The resource id of the icon of the preference's dialog.
     */
    private int dialogIconId = -1;

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
     * The text color of the buttons of the preference's dialog.
     */
    private int dialogButtonTextColor;

    /**
     * The text color of the buttons of the preference's dialog when disabled.
     */
    private int dialogDisabledButtonTextColor;

    /**
     * The background of the preference's dialog.
     */
    private Drawable dialogBackground;

    /**
     * The bitmap of the background of the preference's dialog.
     */
    private Bitmap dialogBackgroundBitmap;

    /**
     * The resource id of the background of the preference's dialog.
     */
    private int dialogBackgroundId = -1;

    /**
     * The color of the background of the preference's dialog.
     */
    private int dialogBackgroundColor = -1;

    /**
     * True, if the currently persisted value should be shown as the summary, instead of the given
     * summaries, false otherwise.
     */
    private boolean showValueAsSummary;

    /**
     * True, if the header of the preference's dialog should be shown, false otherwise.
     */
    private boolean showDialogHeader;

    /**
     * The background of the header of the preference's dialog.
     */
    private Drawable dialogHeaderBackground;

    /**
     * The bitmap of the background of the header of the preference's dialog.
     */
    private Bitmap dialogHeaderBackgroundBitmap;

    /**
     * The resource id of the background of the header of the preference's dialog.
     */
    private int dialogHeaderBackgroundId = -1;

    /**
     * The color of the background of the header of the preference's dialog.
     */
    private int dialogHeaderBackgroundColor = -1;

    /**
     * The icon of the header of the preference's dialog.
     */
    private Drawable dialogHeaderIcon;

    /**
     * The bitmap of the icon of the header of the preference's dialog.
     */
    private Bitmap dialogHeaderIconBitmap;

    /**
     * The resource id of the icon of the header of the preference's dialog.
     */
    private int dialogHeaderIconId = -1;

    /**
     * True, if the divider, which is located above the button bar of the preference's dialog, is
     * shown, false otherwise.
     */
    private boolean showDialogButtonBarDivider;

    /**
     * The color of the divider, which is located above the button bar of the preference's dialog.
     */
    private int dialogButtonBarDividerColor;

    /**
     * True, if the dividers, which are located above and below the list view of the preference's
     * dialog, should be shown when the list view is scrolled, false otherwise.
     */
    private boolean showDialogDividersOnScroll;

    /**
     * The custom view, which should be used to show the title of the preference's dialog.
     */
    private View customDialogTitleView;

    /**
     * The resource id of the custom view, which should be used to show the title of the
     * preference's dialog.
     */
    private int customDialogTitleViewId = -1;

    /**
     * The custom view, which should be used to show the message of the preference's dialog.
     */
    private View customDialogMessageView;

    /**
     * The resource id of the custom view, which should be used to show the message of the
     * preference's dialog.
     */
    private int customDialogMessageViewId = -1;

    /**
     * The custom view, which should be used to show the buttons of the preference's dialog.
     */
    private View customDialogButtonBarView;

    /**
     * The resource id of the custom view, which should be used to show the buttons of the
     * preference's dialog.
     */
    private int customDialogButtonBarViewId = -1;

    /**
     * The listener, which is notified, when a button of the preference's dialog has been clicked.
     */
    private OnClickListener onClickListener;

    /**
     * The listener, which is notified, when the preference's dialog has been shown.
     */
    private OnShowListener onShowListener;

    /**
     * The listener, which is notified, when the preference's dialog has been dismissed.
     */
    private OnDismissListener onDismissListener;

    /**
     * The listener, which is notified, when the preference's dialog has been canceled.
     */
    private OnCancelListener onCancelListener;

    /**
     * The listener, which is notified, when a key has been dispatched to the preference's dialog.
     */
    private OnKeyListener onKeyListener;

    /**
     * Obtains all attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the
     *         preference, used only if the default style is 0 or can not be found in the theme. Can
     *         be 0 to not look for defaults
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet,
                                        @AttrRes final int defaultStyle,
                                        @StyleRes final int defaultStyleResource) {
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attributeSet, R.styleable.DialogPreference, defaultStyle,
                        defaultStyleResource);

        try {
            obtainDialogTheme(typedArray);
            obtainDialogTitle(typedArray);
            obtainDialogMessage(typedArray);
            obtainDialogIcon(typedArray);
            obtainPositiveButtonText(typedArray);
            obtainNegativeButtonText(typedArray);
            obtainDialogTitleColor(typedArray);
            obtainDialogMessageColor(typedArray);
            obtainDialogButtonTextColor(typedArray);
            obtainDialogDisabledButtonTextColor(typedArray);
            obtainDialogBackground(typedArray);
            obtainShowValueAsSummary(typedArray);
            obtainShowDialogHeader(typedArray);
            obtainDialogHeaderBackground(typedArray);
            obtainDialogHeaderIcon(typedArray);
            obtainShowDialogButtonBarDivider(typedArray);
            obtainDialogButtonBarDividerColor(typedArray);
            obtainShowDialogDividersOnScroll(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the theme, which should be used by the preference's dialog, from a specific typed
     * array.
     *
     * @param typedArray
     *         The typed array, the theme should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogTheme(@NonNull final TypedArray typedArray) {
        int themeId = typedArray.getResourceId(R.styleable.DialogPreference_dialogThemeResource, 0);

        if (themeId == 0) {
            TypedValue typedValue = new TypedValue();
            getContext().getTheme()
                    .resolveAttribute(R.attr.preferenceDialogTheme, typedValue, true);
            themeId = typedValue.resourceId;
        }

        dialogTheme = themeId != 0 ? themeId : R.style.MaterialDialog_Light;
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
        CharSequence title = typedArray.getText(R.styleable.DialogPreference_android_dialogTitle);
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
        setDialogMessage(typedArray.getText(R.styleable.DialogPreference_android_dialogMessage));
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
        int resourceId =
                typedArray.getResourceId(R.styleable.DialogPreference_android_dialogIcon, -1);

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
        setPositiveButtonText(
                typedArray.getText(R.styleable.DialogPreference_android_positiveButtonText));
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
        setNegativeButtonText(
                typedArray.getText(R.styleable.DialogPreference_android_negativeButtonText));
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
        setDialogTitleColor(typedArray.getColor(R.styleable.DialogPreference_dialogTitleColor, -1));
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
                typedArray.getColor(R.styleable.DialogPreference_dialogMessageColor, -1));
    }

    /**
     * Obtains the button text color of the dialog, which is shown by the preference, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogButtonTextColor(@NonNull final TypedArray typedArray) {
        setDialogButtonTextColor(
                typedArray.getColor(R.styleable.DialogPreference_dialogButtonTextColor, -1));
    }

    /**
     * Obtains the disabled button text color of the dialog, which is shown by the preference, from
     * a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogDisabledButtonTextColor(@NonNull final TypedArray typedArray) {
        setDialogDisabledButtonTextColor(typedArray
                .getColor(R.styleable.DialogPreference_dialogDisabledButtonTextColor, -1));
    }

    /**
     * Obtains the background of the dialog, which is shown by the preference, from a specific typed
     * array.
     *
     * @param typedArray
     *         The typed array, the background should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogBackground(@NonNull final TypedArray typedArray) {
        int resourceId =
                typedArray.getResourceId(R.styleable.DialogPreference_dialogBackground, -1);

        if (resourceId != -1) {
            setDialogBackground(resourceId);
        } else {
            int color = typedArray.getColor(R.styleable.DialogPreference_dialogBackground, -1);

            if (color != -1) {
                setDialogBackgroundColor(color);
            }
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
                .getBoolean(R.styleable.DialogPreference_showValueAsSummary, defaultValue));
    }

    /**
     * Obtains the boolean value, which specifies whether the header of the dialog, which is shown
     * by the preference, should be shown, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the boolean value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainShowDialogHeader(@NonNull final TypedArray typedArray) {
        boolean defaultValue = getContext().getResources()
                .getBoolean(R.bool.dialog_preference_default_show_dialog_header);
        showDialogHeader(
                typedArray.getBoolean(R.styleable.DialogPreference_showDialogHeader, defaultValue));
    }

    /**
     * Obtains the background of the header of the dialog, which is shown by the preference, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the background should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogHeaderBackground(@NonNull final TypedArray typedArray) {
        int resourceId =
                typedArray.getResourceId(R.styleable.DialogPreference_dialogHeaderBackground, -1);

        if (resourceId != -1) {
            setDialogHeaderBackground(resourceId);
        } else {
            int color =
                    typedArray.getColor(R.styleable.DialogPreference_dialogHeaderBackground, -1);

            if (color != -1) {
                setDialogHeaderBackgroundColor(color);
            }
        }
    }

    /**
     * Obtains the icon of the header of the dialog, which is shown by the preference, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the icon should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainDialogHeaderIcon(@NonNull final TypedArray typedArray) {
        int resourceId =
                typedArray.getResourceId(R.styleable.DialogPreference_dialogHeaderIcon, -1);

        if (resourceId != -1) {
            setDialogHeaderIcon(resourceId);
        }
    }

    /**
     * Obtains the boolean value, which specifies whether the divider, which is located above the
     * buttons of the dialog, which is shown by the preference, should be shown, from a specific
     * typed array.
     *
     * @param typedArray
     *         The typed array, the boolean value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainShowDialogButtonBarDivider(@NonNull final TypedArray typedArray) {
        boolean defaultValue = getContext().getResources()
                .getBoolean(R.bool.dialog_preference_default_show_dialog_button_bar_divider);
        showDialogButtonBarDivider(typedArray
                .getBoolean(R.styleable.DialogPreference_showDialogButtonBarDivider, defaultValue));
    }

    /**
     * Obtains the color of the divider, which is located above the buttons of the dialog, which is
     * shown by the preference, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogButtonBarDividerColor(@NonNull final TypedArray typedArray) {
        int defaultValue =
                ContextCompat.getColor(getContext(), R.color.button_bar_divider_color_light);
        setDialogButtonBarDividerColor(typedArray
                .getColor(R.styleable.DialogPreference_dialogButtonBarDividerColor, defaultValue));
    }

    /**
     * Obtains, whether the dividers, which are shown above and below the list view of the
     * preference's dialog, should be shown when the list view is scrolled, or not, from a specific
     * typed array.
     *
     * @param typedArray
     *         The typed array, the boolean value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainShowDialogDividersOnScroll(@NonNull final TypedArray typedArray) {
        showDialogDividersOnScroll(typedArray
                .getBoolean(R.styleable.DialogPreference_showDialogDividersOnScroll, true));
    }

    /**
     * Shows the preference's dialog.
     *
     * @param dialogState
     *         The dialog's saved state as an instance of the class {@link Bundle} or null, if the
     *         dialog should be created from scratch
     */
    private void showDialog(@Nullable final Bundle dialogState) {
        MaterialDialog.Builder dialogBuilder =
                new MaterialDialog.Builder(getContext(), dialogTheme);
        dialogBuilder.setTitle(getDialogTitle());
        dialogBuilder.setMessage(getDialogMessage());
        dialogBuilder.setPositiveButton(getPositiveButtonText(), this);
        dialogBuilder.setNegativeButton(getNegativeButtonText(), this);
        dialogBuilder.showHeader(isDialogHeaderShown());
        dialogBuilder.showButtonBarDivider(isDialogButtonBarDividerShown());
        dialogBuilder.showDividersOnScroll(areDialogDividersShownOnScroll());

        if (dialogIconId != -1) {
            dialogBuilder.setIcon(dialogIconId);
        } else {
            dialogBuilder.setIcon(dialogIconBitmap);
        }

        if (dialogBackgroundId != -1) {
            dialogBuilder.setBackground(dialogBackgroundId);
        } else if (dialogBackgroundColor != -1) {
            dialogBuilder.setBackgroundColor(dialogBackgroundColor);
        } else if (dialogBackgroundBitmap != null) {
            dialogBuilder.setBackground(dialogBackgroundBitmap);
        }

        if (dialogHeaderBackgroundId != -1) {
            dialogBuilder.setHeaderBackground(dialogHeaderBackgroundId);
        } else if (dialogHeaderBackgroundColor != -1) {
            dialogBuilder.setHeaderBackgroundColor(dialogHeaderBackgroundColor);
        } else if (dialogHeaderBackgroundBitmap != null) {
            dialogBuilder.setHeaderBackground(dialogHeaderBackgroundBitmap);
        }

        if (dialogHeaderIconId != -1) {
            dialogBuilder.setHeaderIcon(dialogHeaderIconId);
        } else {
            dialogBuilder.setHeaderIcon(dialogHeaderIconBitmap);
        }

        if (getDialogTitleColor() != -1) {
            dialogBuilder.setTitleColor(getDialogTitleColor());
        }

        if (getDialogMessageColor() != -1) {
            dialogBuilder.setMessageColor(getDialogMessageColor());
        }

        if (getDialogButtonTextColor() != -1) {
            dialogBuilder.setButtonTextColor(getDialogButtonTextColor());
        }

        if (getDialogDisabledButtonTextColor() != -1) {
            dialogBuilder.setDisabledButtonTextColor(getDialogDisabledButtonTextColor());
        }

        if (getDialogButtonBarDividerColor() != -1) {
            dialogBuilder.setButtonBarDividerColor(getDialogButtonBarDividerColor());
        }

        if (customDialogTitleView != null) {
            dialogBuilder.setCustomTitle(customDialogTitleView);
        }

        if (customDialogTitleViewId != -1) {
            dialogBuilder.setCustomTitle(customDialogTitleViewId);
        }

        if (customDialogMessageView != null) {
            dialogBuilder.setCustomMessage(customDialogMessageView);
        }

        if (customDialogMessageViewId != -1) {
            dialogBuilder.setCustomMessage(customDialogMessageViewId);
        }

        if (customDialogButtonBarView != null) {
            dialogBuilder.setCustomButtonBar(customDialogButtonBarView);
        }

        if (customDialogButtonBarViewId != -1) {
            dialogBuilder.setCustomButtonBar(customDialogButtonBarViewId);
        }

        onPrepareDialog(dialogBuilder);

        dialog = dialogBuilder.create();
        dialog.setOnShowListener(this);
        dialog.setOnDismissListener(this);
        dialog.setOnCancelListener(this);
        dialog.setOnKeyListener(this);

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
     * Creates a new preference, which will show a dialog when clicked by the user.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public DialogPreference(@NonNull final Context context) {
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
    public DialogPreference(@NonNull final Context context,
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
    public DialogPreference(@NonNull final Context context,
                            @Nullable final AttributeSet attributeSet,
                            @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        obtainStyledAttributes(attributeSet, defaultStyle, 0);
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
    public DialogPreference(@NonNull final Context context,
                            @Nullable final AttributeSet attributeSet,
                            @AttrRes final int defaultStyle,
                            @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        obtainStyledAttributes(attributeSet, defaultStyle, defaultStyleResource);
    }

    /**
     * The method, which is invoked on subclasses to determine, whether the soft input mode should
     * be requested when the preference's dialog becomes shown, or not.
     *
     * @return True, if the soft input mode should be requested when the preference's dialog becomes
     * shown, false otherwise
     */
    protected boolean needInputMethod() {
        return false;
    }

    /**
     * The method, which is invoked on subclasses when the preference's dialog is about to be
     * created. The builder, which is passed as a method parameter may be manipulated by subclasses
     * in order to change the appearance of the dialog. When views are inflated inside this method,
     * the context of the builder should be used in order to ensure that the dialog's theme is
     * used.
     *
     * @param dialogBuilder
     *         The builder, which is used to create the preference's dialog, as an instance of the
     *         class MaterialDialog.Builder
     */
    protected void onPrepareDialog(@NonNull final MaterialDialog.Builder dialogBuilder) {

    }

    /**
     * The method, which is invoked on subclasses when the preference's dialog has been closed. This
     * method may be used within subclasses to evaluate the changes, which have been made while the
     * dialog was shown.
     *
     * @param positiveResult
     *         True, if the dialog has been close affirmatively, false otherwise
     */
    protected void onDialogClosed(final boolean positiveResult) {

    }

    /**
     * Returns the dialog, which is shown by the preference.
     *
     * @return The dialog, which is shown by the preference, as an instance of the class {@link
     * Dialog} or null, if the dialog is currently not shown
     */
    @Nullable
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
     *         The dialog, which should be set, as an instance of the class {@link Bitmap} or null,
     *         if no icon should be shown in the dialog
     */
    public final void setDialogIcon(@Nullable final Bitmap dialogIcon) {
        this.dialogIcon = new BitmapDrawable(getContext().getResources(), dialogIcon);
        this.dialogIconBitmap = dialogIcon;
        this.dialogIconId = -1;
    }

    /**
     * Sets the icon of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the icon, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid drawable resource
     */
    public final void setDialogIcon(@DrawableRes final int resourceId) {
        this.dialogIcon = ContextCompat.getDrawable(getContext(), resourceId);
        this.dialogIconBitmap = null;
        this.dialogIconId = resourceId;
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
     * Returns the text color of the buttons of the preference's dialog.
     *
     * @return The text color of the buttons as an {@link Integer} value or -1, if no custom color
     * is set
     */
    public final int getDialogButtonTextColor() {
        return dialogButtonTextColor;
    }

    /**
     * Sets the text color of the buttons of the preference's dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom color
     *         should be set
     */
    public final void setDialogButtonTextColor(@ColorInt final int color) {
        this.dialogButtonTextColor = color;
    }

    /**
     * Returns the text color of the buttons of the preference's dialog when disabled.
     *
     * @return The disabled text color of the buttons as an {@link Integer} value or -1, if no
     * custom color is set
     */
    public final int getDialogDisabledButtonTextColor() {
        return dialogDisabledButtonTextColor;
    }

    /**
     * Sets the text color of the buttons of the preference's dialog when disabled.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value, or -1, if no custom
     *         color should be set
     */
    public final void setDialogDisabledButtonTextColor(@ColorInt final int color) {
        this.dialogDisabledButtonTextColor = color;
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
     *         The background, which should be set, as an instance of the class {@link Bitmap} or
     *         null, if no custom background should be set
     */
    public final void setDialogBackground(@Nullable final Bitmap background) {
        this.dialogBackground = new BitmapDrawable(getContext().getResources(), background);
        this.dialogBackgroundBitmap = background;
        this.dialogBackgroundId = -1;
        this.dialogBackgroundColor = -1;
    }

    /**
     * Sets the background of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     */
    public final void setDialogBackground(@DrawableRes final int resourceId) {
        this.dialogBackground = ContextCompat.getDrawable(getContext(), resourceId);
        this.dialogBackgroundBitmap = null;
        this.dialogBackgroundId = resourceId;
        this.dialogBackgroundColor = -1;
    }

    /**
     * Sets the background color of the preference's dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom
     *         background color should be set
     */
    public final void setDialogBackgroundColor(@ColorInt final int color) {
        this.dialogBackground = new ColorDrawable(color);
        this.dialogBackgroundBitmap = null;
        this.dialogBackgroundId = -1;
        this.dialogBackgroundColor = color;
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

    /**
     * Returns, whether the header of the preference's dialog is shown, or not.
     *
     * @return True, if the header of the preference's dialog is shown, false otherwise
     */
    public final boolean isDialogHeaderShown() {
        return showDialogHeader;
    }

    /**
     * Sets, whether the header of the preference's dialog should be shown, or not.
     *
     * @param show
     *         True, if the header of the preference's dialog should be shown, false otherwise
     */
    public final void showDialogHeader(final boolean show) {
        this.showDialogHeader = show;
    }

    /**
     * Returns the background of the header of the preference's dialog.
     *
     * @return The background of the header of the preference's dialog as an instance of the class
     * {@link Drawable}
     */
    public final Drawable getDialogHeaderBackground() {
        return dialogHeaderBackground;
    }

    /**
     * Sets the background of the header of the preference's dialog.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Bitmap} or
     *         null, if no custom background should be set
     */
    public final void setDialogHeaderBackground(@Nullable final Bitmap background) {
        this.dialogHeaderBackground = new BitmapDrawable(getContext().getResources(), background);
        this.dialogHeaderBackgroundBitmap = background;
        this.dialogHeaderBackgroundId = -1;
        this.dialogHeaderBackgroundColor = -1;
    }

    /**
     * Sets the background of the header of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     */
    public final void setDialogHeaderBackground(@DrawableRes final int resourceId) {
        this.dialogHeaderBackground = ContextCompat.getDrawable(getContext(), resourceId);
        this.dialogHeaderBackgroundBitmap = null;
        this.dialogHeaderBackgroundId = resourceId;
        this.dialogHeaderBackgroundColor = -1;
    }

    /**
     * Sets the background color of the header of the preference's dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom color
     *         should be set
     */
    public final void setDialogHeaderBackgroundColor(@ColorInt final int color) {
        this.dialogHeaderBackground = new ColorDrawable(color);
        this.dialogHeaderBackgroundBitmap = null;
        this.dialogHeaderBackgroundId = -1;
        this.dialogHeaderBackgroundColor = color;
    }

    /**
     * Returns the icon of the header of the preference's dialog.
     *
     * @return The icon of the header of the preference's dialog as an instance of the class {@link
     * Drawable}
     */
    public final Drawable getDialogHeaderIcon() {
        return dialogHeaderIcon;
    }

    /**
     * Sets the icon of the header of the preference's dialog.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Bitmap} or null, if
     *         no icon should be set
     */
    public final void setDialogHeaderIcon(@Nullable final Bitmap icon) {
        this.dialogHeaderIcon = new BitmapDrawable(getContext().getResources(), icon);
        this.dialogHeaderIconBitmap = icon;
        this.dialogHeaderIconId = -1;
    }

    /**
     * Sets the icon of the header of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the icon, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid drawable resource
     */
    public final void setDialogHeaderIcon(@DrawableRes final int resourceId) {
        this.dialogHeaderIcon = ContextCompat.getDrawable(getContext(), resourceId);
        this.dialogHeaderIconBitmap = null;
        this.dialogHeaderIconId = resourceId;
    }

    /**
     * Returns, whether the divider, which is located above the buttons of the preference's dialog,
     * is shown, or not.
     *
     * @return True, if the divider, which is located above the buttons of the preference's dialog,
     * is shown, false otherwise
     */
    public final boolean isDialogButtonBarDividerShown() {
        return showDialogButtonBarDivider;
    }

    /**
     * Sets, whether the divider, which is located above the buttons of the preference's dialog,
     * should be shown, or not.
     *
     * @param show
     *         True, if the divider, which is located above the buttons of the preference's dialog
     *         should be shown, false otherwise
     */
    public final void showDialogButtonBarDivider(final boolean show) {
        this.showDialogButtonBarDivider = show;
    }

    /**
     * Returns the color of the divider, which is located above the buttons of the preference's
     * dialog.
     *
     * @return The color of the divider, which is located above the buttons of the preference's
     * dialog, as an {@link Integer} value or -1, if no custom color is set
     */
    public final int getDialogButtonBarDividerColor() {
        return dialogButtonBarDividerColor;
    }

    /**
     * Sets the color of the divider, which is located above the buttons of the preference's
     * dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom color
     *         should be set
     */
    public final void setDialogButtonBarDividerColor(@ColorInt final int color) {
        this.dialogButtonBarDividerColor = color;
    }

    /**
     * Returns, whether the dividers, which are located above and below the list view of the
     * preference's dialog, are shown when the list view is scrolled, or not.
     *
     * @return True, if the dividers, which are located above and below the list view of the
     * preference's dialog, are shown when the list view is scrolled, false otherwise
     */
    public final boolean areDialogDividersShownOnScroll() {
        return showDialogDividersOnScroll;
    }

    /**
     * Sets, whether the dividers, which are located above and below the list view of the
     * preference's dialog, should be shown when the list view is scrolled, or not.
     *
     * @param show
     *         True, if the dividers, which are located above and below the list view of the
     *         preference's dialog, should be shown when the list view is scrolled, false otherwise
     */
    public final void showDialogDividersOnScroll(final boolean show) {
        this.showDialogDividersOnScroll = show;
    }

    /**
     * Sets the custom view, which should be used to show the title of the preference's dialog.
     *
     * @param view
     *         The custom view, which should be set, as an instance of the class {@link View} or
     *         null, if no custom view should be used
     */
    public final void setCustomDialogTitle(@Nullable final View view) {
        this.customDialogTitleView = view;
    }

    /**
     * Sets the custom view, which should be used to show the title of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the custom view, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid layout resource
     */
    public final void setCustomDialogTitle(@LayoutRes final int resourceId) {
        this.customDialogTitleViewId = resourceId;
    }

    /**
     * Sets the custom view, which should be used to show the message of the preference's dialog.
     *
     * @param view
     *         The custom view, which should be set, as an instance of the class {@link View} or
     *         null, if no custom view should be used
     */
    public final void setCustomDialogMessage(@Nullable final View view) {
        this.customDialogMessageView = view;
    }

    /**
     * Sets the custom view, which should be used to show the message of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of custom view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     */
    public final void setCustomDialogMessage(@LayoutRes final int resourceId) {
        this.customDialogMessageViewId = resourceId;
    }

    /**
     * Sets the custom view, which should be used to show the buttons of the preference's dialog.
     *
     * @param view
     *         The custom view, which should be set, as an instance of the class {@link View} or
     *         null, if no custom view should be set
     */
    public final void setCustomDialogButtonBar(@Nullable final View view) {
        this.customDialogButtonBarView = view;
    }

    /**
     * Sets the custom view, which should be used to show the buttons of the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the custom view, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid layout resource
     */
    public final void setCustomDialogButtonBar(@LayoutRes final int resourceId) {
        this.customDialogButtonBarViewId = resourceId;
    }

    /**
     * Sets the listener, which should be notified, when a button of the preference's dialog has
     * been clicked.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link OnClickListener}
     *         or null, if no listener should be notified
     */
    public final void setOnClickListener(@Nullable final OnClickListener listener) {
        onClickListener = listener;
    }

    /**
     * Sets the listener, which should be notified, when the preference's dialog has been shown.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link OnShowListener}
     *         or null, if no listener should be notified
     */
    public final void setOnShowListener(@Nullable final OnShowListener listener) {
        onShowListener = listener;
    }

    /**
     * Sets the listener, which should be notified, when the preference's dialog has been
     * dismissed.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         OnDismissListener} or null, if no listener should be notified
     */
    public final void setOnDismissListener(@Nullable final OnDismissListener listener) {
        onDismissListener = listener;
    }

    /**
     * Sets the listener, which should be notified, when the preference's dialog has been canceled.
     *
     * The listener will only be invoked, when the dialog is canceled. Cancel events alone will not
     * capture all ways that the dialog might be dismissed. If the creator needs to know when a
     * dialog is dismissed in general, use {@link #setOnDismissListener}.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         OnCancelListener} or null, if no listener should be notified
     */
    public final void setOnCancelListener(@Nullable final OnCancelListener listener) {
        onCancelListener = listener;
    }

    /**
     * Sets the listener, which should be notified, when a key has been dispatched to the
     * preference's dialog.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link OnKeyListener}
     *         or null, if no listener should be notified
     */
    public final void setOnKeyListener(@Nullable final OnKeyListener listener) {
        onKeyListener = listener;
    }

    @CallSuper
    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        dialogResultPositive = (which == Dialog.BUTTON_POSITIVE);

        if (onClickListener != null) {
            onClickListener.onClick(dialog, which);
        }
    }

    @CallSuper
    @Override
    public void onShow(final DialogInterface dialog) {
        if (onShowListener != null) {
            onShowListener.onShow(dialog);
        }
    }

    @CallSuper
    @Override
    public void onDismiss(final DialogInterface dialog) {
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }

        this.dialog = null;
        onDialogClosed(dialogResultPositive);
    }

    @CallSuper
    @Override
    public void onCancel(final DialogInterface dialog) {
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialog);
        }
    }

    @CallSuper
    @Override
    public boolean onKey(final DialogInterface dialog, final int keyCode, final KeyEvent event) {
        return onKeyListener != null && onKeyListener.onKey(dialog, keyCode, event);
    }

    @Override
    protected final void onClick() {
        if (!isDialogShown()) {
            showDialog(null);
        }
    }

    @CallSuper
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);

        if (isDialogShown()) {
            savedState.dialogState = dialog.onSaveInstanceState();
            dialog.dismiss();
            dialog = null;
        }

        return savedState;
    }

    @CallSuper
    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (state != null && state instanceof SavedState) {
            SavedState savedState = (SavedState) state;

            if (savedState.dialogState != null) {
                showDialog(savedState.dialogState);
            }

            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}