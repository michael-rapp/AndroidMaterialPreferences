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
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import de.mrapp.android.dialog.MaterialDialogBuilder;

/**
 * A preference, which allows to select a value from a list. The selected value will immediately
 * persisted and the preference's dialog will become closed.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class ListPreference extends AbstractListPreference {

    /**
     * A data structure, which allows to save the internal state of a {@link ListPreference}.
     */
    public static class SavedState extends BaseSavedState {

        /**
         * A creator, which allows to create instances of the class {@link ListPreference} from
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
         * The saved value of the attribute "value".
         */
        public String value;

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * ListPreference}. This constructor is called by derived classes when saving their states.
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
         * ListPreference}. This constructor is used when reading from a parcel. It reads the state
         * of the superclass.
         *
         * @param source
         *         The parcel to read read from as a instance of the class {@link Parcel}. The
         *         parcel may not be null
         */
        public SavedState(@NonNull final Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public final void writeToParcel(final Parcel destination, final int flags) {
            super.writeToParcel(destination, flags);
            destination.writeString(value);
        }

    }

    /**
     * The currently persisted value of the preference.
     */
    private String value;

    /**
     * The index of the currently selected list item.
     */
    private int selectedIndex;

    /**
     * Initializes the preference.
     */
    private void initialize() {
        selectedIndex = -1;
        setNegativeButtonText(android.R.string.cancel);
    }

    /**
     * Creates and returns a listener, which allows to persist the value a list item, which is
     * clicked by the user.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnClickListener}
     */
    private OnClickListener createListItemListener() {
        return new OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                selectedIndex = which;
                ListPreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                dialog.dismiss();
            }
        };
    }

    /**
     * Creates a new preference, which allows to select a value from a list.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public ListPreference(@NonNull final Context context) {
        super(context);
        initialize();
    }

    /**
     * Creates a new preference, which allows to select a value from a list.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public ListPreference(@NonNull final Context context,
                          @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize();
    }

    /**
     * Creates a new preference, which allows to select a value from a list.
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
    public ListPreference(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                          final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize();
    }

    /**
     * Creates a new preference, which allows to select a value from a list.
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
    public ListPreference(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                          final int defaultStyle, final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize();
    }

    /**
     * Returns the currently persisted value of the preference.
     *
     * @return The currently persisted value of the preference as a {@link String}
     */
    public final String getValue() {
        return value;
    }

    /**
     * Sets the current value of the preference. By setting a value, it will be persisted.
     *
     * @param value
     *         The value, which should be set, as a {@link String}
     */
    public final void setValue(@Nullable final String value) {
        if (!TextUtils.equals(this.value, value)) {
            this.value = value;
            persistString(value);
            notifyChanged();
        }
    }

    /**
     * Sets the current value of the preference. By setting a value, it will be persisted.
     *
     * @param index
     *         The index of the value, which should be set, as an {@link Integer} value
     */
    public final void setValueIndex(final int index) {
        if (getEntryValues() != null) {
            setValue(getEntryValues()[index].toString());
        }
    }

    /**
     * Returns the entry, the currently persisted value of the preference belongs to.
     *
     * @return The entry, the currently persisted value of the preference belongs to, as an instance
     * of the type {@link CharSequence}
     */
    public final CharSequence getEntry() {
        int index = indexOf(value);
        return index >= 0 && getEntries() != null ? getEntries()[index] : null;
    }

    @Override
    public final CharSequence getSummary() {
        if (isValueShownAsSummary()) {
            return getEntry();
        } else {
            return super.getSummary();
        }
    }

    @Override
    protected final Object onGetDefaultValue(final TypedArray typedArray, final int index) {
        return typedArray.getString(index);
    }

    @Override
    protected final void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
        setValue(restoreValue ? getPersistedString(getValue()) : (String) defaultValue);
    }

    @Override
    protected final void onPrepareDialog(@NonNull final MaterialDialogBuilder dialogBuilder) {
        selectedIndex = indexOf(getValue());
        dialogBuilder.setSingleChoiceItems(getEntries(), selectedIndex, createListItemListener());
        dialogBuilder.setItemColor(getDialogItemColor());
        dialogBuilder.setItemControlColor(getDialogItemColor());
    }

    @Override
    protected final void onDialogClosed(final boolean positiveResult) {
        if (positiveResult && selectedIndex >= 0 && getEntryValues() != null) {
            String newValue = getEntryValues()[selectedIndex].toString();

            if (callChangeListener(newValue)) {
                setValue(newValue);
            }
        }

        selectedIndex = -1;
    }

    @Override
    protected final Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        if (!isPersistent()) {
            SavedState savedState = new SavedState(superState);
            savedState.value = getValue();
            return savedState;
        }

        return superState;
    }

    @Override
    protected final void onRestoreInstanceState(final Parcelable state) {
        if (state != null && state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            setValue(savedState.value);
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}