/*
 * AndroidMaterialPreferences Copyright 2014 - 2015 Michael Rapp
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

import static de.mrapp.android.preference.util.Condition.ensureNotNull;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;

/**
 * An abstract base class for all preferences, which provide a list of
 * selectable items.
 * 
 * @author Michael Rapp
 */
public abstract class AbstractListPreference extends AbstractDialogPreference {

	/**
	 * The color of the items of the preference's dialog.
	 */
	private int dialogItemColor;

	/**
	 * The color of the item controls of the preference's dialog.
	 */
	private int dialogItemControlColor;

	/**
	 * An array, which contains the entries, which are shown in the list.
	 */
	private CharSequence[] entries;

	/**
	 * An array, which contains the values, which correspond to the entries,
	 * which are shown in the list.
	 */
	private CharSequence[] entryValues;

	/**
	 * Initializes the list preference.
	 * 
	 * @param attributeSet
	 *            The attribute set, which should be used to initialize the list
	 *            preferences, as an instance of the type {@link AttributeSet}
	 */
	private void initialize(final AttributeSet attributeSet) {
		entries = new CharSequence[0];
		entryValues = new CharSequence[0];
		obtainStyledAttributes(attributeSet);
	}

	/**
	 * Obtains all attributes from a specific attribute set.
	 * 
	 * @param attributeSet
	 *            The attribute set, the attributes should be obtained from, as
	 *            an instance of the type {@link AttributeSet}
	 */
	private void obtainStyledAttributes(final AttributeSet attributeSet) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.AbstractListPreference);

		try {
			obtainDialogItemColor(typedArray);
			obtainDialogItemControlColor(typedArray);
			obtainEntries(typedArray);
			obtainEntryValues(typedArray);
		} finally {
			typedArray.recycle();
		}
	}

	/**
	 * Obtains the item color of the preference's dialog from a specific typed
	 * array.
	 * 
	 * @param typedArray
	 *            The typed array, the item color should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainDialogItemColor(final TypedArray typedArray) {
		setDialogItemColor(typedArray.getColor(R.styleable.AbstractListPreference_dialogItemColor, -1));
	}

	/**
	 * Obtains the item control color of the preference's dialog from a specific
	 * typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the item control color should be obtained
	 *            from, as an instance of the class {@link TypedArray}
	 */
	private void obtainDialogItemControlColor(final TypedArray typedArray) {
		setDialogItemControlColor(typedArray.getColor(R.styleable.AbstractListPreference_dialogItemControlColor, -1));
	}

	/**
	 * Obtains the the entries of the list preference from a specific typed
	 * array.
	 * 
	 * @param typedArray
	 *            The typed array, the entries should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainEntries(final TypedArray typedArray) {
		CharSequence[] obtainedEntries = typedArray.getTextArray(R.styleable.AbstractListPreference_android_entries);

		if (obtainedEntries != null) {
			setEntries(obtainedEntries);
		}
	}

	/**
	 * Obtains the the values, which correspond to the entries of the list
	 * preference from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the entry values should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainEntryValues(final TypedArray typedArray) {
		CharSequence[] obtainedEntryValues = typedArray
				.getTextArray(R.styleable.AbstractListPreference_android_entryValues);

		if (obtainedEntryValues != null) {
			setEntryValues(obtainedEntryValues);
		}
	}

	/**
	 * Creates a new preference, which provides a list of selectable items.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 */
	public AbstractListPreference(final Context context) {
		super(context);
		initialize(null);
	}

	/**
	 * Creates a new preference, which provides a list of selectable items.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the
	 *            preference, as an instance of the type {@link AttributeSet}
	 */
	public AbstractListPreference(final Context context, final AttributeSet attributeSet) {
		super(context, attributeSet);
		initialize(attributeSet);
	}

	/**
	 * Creates a new preference, which provides a list of selectable items.
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
	public AbstractListPreference(final Context context, final AttributeSet attributeSet, final int defaultStyle) {
		super(context, attributeSet, defaultStyle);
		initialize(attributeSet);
	}

	/**
	 * Creates a new preference, which provides a list of selectable items.
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
	public AbstractListPreference(final Context context, final AttributeSet attributeSet, final int defaultStyle,
			final int defaultStyleResource) {
		super(context, attributeSet, defaultStyle, defaultStyleResource);
		initialize(attributeSet);
	}

	/**
	 * Returns the color of the items of the preference's dialog.
	 * 
	 * @return The color of the items as an {@link Integer} value or -1, if no
	 *         custom item color is set
	 */
	public final int getDialogItemColor() {
		return dialogItemColor;
	}

	/**
	 * Sets the color of the items of the preference's dialog.
	 * 
	 * @param color
	 *            The color, which should be set, as an {@link Integer} value or
	 *            -1, if no custom item color should be set
	 */
	public final void setDialogItemColor(final int color) {
		this.dialogItemColor = color;
	}

	/**
	 * Returns the color of the item controls of the preference's dialog.
	 * 
	 * @return The color of the item controls as an {@link Integer} value or -1,
	 *         if no custom item color is set
	 */
	public final int getDialogItemControlColor() {
		return dialogItemControlColor;
	}

	/**
	 * Sets the color of the item controls of the preference's dialog.
	 * 
	 * @param color
	 *            The color, which should be set, as an {@link Integer} value or
	 *            -1, if no custom item control color should be set
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public final void setDialogItemControlColor(final int color) {
		this.dialogItemControlColor = color;
	}

	/**
	 * Returns the entries of the list, which is shown by the preference.
	 * 
	 * @return The entries of the list, which is shown by the preference, as a
	 *         {@link CharSequence} array or null, if no entries have been set
	 */
	public final CharSequence[] getEntries() {
		return entries;
	}

	/**
	 * Sets the entries of the list, which is shown by the preference.
	 * 
	 * @param entries
	 *            The entries, which should be set, as an {@link CharSequence}
	 *            array. The entries may not be null
	 */
	public final void setEntries(final CharSequence[] entries) {
		ensureNotNull(entries, "The entries may not be null");
		this.entries = entries;
	}

	/**
	 * Sets the entries of the list, which is shown by the preference.
	 * 
	 * @param resourceId
	 *            The resource id of the entries, which should be set, as an
	 *            {@link Integer} value. The resource id must correspond to a
	 *            valid array resource
	 */
	public final void setEntries(final int resourceId) {
		setEntries(getContext().getResources().getTextArray(resourceId));
	}

	/**
	 * Returns the values, which correspond to the entries of the list, which is
	 * shown by the preference.
	 * 
	 * @return The values, which correspond to the entries of the list, which is
	 *         shown by the preference, as a {@link CharSequence} array
	 */
	public final CharSequence[] getEntryValues() {
		return entryValues;
	}

	/**
	 * Sets the values, which correspond to the entries of the list, which is
	 * shown by the preference.
	 * 
	 * @param entryValues
	 *            The values, which should be set, as a {@link CharSequence}
	 *            array. The values may not be null and the array's length must
	 *            be equal to the number of list items
	 */
	public final void setEntryValues(final CharSequence[] entryValues) {
		ensureNotNull(entryValues, "The entry values may not be null");
		this.entryValues = entryValues;
	}

	/**
	 * Sets the values, which correspond to the entries of the list, which is
	 * shown by the preference.
	 * 
	 * @param resourceId
	 *            The resource id of the values, which should be set, as an
	 *            {@link Integer} value. The resource id must correspond to a
	 *            valid array resource. The array's length must be equal to the
	 *            number of list items
	 */
	public final void setEntryValues(final int resourceId) {
		setEntryValues(getContext().getResources().getTextArray(resourceId));
	}

	@Override
	protected final boolean needInputMethod() {
		return false;
	}

}