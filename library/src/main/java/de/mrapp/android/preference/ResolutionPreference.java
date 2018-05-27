/*
 * Copyright 2014 - 2018 Michael Rapp
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
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.regex.Pattern;

import de.mrapp.android.dialog.MaterialDialog;
import de.mrapp.android.util.view.AbstractSavedState;
import de.mrapp.android.validation.EditText;
import de.mrapp.android.validation.ValidationListener;
import de.mrapp.android.validation.Validators;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotEmpty;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A preference, which allows to enter a two-dimensional image or video resulution via two EditText
 * widgets. The entered resolution will only be persisted, if confirmed by the user.
 *
 * @author Michael Rapp
 * @since 1.3.0
 */
public class ResolutionPreference extends AbstractValidateableDialogPreference<CharSequence> {

    /**
     * A data structure, which allows to save the internal state of a {@link ResolutionPreference}.
     */
    public static class SavedState extends AbstractSavedState {

        /**
         * A creator, which allows to create instances of the class {@link ResolutionPreference}
         * from parcels.
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
         * The saved value of the attribute "width".
         */
        public int width;

        /**
         * The saved value of the attribute "height".
         */
        public int height;

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * ResolutionPreference}. This constructor is called by derived classes when saving their
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
         * ResolutionPreference}. This constructor is used when reading from a parcel. It reads the
         * state of the superclass.
         *
         * @param source
         *         The parcel to read read from as a instance of the class {@link Parcel}. The
         *         parcel may not be null
         */
        public SavedState(@NonNull final Parcel source) {
            super(source);
            width = source.readInt();
            height = source.readInt();
        }

        @Override
        public final void writeToParcel(final Parcel destination, final int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(width);
            destination.writeInt(height);
        }

    }

    /**
     * The regular expression, which is used to ensure, that a dimension of a resolution is at least
     * 1.
     */
    private static final Pattern MIN_VALUE_REGEX = Pattern.compile("^(?!0).*");

    /**
     * The edit text widget, which allows to enter the width of the resolution.
     */
    private EditText widthEditText;

    /**
     * The edit text widget, which allows to enter the height of the resolution.
     */
    private EditText heightEditText;

    /**
     * The width of the currently persisted resolution.
     */
    private int width;

    /**
     * The height of the currently persisted resolution.
     */
    private int height;

    /**
     * The unit, which is used for textual representation of the preference's resolution.
     */
    private CharSequence unit;

    /**
     * The hint of the edit text widget, which allows to enter the width of the resolution.
     */
    private CharSequence widthHint;

    /**
     * The hint of the edit text widget, which allows to enter the height of the resolution.
     */
    private CharSequence heightHint;

    /**
     * Initializes the preference.
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
    private void initialize(@Nullable final AttributeSet attributeSet,
                            @AttrRes final int defaultStyle,
                            @StyleRes final int defaultStyleResource) {
        obtainStyledAttributes(attributeSet, defaultStyle, defaultStyleResource);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        addValidator(
                Validators.notEmpty(getContext(), R.string.resolution_not_empty_error_message));
        addValidator(Validators.number(getContext(), R.string.resolution_number_error_message));
        addValidator(Validators
                .regex(getContext(), R.string.resolution_min_value_error_message, MIN_VALUE_REGEX));
    }

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
                .obtainStyledAttributes(attributeSet, R.styleable.AbstractUnitPreference,
                        defaultStyle, defaultStyleResource);

        try {
            obtainUnit(typedArray);
            obtainWidthHint(typedArray);
            obtainHeightHint(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the unit of the resolution, which is shown in the preference's dialog, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the unit should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainUnit(@NonNull final TypedArray typedArray) {
        CharSequence obtainedUnit = typedArray.getText(R.styleable.AbstractUnitPreference_unit);

        if (obtainedUnit == null) {
            obtainedUnit = getContext().getText(R.string.resolution_preference_unit);
        }

        setUnit(obtainedUnit);
    }

    /**
     * Obtains the hint of the edit text widget, which allows to enter the width of the resolution,
     * from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the hint should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainWidthHint(@NonNull final TypedArray typedArray) {
        CharSequence obtainedHint = typedArray.getText(R.styleable.ResolutionPreference_widthHint);

        if (obtainedHint == null) {
            obtainedHint = getContext().getText(R.string.resolution_preference_width_hint);
        }

        setWidthHint(obtainedHint);
    }

    /**
     * Obtains the hint of the edit text widget, which allows to enter the height of the resolution,
     * from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the hint should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainHeightHint(@NonNull final TypedArray typedArray) {
        CharSequence obtainedHint = typedArray.getText(R.styleable.ResolutionPreference_heightHint);

        if (obtainedHint == null) {
            obtainedHint = getContext().getText(R.string.resolution_preference_height_hint);
        }

        setHeightHint(obtainedHint);
    }

    /**
     * Creates a new preference, which allows to enter a two-dimensional image or video resolution
     * via two EditText widgets.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public ResolutionPreference(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new preference, which allows to enter a two-dimensional image or video resolution
     * via two EditText widgets.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public ResolutionPreference(@NonNull final Context context,
                                @Nullable final AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.dialogPreferenceStyle);
    }

    /**
     * Creates a new preference, which allows to enter a two-dimensional image or video resolution
     * via two EditText widgets.
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
    public ResolutionPreference(@NonNull final Context context,
                                @Nullable final AttributeSet attributeSet,
                                @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet, defaultStyle, 0);
    }

    /**
     * Creates a new preference, which allows to enter a two-dimensional image or video resolution
     * via two EditText widgets.
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
    public ResolutionPreference(@NonNull final Context context,
                                @Nullable final AttributeSet attributeSet,
                                @AttrRes final int defaultStyle,
                                @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet, defaultStyle, defaultStyleResource);
    }

    /**
     * Parses a specific textual representation of a resolution and returns its dimensions.
     *
     * @param context
     *         The context, which should be used, as an instance of the class {@link Context}. The
     *         context may not be null
     * @param resolution
     *         The textual representation of the resolution, which should be parsed, as a {@link
     *         String}. The resolution may neither be null, nor empty
     * @return A pair, which contains the width and height of the given resolution, as an instance
     * of the class {@link Pair}
     */
    public static Pair<Integer, Integer> parseResolution(@NonNull final Context context,
                                                         @NonNull final String resolution) {
        ensureNotNull(context, "The context may not be null");
        ensureNotNull(resolution, "The resolution may not be null");
        ensureNotEmpty(resolution, "The resolution may not be empty");
        String separator = context.getString(R.string.resolution_preference_separator);
        String[] dimensions = resolution.split(separator);

        if (dimensions.length != 2) {
            throw new IllegalArgumentException("Malformed resolution: " + resolution);
        }

        try {
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);
            return Pair.create(width, height);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Resolution contains invalid dimension: " + resolution, e);
        }
    }

    /**
     * Creates and returns the textual representation of a specific resolution.
     *
     * @param context
     *         The context, which should be used, as an instance of the class {@link Context}. The
     *         context may not be null
     * @param width
     *         The width of the resolution as an {@link Integer} value. The width must be at least
     *         1
     * @param height
     *         The height of the resolution as an {@link Integer} value. The height must be at least
     *         1
     * @return The textual representation of the given resolution as a {@link String}
     */
    public static String formatResolution(@NonNull final Context context, final int width,
                                          final int height) {
        ensureNotNull(context, "The context may not be null");
        String separator = context.getString(R.string.resolution_preference_separator);
        return width + separator + height;
    }

    /**
     * Returns the width of the currently persisted resolution of the preference.
     *
     * @return The width of the currently persisted resolution as an {@link Integer} value
     */
    public final int getWidth() {
        return width;
    }

    /**
     * Returns the height of the currently persisted resolution of the preference.
     *
     * @return The height of the currently persisted resolution as an {@link Integer} value
     */
    public final int getHeight() {
        return height;
    }

    /**
     * Sets the current resolution of the preference. By setting a value, it will be persisted.
     *
     * @param width
     *         The width, which should be set, as an {@link Integer} value. The width must be at
     *         least 1
     * @param height
     *         The height, which should be set, as an {@link Integer} value. The height must be at
     *         least 1
     */
    public final void setResolution(final int width, final int height) {
        ensureAtLeast(width, 1, "The width must be at least 1");
        ensureAtLeast(height, 1, "The height must be at least 1");
        this.width = width;
        this.height = height;
        persistString(formatResolution(getContext(), width, height));
        notifyChanged();
    }

    /**
     * Returns the unit, which is used for textual representation of the preference's resolution.
     *
     * @return The unit, which is used for textual representation of the preference's resolution, as
     * an instance of the type {@link CharSequence} or null, if no unit is used
     */
    public final CharSequence getUnit() {
        return unit;
    }

    /**
     * Sets the unit, which should be used for textual representation of the preference's
     * resolution.
     *
     * @param unit
     *         The unit, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no unit should be used
     */
    public final void setUnit(@Nullable final CharSequence unit) {
        this.unit = unit;
    }

    /**
     * Sets the unit, which should be used for textual representation of the preference's
     * resolution.
     *
     * @param resourceId
     *         The resource id of the unit, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setUnit(@StringRes final int resourceId) {
        setUnit(getContext().getText(resourceId));
    }

    /**
     * Returns the hint of the edit text widget, which allows to enter the width of the resolution.
     *
     * @return The hint of the edit text widget, which allows to enter the width of the resolution,
     * as an instance of the type {@link CharSequence} or null, if no hint is set
     */
    public final CharSequence getWidthHint() {
        return widthHint;
    }

    /**
     * Sets the hint of the edit text widget, which allows to enter the width of the resolution.
     *
     * @param hint
     *         The hint, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no hint should be set
     */
    public final void setWidthHint(@Nullable final CharSequence hint) {
        this.widthHint = hint;
    }

    /**
     * Sets the hint of the edit text widget, which allows to enter the width of the resolution.
     *
     * @param resourceId
     *         The resource id of the hint, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setWidthHint(@StringRes final int resourceId) {
        setWidthHint(getContext().getText(resourceId));
    }

    /**
     * Returns the hint of the edit text widget, which allows to enter the height of the
     * resolution.
     *
     * @return The hint of the edit text widget, which allows to enter the height of the resolution,
     * as an instance of the type {@link CharSequence} or null, if no hint is set
     */
    public final CharSequence getHeightHint() {
        return heightHint;
    }

    /**
     * Sets the hint of the edit text widget, which allows to enter the height of the resolution.
     *
     * @param hint
     *         The hint, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no hint should be set
     */
    public final void setHeightHint(@Nullable final CharSequence hint) {
        this.heightHint = hint;
    }

    /**
     * Sets the hint of the edit text widget, which allows to enter the height of the resolution.
     *
     * @param resourceId
     *         The resource id of the hint, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setHeightHint(@StringRes final int resourceId) {
        setHeightHint(getContext().getText(resourceId));
    }

    @Override
    public final CharSequence getSummary() {
        if (isValueShownAsSummary()) {
            String separator = getContext().getString(R.string.resolution_preference_separator);
            return getWidth() + " " + separator + " " + getHeight() + " " + getUnit();
        } else {
            return super.getSummary();
        }
    }

    @Override
    public final boolean validate() {
        return (widthEditText == null || widthEditText.validate()) &&
                (heightEditText == null || heightEditText.validate());
    }

    @Override
    protected final void onPrepareDialog(@NonNull final MaterialDialog.Builder dialogBuilder) {
        super.onPrepareDialog(dialogBuilder);
        View view = View.inflate(dialogBuilder.getContext(), R.layout.resolution, null);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        view.setLayoutParams(layoutParams);
        TextView unitTextView = view.findViewById(R.id.unit_text_view);
        unitTextView.setText(getUnit());

        widthEditText = view.findViewById(R.id.width_edit_text);
        widthEditText.addAllValidators(getValidators());
        widthEditText.validateOnValueChange(isValidatedOnValueChange());
        widthEditText.validateOnFocusLost(isValidatedOnFocusLost());
        widthEditText.setErrorColor(getErrorColor());
        widthEditText.setHint(getWidthHint());

        heightEditText = view.findViewById(R.id.height_edit_text);
        heightEditText.addAllValidators(getValidators());
        heightEditText.validateOnValueChange(isValidatedOnValueChange());
        heightEditText.validateOnFocusLost(isValidatedOnFocusLost());
        heightEditText.setErrorColor(getErrorColor());
        heightEditText.setHint(getHeightHint());

        for (ValidationListener<CharSequence> listener : getValidationListeners()) {
            widthEditText.addValidationListener(listener);
            heightEditText.addValidationListener(listener);
        }

        widthEditText.setText(Integer.toString(getWidth()));
        heightEditText.setText(Integer.toString(getHeight()));
        widthEditText.setSelection(
                widthEditText.getText() != null ? widthEditText.getText().length() : 0);
        dialogBuilder.setView(view);
    }

    @Override
    protected final void onDialogClosed(final boolean positiveResult) {
        if (positiveResult) {
            int newWidth = Integer.parseInt(widthEditText.getText().toString());
            int newHeight = Integer.parseInt(heightEditText.getText().toString());

            if (callChangeListener(formatResolution(getContext(), newWidth, newHeight))) {
                setResolution(newWidth, newHeight);
            }
        }

        widthEditText = null;
        heightEditText = null;
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
        String resolution = restoreValue ?
                getPersistedString(formatResolution(getContext(), getWidth(), getHeight())) :
                (String) defaultValue;
        Pair<Integer, Integer> dimensions = parseResolution(getContext(), resolution);
        setResolution(dimensions.first, dimensions.second);
    }

    @Override
    protected final Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        if (!isPersistent()) {
            SavedState savedState = new SavedState(superState);
            savedState.width = getWidth();
            savedState.height = getHeight();
            return savedState;
        }

        return superState;
    }

    @Override
    protected final void onRestoreInstanceState(final Parcelable state) {
        if (state != null && state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            setResolution(savedState.width, savedState.height);
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}