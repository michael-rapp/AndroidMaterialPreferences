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
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * An abstract base class for all preferences, which have two selectable states, persist a boolean
 * value in SharedPreferences and may have dependent preferences, that are enabled/disabled based on
 * the current state.
 *
 * @author Michael Rapp
 * @since 1.4.0
 */
public abstract class AbstractTwoStatePreference extends Preference {

    /**
     * A data structure, which allows to save the internal state of an {@link
     * AbstractTwoStatePreference}.
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
         * The saved value of the attribute "checked".
         */
        public boolean checked;

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * AbstractTwoStatePreference}. This constructor is called by derived classes when saving
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
         * AbstractTwoStatePreference}. This constructor is used when reading from a parcel. It
         * reads the state of the superclass.
         *
         * @param source
         *         The parcel to read read from as a instance of the class {@link Parcel}. The
         *         parcel may not be null
         */
        public SavedState(@NonNull final Parcel source) {
            super(source);
            checked = source.readInt() > 1;
        }

        @Override
        public final void writeToParcel(final Parcel destination, final int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(checked ? 1 : 0);
        }

    }

    /**
     * The summary, which is shown when the preference is checked.
     */
    private CharSequence summaryOn;

    /**
     * The summary, which is shown when the preference is not checked.
     */
    private CharSequence summaryOff;

    /**
     * True, if dependent preferences are disabled when the preference is checked, false, if they
     * are disabled when the preference is not checked.
     */
    private boolean disableDependentsState;

    /**
     * True, if the preference is currently checked, false otherwise.
     */
    boolean checked;

    /**
     * Obtains all attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet) {
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attributeSet, R.styleable.AbstractTwoStatePreference);

        try {
            obtainSummaryOn(typedArray);
            obtainSummaryOff(typedArray);
            obtainDisableDependantsState(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the summary, which should be shown when the preference is checked, from a specific
     * typed array.
     *
     * @param typedArray
     *         The typed array, the summary should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainSummaryOn(@NonNull final TypedArray typedArray) {
        setSummaryOn(typedArray.getText(R.styleable.AbstractTwoStatePreference_android_summaryOn));
    }

    /**
     * Obtains the summary, which should be shown when the preference is not checked, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the summary should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainSummaryOff(@NonNull final TypedArray typedArray) {
        setSummaryOff(
                typedArray.getText(R.styleable.AbstractTwoStatePreference_android_summaryOff));
    }

    /**
     * Obtains the state, when dependent preferences should be disabled, from a specific typed
     * array.
     *
     * @param typedArray
     *         The typed array, the state should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDisableDependantsState(@NonNull final TypedArray typedArray) {
        boolean defaultValue = getContext().getResources()
                .getBoolean(R.bool.two_state_preference_default_disable_dependents_state);
        setDisableDependentsState(typedArray
                .getBoolean(R.styleable.AbstractTwoStatePreference_android_disableDependentsState,
                        defaultValue));
    }

    /**
     * Creates a new preference, which has two selectable states.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public AbstractTwoStatePreference(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new preference, which has two selectable states.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public AbstractTwoStatePreference(@NonNull final Context context,
                                      @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        obtainStyledAttributes(attributeSet);
    }

    /**
     * Creates a new preference, which has two selectable states.
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
    public AbstractTwoStatePreference(@NonNull final Context context,
                                      @Nullable final AttributeSet attributeSet,
                                      final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        obtainStyledAttributes(attributeSet);
    }

    /**
     * Creates a new preference, which has two selectable states.
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
    public AbstractTwoStatePreference(@NonNull final Context context,
                                      @Nullable final AttributeSet attributeSet,
                                      final int defaultStyle, final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        obtainStyledAttributes(attributeSet);
    }

    /**
     * Returns, whether the preference is currently checked, or not.
     *
     * @return True, if the preference is currently checked, false otherwise.
     */
    public final boolean isChecked() {
        return checked;
    }

    /**
     * Sets, whether the preference should currently be checked, or not. When setting a value, it
     * will be persisted.
     *
     * @param checked
     *         True, if the preference should be checked, false otherwise
     */
    public final void setChecked(final boolean checked) {
        boolean hasDisabledDependents = shouldDisableDependents();
        this.checked = checked;
        persistBoolean(checked);
        boolean isDisabelingDependents = shouldDisableDependents();

        if (isDisabelingDependents != hasDisabledDependents) {
            notifyDependencyChange(isDisabelingDependents);
        }

        notifyChanged();
    }

    /**
     * Returns the summary, which is shown when the preference is checked.
     *
     * @return The summary, which is shown when the preference is checked, as an instance of the
     * type {@link CharSequence} or null, if no dedicated summary is shown when the preference is
     * checked
     */
    public final CharSequence getSummaryOn() {
        return summaryOn;
    }

    /**
     * Sets the summary, which should be shown when the preference is checked.
     *
     * @param summaryOn
     *         The summary, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no dedicated summary should be shown when the preference is checked
     */
    public final void setSummaryOn(@Nullable final CharSequence summaryOn) {
        this.summaryOn = summaryOn;
        notifyChanged();
    }

    /**
     * Sets the summary, which should be shown when the preference is checked.
     *
     * @param resourceId
     *         The resource id of the summary, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setSummaryOn(@StringRes final int resourceId) {
        setSummaryOn(getContext().getText(resourceId));
    }

    /**
     * Returns the summary, which is shown when the preference is not checked.
     *
     * @return The summary, which is shown when the preference is not checked, as an instance of the
     * type {@link CharSequence} or null, if no dedicated summary is shown when the preference is
     * not checked
     */
    public final CharSequence getSummaryOff() {
        return summaryOff;
    }

    /**
     * Sets the summary, which should be shown when the preference is not checked.
     *
     * @param resourceId
     *         The resource id of the summary, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setSummaryOff(@StringRes final int resourceId) {
        setSummaryOff(getContext().getText(resourceId));
        notifyChanged();
    }

    /**
     * Sets the summary, which should be shown when the preference is not checked.
     *
     * @param summaryOff
     *         The summary, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no dedicated summary should be shown when the preference is not checked
     */
    public final void setSummaryOff(@Nullable final CharSequence summaryOff) {
        this.summaryOff = summaryOff;
    }

    /**
     * Returns, whether dependent preferences are disabled when this preference is checked, or when
     * this preference is not checked.
     *
     * @return True, if dependent preferences are disabled when this preference checked, false, if
     * they are disabled when this preference is not checked
     */
    public final boolean getDisableDependentsState() {
        return disableDependentsState;
    }

    /**
     * Sets, whether dependent preferences should be disabled when this preference is checked, or
     * when this preference is not checked.
     *
     * @param disableDependentsState
     *         True, if dependent preferences should be disabled when this preference is checked,
     *         false, if they should be disabled when this preference is not checked
     */
    public final void setDisableDependentsState(final boolean disableDependentsState) {
        this.disableDependentsState = disableDependentsState;
        notifyChanged();
    }

    @Override
    public final boolean shouldDisableDependents() {
        return getDisableDependentsState() ? isChecked() :
                !isChecked() || super.shouldDisableDependents();
    }

    @Override
    public final CharSequence getSummary() {
        if (isChecked() && !TextUtils.isEmpty(getSummaryOn())) {
            return getSummaryOn();
        } else if (!isChecked() && !TextUtils.isEmpty(getSummaryOff())) {
            return getSummaryOff();
        }

        return super.getSummary();
    }

    @Override
    protected final Object onGetDefaultValue(final TypedArray typedArray, final int index) {
        return typedArray.getBoolean(index, false);
    }

    @Override
    protected final void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
        setChecked(restoreValue ? getPersistedBoolean(isChecked()) : (Boolean) defaultValue);
    }

    @Override
    protected final void onClick() {
        super.onClick();
        boolean newValue = !isChecked();

        if (callChangeListener(newValue)) {
            setChecked(newValue);
        }
    }

    @Override
    protected final Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        if (!isPersistent()) {
            SavedState savedState = new SavedState(superState);
            savedState.checked = isChecked();
            return savedState;
        }

        return superState;
    }

    @Override
    protected final void onRestoreInstanceState(final Parcelable state) {
        if (state != null && state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            setChecked(savedState.checked);
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}