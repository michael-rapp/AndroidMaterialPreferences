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
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import de.mrapp.android.dialog.DialogValidator;
import de.mrapp.android.dialog.MaterialDialog;
import de.mrapp.android.dialog.model.ValidateableDialog;
import de.mrapp.android.util.datastructure.ListenerList;
import de.mrapp.android.validation.Validateable;
import de.mrapp.android.validation.ValidationListener;
import de.mrapp.android.validation.Validator;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all dialog preferences, which can be validated.
 *
 * @param <ValueType>
 *         The type of the values, which can be validated
 * @author Michael Rapp
 * @since 1.2.0
 */
public abstract class AbstractValidateableDialogPreference<ValueType> extends DialogPreference
        implements DialogValidator, Validateable<ValueType> {

    /**
     * A set, which contains the validators, which are used to validate the view, which is contained
     * by the preference's dialog.
     */
    private transient Set<Validator<ValueType>> validators;

    /**
     * A set, which contains the listeners, which are notified when the view, which is contained by
     * the preference's dialog, has been validated.
     */
    private transient ListenerList<ValidationListener<ValueType>> validationListeners;

    /**
     * True, if the view, which is contained by the preference's dialog should be automatically
     * validated, when its value has been changed, false otherwise.
     */
    private boolean validateOnValueChange;

    /**
     * True, if the view, which is contained by the preference's dialog should be automatically
     * validated, when it has lost its focus, false otherwise.
     */
    private boolean validateOnFocusLost;

    /**
     * The helper text of the view, which is contained by the preference's dialog.
     */
    private CharSequence helperText;

    /**
     * The error color of the view, which is contained by the preference's dialog.
     */
    private int errorColor;

    /**
     * The helper text color of the view, which is contained by the preference's dialog.
     */
    private int helperTextColor;

    /**
     * Initializes the preference.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet}
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the
     *         preference, used only if the default style is 0 or can not be found in the theme. Can
     *         be 0 to not look for defaults
     */
    private void initialize(final AttributeSet attributeSet, @AttrRes final int defaultStyle,
                            @StyleRes final int defaultStyleResource) {
        validators = new LinkedHashSet<>();
        validationListeners = new ListenerList<>();
        obtainStyledAttributes(attributeSet, defaultStyle, defaultStyleResource);
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
                .obtainStyledAttributes(attributeSet, R.styleable.AbstractValidateableView,
                        defaultStyle, defaultStyleResource);

        try {
            obtainHelperText(typedArray);
            obtainHelperTextColor(typedArray);
            obtainErrorColor(typedArray);
            obtainValidateOnValueChange(typedArray);
            obtainValidateOnFocusLost(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the helper text from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the helper text should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainHelperText(@NonNull final TypedArray typedArray) {
        setHelperText(typedArray.getString(R.styleable.AbstractValidateableView_helperText));
    }

    /**
     * Obtains the color of the helper text from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color of the helper text should be obtained from, as an instance
     *         of the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainHelperTextColor(@NonNull final TypedArray typedArray) {
        setHelperTextColor(typedArray.getColor(R.styleable.AbstractValidateableView_helperTextColor,
                ContextCompat.getColor(getContext(), R.color.default_helper_text_color)));
    }

    /**
     * Obtains the color, which is used to indicate validation errors, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the error color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainErrorColor(@NonNull final TypedArray typedArray) {
        setErrorColor(typedArray.getColor(R.styleable.AbstractValidateableView_errorColor,
                ContextCompat.getColor(getContext(), R.color.default_error_color)));
    }

    /**
     * Obtains, whether the value of the view should be validated, when its value has been changed,
     * or not, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, it should be obtained from, whether the value of the view should be
     *         validated, when its value has been changed, or not, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainValidateOnValueChange(@NonNull final TypedArray typedArray) {
        boolean defaultValue = getContext().getResources()
                .getBoolean(R.bool.validateable_dialog_preference_default_validate_on_value_change);
        validateOnValueChange(typedArray
                .getBoolean(R.styleable.AbstractValidateableView_validateOnValueChange,
                        defaultValue));
    }

    /**
     * Obtains, whether the value of the view should be validated, when the view loses its focus, or
     * not, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, it should be obtained from, whether the value of the view should be
     *         validated, when the view loses its focus, or not, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainValidateOnFocusLost(@NonNull final TypedArray typedArray) {
        boolean defaultValue = getContext().getResources()
                .getBoolean(R.bool.validateable_dialog_preference_default_validate_on_focus_lost);
        validateOnFocusLost(typedArray
                .getBoolean(R.styleable.AbstractValidateableView_validateOnFocusLost,
                        defaultValue));
    }

    /**
     * Returns the listeners, which are notified when the view, which is contained by the
     * preference's dialog, has been validated.
     *
     * @return A list, which contains the listeners, which are notified when the view, which is
     * contained by the preference's dialog, has been validated, as an instance of the type
     * ListenerList or an empty list, if no validators are used
     */
    protected final ListenerList<ValidationListener<ValueType>> getValidationListeners() {
        return validationListeners;
    }

    /**
     * Creates a new dialog preference, which can be validated.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public AbstractValidateableDialogPreference(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new dialog preference, which can be validated.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public AbstractValidateableDialogPreference(@NonNull final Context context,
                                                @Nullable final AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /**
     * Creates a new dialog preference, which can be validated.
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
    public AbstractValidateableDialogPreference(@NonNull final Context context,
                                                @Nullable final AttributeSet attributeSet,
                                                @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet, defaultStyle, 0);
    }

    /**
     * Creates a new dialog preference, which can be validated.
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
    public AbstractValidateableDialogPreference(@NonNull final Context context,
                                                @Nullable final AttributeSet attributeSet,
                                                @AttrRes final int defaultStyle,
                                                @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet, defaultStyle, defaultStyleResource);
    }

    /**
     * Returns the helper text of the view, which is contained by the preference's dialog.
     *
     * @return The helper text of the view, which is contained by the preference's dialog, as an
     * instance of the type {@link CharSequence} or null, if no helper text is shown
     */
    public final CharSequence getHelperText() {
        return helperText;
    }

    /**
     * Sets the helper text of the view, which is contained by the preference's dialog.
     *
     * @param helperText
     *         The helper text, which should be set, as an instance of the type {@link CharSequence}
     *         or null, if no helper text should be shown
     */
    public final void setHelperText(@Nullable final CharSequence helperText) {
        this.helperText = helperText;
    }

    /**
     * Sets the helper text of the view, which is contained by the preference's dialog.
     *
     * @param resourceId
     *         The resource ID of the string resource, which contains the helper text, which should
     *         be set, as an {@link Integer} value. The resource ID must correspond to a valid
     *         string resource
     */
    public final void setHelperText(@StringRes final int resourceId) {
        setHelperText(getContext().getText(resourceId));
    }

    /**
     * Returns the color, which is used to indicate validation errors.
     *
     * @return The color, which is used to indicate validation errors, as an {@link Integer} value
     */
    public final int getErrorColor() {
        return errorColor;
    }

    /**
     * Sets the color, which should be used to indicate validation errors.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setErrorColor(@ColorInt final int color) {
        this.errorColor = color;
    }

    /**
     * Returns the color of the helper text of the view, which is contained by the preference's
     * dialog.
     *
     * @return The color of the helper text as an {@link Integer} value
     */
    public final int getHelperTextColor() {
        return helperTextColor;
    }

    /**
     * Sets the color of the helper text of the view, which is contained by the preference's
     * dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setHelperTextColor(@ColorInt final int color) {
        this.helperTextColor = color;
    }

    @Override
    public final boolean validate(@NonNull final ValidateableDialog dialog) {
        return validate();
    }

    @Override
    public final Collection<Validator<ValueType>> getValidators() {
        return validators;
    }

    @Override
    public final void addValidator(@NonNull final Validator<ValueType> validator) {
        ensureNotNull(validator, "The validator may not be null");
        this.validators.add(validator);
    }

    @Override
    public final void addAllValidators(@NonNull final Collection<Validator<ValueType>> validators) {
        ensureNotNull(validators, "The collection may not be null");

        for (Validator<ValueType> validator : validators) {
            addValidator(validator);
        }
    }

    @SafeVarargs
    @Override
    public final void addAllValidators(@NonNull final Validator<ValueType>... validators) {
        ensureNotNull(validators, "The array may not be null");

        for (Validator<ValueType> validator : validators) {
            addValidator(validator);
        }
    }

    @Override
    public final void removeValidator(@NonNull final Validator<ValueType> validator) {
        ensureNotNull(validator, "The validator may not be null");
        this.validators.remove(validator);
    }

    @Override
    public final void removeAllValidators(
            @NonNull final Collection<Validator<ValueType>> validators) {
        ensureNotNull(validators, "The collection may not be null");

        for (Validator<ValueType> validator : validators) {
            removeValidator(validator);
        }
    }

    @SafeVarargs
    @Override
    public final void removeAllValidators(@NonNull final Validator<ValueType>... validators) {
        ensureNotNull(validators, "The array may not be null");

        for (Validator<ValueType> validator : validators) {
            removeValidator(validator);
        }
    }

    @Override
    public final void removeAllValidators() {
        this.validators.clear();
    }

    @Override
    public final boolean isValidatedOnValueChange() {
        return validateOnValueChange;
    }

    @Override
    public final void validateOnValueChange(final boolean validateOnValueChange) {
        this.validateOnValueChange = validateOnValueChange;
    }

    @Override
    public final boolean isValidatedOnFocusLost() {
        return validateOnFocusLost;
    }

    @Override
    public final void validateOnFocusLost(final boolean validateOnFocusLost) {
        this.validateOnFocusLost = validateOnFocusLost;
    }

    @Override
    public final void addValidationListener(@NonNull final ValidationListener<ValueType> listener) {
        ensureNotNull(listener, "The listener may not be null");
        this.validationListeners.add(listener);
    }

    @Override
    public final void removeValidationListener(
            @NonNull final ValidationListener<ValueType> listener) {
        ensureNotNull(listener, "The listener may not be null");
        this.validationListeners.remove(listener);
    }

    @CallSuper
    @Override
    protected void onPrepareDialog(@NonNull final MaterialDialog.Builder dialogBuilder) {
        dialogBuilder.addValidator(this);
    }

}