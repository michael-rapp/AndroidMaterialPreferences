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
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import de.mrapp.android.dialog.MaterialDialogBuilder;
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
public abstract class AbstractValidateableDialogPreference<ValueType>
        extends AbstractDialogPreference
        implements de.mrapp.android.dialog.MaterialDialogBuilder.Validator,
        Validateable<ValueType> {

    /**
     * A set, which contains the validators, which are used to validate the view, which is contained
     * by the preference's dialog.
     */
    private transient Set<Validator<ValueType>> validators;

    /**
     * A set, which contains the listeners, which are notified when the view, which is contained by
     * the preference's dialog, has been validated.
     */
    private transient Set<ValidationListener<ValueType>> validationListeners;

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
     */
    private void initialize(final AttributeSet attributeSet) {
        validators = new LinkedHashSet<>();
        validationListeners = new LinkedHashSet<>();
        obtainStyledAttributes(attributeSet);
    }

    /**
     * Obtains all attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet) {
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attributeSet, R.styleable.AbstractValidateableView);
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
    @SuppressWarnings("deprecation")
    private void obtainHelperTextColor(@NonNull final TypedArray typedArray) {
        setHelperTextColor(typedArray.getColor(R.styleable.AbstractValidateableView_helperTextColor,
                getContext().getResources().getColor(R.color.default_helper_text_color)));
    }

    /**
     * Obtains the color, which is used to indicate validation errors, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the error color should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    @SuppressWarnings("deprecation")
    private void obtainErrorColor(@NonNull final TypedArray typedArray) {
        setErrorColor(typedArray.getColor(R.styleable.AbstractValidateableView_errorColor,
                getContext().getResources().getColor(R.color.default_error_color)));
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
     * The method, which is invoked on subclasses when the preference's dialog is about to be
     * created. The builder, which is passed as a method parameter may be manipulated by subclasses
     * in order to change the appearance of the dialog.
     *
     * @param dialogBuilder
     *         The builder, which is used to create the preference's dialog, as an instance of the
     *         class MaterialDialogBuilder
     */
    protected abstract void onPrepareValidateableDialog(
            @NonNull final MaterialDialogBuilder dialogBuilder);

    /**
     * Returns the listeners, which are notified when the view, which is contained by the
     * preference's dialog, has been validated.
     *
     * @return A set, which contains the listeners, which are notified when the view, which is
     * contained by the preference's dialog, has been validated, as an instance of the type {@link
     * Set} or an empty set, if no validators are used
     */
    protected final Set<ValidationListener<ValueType>> getValidationListeners() {
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
        this(context, attributeSet, android.R.attr.dialogPreferenceStyle);
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
                                                final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet);
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
                                                final int defaultStyle,
                                                final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet);
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

    @Override
    protected final void onPrepareDialog(@NonNull final MaterialDialogBuilder dialogBuilder) {
        dialogBuilder.addValidator(this);
        onPrepareValidateableDialog(dialogBuilder);
    }

}