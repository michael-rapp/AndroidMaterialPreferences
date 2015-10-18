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
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.mrapp.android.dialog.MaterialDialogBuilder;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A preference, which allows to select multiple values from a list. The selected values are
 * persisted as a {@link Set} in the shared preferences. They will only be persisted, if confirmed
 * by the user.
 *
 * @author Michael Rapp
 * @since 1.7.0
 */
public class MultiChoiceListPreference extends AbstractListPreference {

    /**
     * A data structure, which allows to save the internal state of a {@link
     * MultiChoiceListPreference}.
     */
    public static class SavedState extends BaseSavedState {

        /**
         * A creator, which allows to create instances of the class {@link
         * MultiChoiceListPreference} from parcels.
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
         * The saved value of the attribute "values".
         */
        public Set<String> values;

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * MultiChoiceListPreference}. This constructor is called by derived classes when saving
         * their states.
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
         * MultiChoiceListPreference}. This constructor is used when reading from a parcel. It reads
         * the state of the superclass.
         *
         * @param source
         *         The parcel to read read from as a instance of the class {@link Parcel}. The
         *         parcel may not be null
         */
        public SavedState(@NonNull final Parcel source) {
            super(source);
            List<String> list = new ArrayList<>();
            source.readStringList(list);
            values = new HashSet<>(list);
        }

        @Override
        public final void writeToParcel(final Parcel destination, final int flags) {
            super.writeToParcel(destination, flags);
            destination.writeStringList(new ArrayList<>(values));
        }

    }

    /**
     * The currently persisted values of the preference.
     */
    private Set<String> values;

    /**
     * A set, which contains the indices of the currently selected list items.
     */
    private Set<Integer> selectedIndices;

    /**
     * Initializes the preference.
     */
    private void initialize() {
        selectedIndices = null;
        setNegativeButtonText(android.R.string.cancel);
        setPositiveButtonText(android.R.string.ok);
    }

    /**
     * Loads and returns the currently persisted set, which belongs to the preference's key, from
     * the shared preferences.
     *
     * @param defaultValue
     *         The default value, which should be returned, if no set with the preference's key is
     *         currently persisted, as an instance of the type {@link Set}
     * @return The currently persisted set or the given default value as an instance of the type
     * {@link Set}
     */
    private Set<String> getPersistedSet(@Nullable final Set<String> defaultValue) {
        if (!shouldPersist()) {
            return defaultValue;
        }

        return getPreferenceManager().getSharedPreferences().getStringSet(getKey(), defaultValue);
    }

    /**
     * Return the indices of the entries, which correspond to specific values.
     *
     * @param values
     *         A set, which contains the values of the entries, whose indices should be returned, as
     *         an instance of the type {@link Set}
     * @return A list, which contains the indices of the entries, the given values correspond to, as
     * an instance of the type {@link List}
     */
    private List<Integer> indicesOf(@Nullable final Set<String> values) {
        List<Integer> indices = new ArrayList<>();

        if (values != null && getEntryValues() != null) {
            for (String value : values) {
                int index = indexOf(value);

                if (index >= 0) {
                    indices.add(index);
                }
            }
        }

        return indices;
    }

    /**
     * Persists a specific set in the shared preferences by using the preference's key.
     *
     * @param set
     *         The set, which should be persisted, as an instance of the type {@link Set}
     * @return True, if the given set has been persisted, false otherwise
     */
    private boolean persistSet(@Nullable final Set<String> set) {
        if (set != null && shouldPersist()) {
            if (set.equals(getPersistedSet(null))) {
                return true;
            }

            Editor editor = getPreferenceManager().getSharedPreferences().edit();
            editor.putStringSet(getKey(), set);
            editor.apply();
            return true;
        }

        return false;
    }

    /**
     * Creates and returns a listener, which allows to observe when list items are selected or
     * unselected by the user.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnMultiChoiceClickListener}
     */
    private OnMultiChoiceClickListener createListItemListener() {
        return new OnMultiChoiceClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which,
                                final boolean isChecked) {
                if (isChecked) {
                    selectedIndices.add(which);
                } else {
                    selectedIndices.remove(which);
                }
            }

        };
    }

    /**
     * Creates a new preference, which allows to select multiple values from a list.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public MultiChoiceListPreference(@NonNull final Context context) {
        super(context);
        initialize();
    }

    /**
     * Creates a new preference, which allows to select multiple values from a list.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public MultiChoiceListPreference(@NonNull final Context context,
                                     @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize();
    }

    /**
     * Creates a new preference, which allows to select multiple values from a list.
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
    public MultiChoiceListPreference(@NonNull final Context context,
                                     @Nullable final AttributeSet attributeSet,
                                     final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize();
    }

    /**
     * Creates a new preference, which allows to select multiple values from a list.
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
    public MultiChoiceListPreference(@NonNull final Context context,
                                     @Nullable final AttributeSet attributeSet,
                                     final int defaultStyle, final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize();
    }

    /**
     * Returns the currently persisted values of the preference.
     *
     * @return A set, which contains the currently persisted values of the preference, as an
     * instance of the type {@link Set}
     */
    public final Set<String> getValues() {
        return values;
    }

    /**
     * Sets the current values of the preference. By setting values, they will be persisted.
     *
     * @param values
     *         A set, which contains the values, which should be set, as an instance of the type
     *         {@link Set}
     */
    public final void setValues(@Nullable final Set<String> values) {
        if (values != null && !values.equals(this.values)) {
            this.values = values;
            persistSet(this.values);
            notifyChanged();
        }
    }

    /**
     * Adds a new value to the preference. By adding a value, the changes will be persisted.
     *
     * @param value
     *         The value, which should be added, as a {@link String}. The value may not be null
     */
    public final void addValue(@NonNull final String value) {
        ensureNotNull(value, "The value may not be null");

        if (this.values != null) {
            if (this.values.add(value)) {
                persistSet(this.values);
                notifyChanged();
            }
        } else {
            Set<String> newValues = new HashSet<>();
            newValues.add(value);
            setValues(newValues);
        }
    }

    /**
     * Removes a specific value from the preference. By removing a value, the changes will be
     * persisted.
     *
     * @param value
     *         The value, which should be removed, as a {@link String}. The value may not be null
     */
    public final void removeValue(@NonNull final String value) {
        ensureNotNull(value, "The value may not be null");

        if (this.values != null) {
            if (this.values.remove(value)) {
                persistSet(this.values);
                notifyChanged();
            }
        }
    }

    /**
     * Adds all values, which are contained by a specific collection, to the preference. By adding
     * values, the changes will be persisted.
     *
     * @param values
     *         A collection, which contains the values, which should be added, as an instance of the
     *         type {@link Collection} or an empty collection, if no values should be added
     */
    public final void addAllValues(@NonNull final Collection<String> values) {
        ensureNotNull(values, "The values may not be null");

        if (this.values != null) {
            if (this.values.addAll(values)) {
                persistSet(this.values);
                notifyChanged();
            }
        } else {
            Set<String> newValues = new HashSet<>();
            newValues.addAll(values);
            setValues(newValues);
        }
    }

    /**
     * Removes all values, which are contained by a specific collection, from the preference. By
     * removing values, the changes will be persisted.
     *
     * @param values
     *         A collection, which contains the values, which should be removed, as an instance of
     *         the type {@link Collection} or an empty collection, if no values should be removed
     */
    public final void removeAllValues(@NonNull final Collection<String> values) {
        ensureNotNull(values, "The values may not be null");

        if (this.values != null) {
            if (this.values.removeAll(values)) {
                persistSet(this.values);
                notifyChanged();
            }
        }
    }

    /**
     * Returns the entries, the currently persisted values of the preference belong to.
     *
     * @return An array, which contains the entries, the currently persisted values of the
     * preference belong to, as an array of the type {@link CharSequence}
     */
    public final CharSequence[] getSelectedEntries() {
        List<Integer> indices = indicesOf(values);
        Collections.sort(indices);

        if (!indices.isEmpty()) {
            CharSequence[] selectedEntries = new CharSequence[indices.size()];
            int currentIndex = 0;

            for (int index : indices) {
                selectedEntries[currentIndex] = getEntries()[index];
                currentIndex++;
            }

            return selectedEntries;
        }

        return null;
    }

    @Override
    public final CharSequence getSummary() {
        if (isValueShownAsSummary()) {
            CharSequence[] entries = getSelectedEntries();

            if (entries != null) {
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < entries.length; i++) {
                    if (i > 0) {
                        stringBuilder.append(", ");
                    }

                    stringBuilder.append(entries[i]);
                }

                return stringBuilder.toString();
            }

            return null;
        } else {
            return super.getSummary();
        }
    }

    @Override
    protected final Object onGetDefaultValue(final TypedArray typedArray, final int index) {
        CharSequence[] defaultValues = typedArray.getTextArray(index);

        if (defaultValues != null) {
            Set<String> defaultValue = new HashSet<>();

            for (CharSequence value : defaultValues) {
                defaultValue.add(value.toString());
            }

            return defaultValue;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
        setValues(restoreValue ? getPersistedSet(getValues()) : (Set<String>) defaultValue);
    }

    @Override
    protected final void onPrepareDialog(@NonNull final MaterialDialogBuilder dialogBuilder) {
        selectedIndices = new HashSet<Integer>(indicesOf(values));
        boolean[] checkedItems = new boolean[getEntryValues().length];

        for (int selectedIndex : selectedIndices) {
            checkedItems[selectedIndex] = true;
        }

        dialogBuilder.setMultiChoiceItems(getEntries(), checkedItems, createListItemListener());
        dialogBuilder.setItemColor(getDialogItemColor());
        dialogBuilder.setItemControlColor(getDialogItemColor());
    }

    @Override
    protected final void onDialogClosed(final boolean positiveResult) {
        if (positiveResult && selectedIndices != null && getEntryValues() != null) {
            Set<String> newValues = new HashSet<>();

            for (int selectedIndex : selectedIndices) {
                newValues.add(getEntryValues()[selectedIndex].toString());
            }

            if (callChangeListener(newValues)) {
                setValues(newValues);
            }
        }

        selectedIndices = null;
    }

    @Override
    protected final Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        if (!isPersistent()) {
            SavedState savedState = new SavedState(superState);
            savedState.values = getValues();
            return savedState;
        }

        return superState;
    }

    @Override
    protected final void onRestoreInstanceState(final Parcelable state) {
        if (state != null && state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            setValues(savedState.values);
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}