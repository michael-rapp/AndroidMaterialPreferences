package de.mrapp.android.preference;

import static de.mrapp.android.preference.util.Condition.ensureNotNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import de.mrapp.android.dialog.MaterialDialogBuilder;
import de.mrapp.android.validation.Validateable;
import de.mrapp.android.validation.ValidationListener;
import de.mrapp.android.validation.Validator;

/**
 * An abstract base class for all dialog preferences, which can be validated.
 * 
 * @param <ValueType>
 *            The type of the values, which can be validated
 * 
 * @author Michael Rapp
 * 
 * @since 1.2.0
 */
public abstract class AbstractValidateableDialogPreference<ValueType> extends
		AbstractDialogPreference implements
		de.mrapp.android.dialog.MaterialDialogBuilder.Validator,
		Validateable<ValueType> {

	/**
	 * A set, which contains the validators, which are used to validate the
	 * view, which is contained by the preference's dialog.
	 */
	private transient Set<Validator<ValueType>> validators;

	/**
	 * A set, which contains the listeners, which are notified when the view,
	 * which is contained by the preference's dialog, has been validated.
	 */
	private transient Set<ValidationListener<ValueType>> validationListeners;

	/**
	 * True, if the view, which is contained by the preference's dialog should
	 * be automatically validated, when its value has been changed, false
	 * otherwise.
	 */
	private boolean validateOnValueChange;

	/**
	 * True, if the view, which is contained by the preference's dialog should
	 * be automatically validated, when it has lost its focus, false otherwise.
	 */
	private boolean validateOnFocusLost;

	/**
	 * Initializes the preference.
	 */
	private void initialize() {
		validators = new LinkedHashSet<>();
		validationListeners = new LinkedHashSet<>();
		validateOnValueChange = true;
		validateOnFocusLost = true;
	}

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
	protected abstract void onPrepareValidateableDialog(
			final MaterialDialogBuilder dialogBuilder);

	/**
	 * Returns the listeners, which are notified when the view, which is
	 * contained by the preference's dialog, has been validated.
	 * 
	 * @return A set, which contains the listeners, which are notified when the
	 *         view, which is contained by the preference's dialog, has been
	 *         validated, as an instance of the type {@link Set} or an empty
	 *         set, if no validators are used
	 */
	protected final Set<ValidationListener<ValueType>> getValidationListeners() {
		return validationListeners;
	}

	/**
	 * Creates a new dialog preference, which can be validated.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 */
	public AbstractValidateableDialogPreference(final Context context) {
		this(context, null);
	}

	/**
	 * Creates a new dialog preference, which can be validated.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the
	 *            preference, as an instance of the type {@link AttributeSet}
	 */
	public AbstractValidateableDialogPreference(final Context context,
			final AttributeSet attributeSet) {
		this(context, attributeSet, android.R.attr.dialogPreferenceStyle);
	}

	/**
	 * Creates a new dialog preference, which can be validated.
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
	public AbstractValidateableDialogPreference(final Context context,
			final AttributeSet attributeSet, final int defaultStyle) {
		super(context, attributeSet, defaultStyle);
		initialize();
	}

	/**
	 * Creates a new dialog preference, which can be validated.
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
	public AbstractValidateableDialogPreference(final Context context,
			final AttributeSet attributeSet, final int defaultStyle,
			final int defaultStyleResource) {
		super(context, attributeSet, defaultStyle, defaultStyleResource);
		initialize();
	}

	@Override
	public final Collection<Validator<ValueType>> getValidators() {
		return validators;
	}

	@Override
	public final void addValidator(final Validator<ValueType> validator) {
		ensureNotNull(validator, "The validator may not be null");
		this.validators.add(validator);
	}

	@Override
	public final void addAllValidators(
			final Collection<Validator<ValueType>> validators) {
		ensureNotNull(validators, "The collection may not be null");

		for (Validator<ValueType> validator : validators) {
			addValidator(validator);
		}
	}

	@SafeVarargs
	@Override
	public final void addAllValidators(final Validator<ValueType>... validators) {
		ensureNotNull(validators, "The array may not be null");

		for (Validator<ValueType> validator : validators) {
			addValidator(validator);
		}
	}

	@Override
	public final void removeValidator(final Validator<ValueType> validator) {
		ensureNotNull(validator, "The validator may not be null");
		this.validators.remove(validator);
	}

	@Override
	public final void removeAllValidators(
			final Collection<Validator<ValueType>> validators) {
		ensureNotNull(validators, "The collection may not be null");

		for (Validator<ValueType> validator : validators) {
			removeValidator(validator);
		}
	}

	@SafeVarargs
	@Override
	public final void removeAllValidators(
			final Validator<ValueType>... validators) {
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
	public final void addValidationListener(
			final ValidationListener<ValueType> listener) {
		ensureNotNull(listener, "The listener may not be null");
		this.validationListeners.add(listener);
	}

	@Override
	public final void removeValidationListener(
			final ValidationListener<ValueType> listener) {
		ensureNotNull(listener, "The listener may not be null");
		this.validationListeners.remove(listener);
	}

	@Override
	protected final void onPrepareDialog(
			final MaterialDialogBuilder dialogBuilder) {
		dialogBuilder.addValidator(this);
		onPrepareValidateableDialog(dialogBuilder);
	}

}