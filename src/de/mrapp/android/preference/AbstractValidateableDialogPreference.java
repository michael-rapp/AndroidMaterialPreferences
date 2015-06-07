package de.mrapp.android.preference;

import java.util.Collection;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import de.mrapp.android.dialog.MaterialDialogBuilder;
import de.mrapp.android.validation.AbstractValidateableView;
import de.mrapp.android.validation.Validateable;
import de.mrapp.android.validation.ValidationListener;
import de.mrapp.android.validation.Validator;

/**
 * An abstract base class for all dialog preferences, which can be validated.
 * 
 * @param <ViewType>
 *            The type of the view, which is contained by the preference's
 *            dialog
 * @param <ValueType>
 *            The type of the values, which can be validated
 * 
 * @author Michael Rapp
 * 
 * @since 1.2.0
 */
public abstract class AbstractValidateableDialogPreference<ViewType extends AbstractValidateableView<?, ValueType>, ValueType>
		extends AbstractDialogPreference implements
		de.mrapp.android.dialog.MaterialDialogBuilder.Validator,
		Validateable<ValueType> {

	/**
	 * The view, which is contained by the preference's dialog.
	 */
	private ViewType view;

	/**
	 * The method, which is invoked on subclasses when the preference's dialog
	 * is about to be created. This method must be overridden by subclasses in
	 * order to return the view, which should be contained by the preference's
	 * dialog.
	 * 
	 * @return The view, which should be contained by the preferences dialog, as
	 *         an instance of the generic type ViewType. The view may not be
	 *         null
	 */
	protected abstract ViewType onCreateView();

	/**
	 * Returns the view, which is contained by the preference's dialog.
	 * 
	 * @return The view, which is contained by the preference's dialog, as an
	 *         instance of the generic type ViewType. The view may not be null
	 */
	protected final ViewType getView() {
		return view;
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
	}

	@Override
	public final boolean validate() {
		return view.validate();
	}

	@Override
	public final Collection<Validator<ValueType>> getValidators() {
		return view.getValidators();
	}

	@Override
	public final void addValidator(final Validator<ValueType> validator) {
		view.addValidator(validator);
	}

	@Override
	public final void addAllValidators(
			final Collection<Validator<ValueType>> validators) {
		view.addAllValidators(validators);
	}

	@SafeVarargs
	@Override
	public final void addAllValidators(final Validator<ValueType>... validators) {
		view.addAllValidators(validators);
	}

	@Override
	public final void removeValidator(final Validator<ValueType> validator) {
		view.removeValidator(validator);
	}

	@Override
	public final void removeAllValidators(
			final Collection<Validator<ValueType>> validators) {
		view.removeAllValidators(validators);
	}

	@SafeVarargs
	@Override
	public final void removeAllValidators(
			final Validator<ValueType>... validators) {
		view.removeAllValidators(validators);
	}

	@Override
	public final void removeAllValidators() {
		view.removeAllValidators();
	}

	@Override
	public final boolean isValidatedOnValueChange() {
		return view.isValidatedOnValueChange();
	}

	@Override
	public final void validateOnValueChange(final boolean validateOnValueChange) {
		view.validateOnValueChange(validateOnValueChange);
	}

	@Override
	public final boolean isValidatedOnFocusLost() {
		return view.isValidatedOnFocusLost();
	}

	@Override
	public final void validateOnFocusLost(final boolean validateOnFocusLost) {
		view.validateOnFocusLost(validateOnFocusLost);
	}

	@Override
	public final void addValidationListener(
			final ValidationListener<ValueType> listener) {
		view.addValidationListener(listener);
	}

	@Override
	public final void removeValidationListener(
			final ValidationListener<ValueType> listener) {
		view.removeValidationListener(listener);
	}

	@Override
	protected final void onPrepareDialog(
			final MaterialDialogBuilder dialogBuilder) {
		dialogBuilder.addValidator(this);
		view = onCreateView();
		dialogBuilder.setView(view);
	}

}