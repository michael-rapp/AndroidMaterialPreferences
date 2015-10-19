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

import de.mrapp.android.dialog.MaterialDialogBuilder;
import de.mrapp.android.validation.EditText;
import de.mrapp.android.validation.ValidationListener;

/**
 * A preference, which allows to enter a text via an EditText widget. The entered text will only be
 * persisted, if confirmed by the user.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class EditTextPreference extends AbstractValidateableDialogPreference<CharSequence> {

    /**
     * A data structure, which allows to save the internal state of an {@link EditTextPreference}.
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
         * The saved value of the attribute "text".
         */
        public String text;

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * EditTextPreference}. This constructor is called by derived classes when saving their
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
         * EditTextPreference}. This constructor is used when reading from a parcel. It reads the
         * state of the superclass.
         *
         * @param source
         *         The parcel to read read from as a instance of the class {@link Parcel}. The
         *         parcel may not be null
         */
        public SavedState(@NonNull final Parcel source) {
            super(source);
            text = source.readString();
        }

        @Override
        public final void writeToParcel(final Parcel destination, final int flags) {
            super.writeToParcel(destination, flags);
            destination.writeString(text);
        }

    }

    /**
     * The edit text, which is contained by the preference's dialog.
     */
    private EditText editText;

    /**
     * The currently persisted text.
     */
    private String text;

    /**
     * Initializes the preference.
     */
    private void initialize() {
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    /**
     * Creates a new preference, which allows to enter a text via an EditText widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public EditTextPreference(@NonNull final Context context) {
        super(context);
        initialize();
    }

    /**
     * Creates a new preference, which allows to enter a text via an EditText widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public EditTextPreference(@NonNull final Context context,
                              @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize();
    }

    /**
     * Creates a new preference, which allows to enter a text via an EditText widget.
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
    public EditTextPreference(@NonNull final Context context,
                              @Nullable final AttributeSet attributeSet, final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize();
    }

    /**
     * Creates a new preference, which allows to enter a text via an EditText widget.
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
    public EditTextPreference(@NonNull final Context context,
                              @Nullable final AttributeSet attributeSet, final int defaultStyle,
                              final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize();
    }

    /**
     * Returns the currently persisted text of the preference.
     *
     * @return The currently persisted text as a {@link String}
     */
    public final String getText() {
        return text;
    }

    /**
     * Sets the current text of the preference. By setting a value, it will be persisted.
     *
     * @param text
     *         The text, which should be set, as a {@link String}
     */
    public final void setText(@Nullable final String text) {
        boolean hasDisabledDependents = shouldDisableDependents();
        this.text = text;
        persistString(text);
        boolean isDisabelingDependents = shouldDisableDependents();

        if (isDisabelingDependents != hasDisabledDependents) {
            notifyDependencyChange(isDisabelingDependents);
        }

        notifyChanged();
    }

    @Override
    public final CharSequence getSummary() {
        if (isValueShownAsSummary()) {
            return getText();
        } else {
            return super.getSummary();
        }
    }

    @Override
    public final boolean shouldDisableDependents() {
        return TextUtils.isEmpty(getText()) || super.shouldDisableDependents();
    }

    @Override
    public final boolean validate() {
        return editText == null || editText.validate();
    }

    @Override
    protected final void onPrepareValidateableDialog(
            @NonNull final MaterialDialogBuilder dialogBuilder) {
        editText = (EditText) View.inflate(getContext(), R.layout.edit_text, null);
        editText.addAllValidators(getValidators());
        editText.validateOnValueChange(isValidatedOnValueChange());
        editText.validateOnFocusLost(isValidatedOnFocusLost());
        editText.setHelperText(getHelperText());
        editText.setHelperTextColor(getHelperTextColor());
        editText.setErrorColor(getErrorColor());

        for (ValidationListener<CharSequence> listener : getValidationListeners()) {
            editText.addValidationListener(listener);
        }

        editText.setText(getText());
        editText.setSelection(getText() != null ? getText().length() : 0);
        dialogBuilder.setView(editText);
    }

    @Override
    protected final void onDialogClosed(final boolean positiveResult) {
        if (positiveResult) {
            String newValue = editText.getText().toString();

            if (callChangeListener(newValue)) {
                setText(newValue);
            }
        }

        editText = null;
    }

    @Override
    protected final boolean needInputMethod() {
        return true;
    }

    @Override
    protected final Object onGetDefaultValue(final TypedArray typedArray, final int index) {
        return typedArray.getString(index);
    }

    @Override
    protected final void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
        setText(restoreValue ? getPersistedString(getText()) : (String) defaultValue);
    }

    @Override
    protected final Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        if (!isPersistent()) {
            SavedState savedState = new SavedState(superState);
            savedState.text = getText();
            return savedState;
        }

        return superState;
    }

    @Override
    protected final void onRestoreInstanceState(final Parcelable state) {
        if (state != null && state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            setText(savedState.text);
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}