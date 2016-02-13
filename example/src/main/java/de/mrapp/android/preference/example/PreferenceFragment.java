/*
 * AndroidMaterialPreferences Copyright 2014 - 2016 Michael Rapp
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
package de.mrapp.android.preference.example;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;

import de.mrapp.android.preference.ColorPalettePreference;
import de.mrapp.android.preference.DigitPickerPreference;
import de.mrapp.android.preference.EditTextPreference;
import de.mrapp.android.preference.ListPreference;
import de.mrapp.android.preference.MultiChoiceListPreference;
import de.mrapp.android.preference.NumberPickerPreference;
import de.mrapp.android.preference.ResolutionPreference;
import de.mrapp.android.preference.SeekBarPreference;
import de.mrapp.android.preference.SwitchPreference;
import de.mrapp.android.validation.Validators;

/**
 * A preference fragment, which contains the example app's settings.
 *
 * @author Michael Rapp
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {

    /**
     * The {@link EditTextPreference}.
     */
    private EditTextPreference editTextPreference;

    /**
     * The {@link ListPreference}.
     */
    private ListPreference listPreference;

    /**
     * The {@link MultiChoiceListPreference}.
     */
    private MultiChoiceListPreference multiChoiceListPreference;

    /**
     * The {@link SeekBarPreference}.
     */
    private SeekBarPreference seekBarPreference;

    /**
     * The {@link NumberPickerPreference}.
     */
    private NumberPickerPreference numberPickerPreference;

    /**
     * The {@link DigitPickerPreference}.
     */
    private DigitPickerPreference digitPickerPreference;

    /**
     * The {@link ResolutionPreference}.
     */
    private ResolutionPreference resolutionPreference;

    /**
     * The {@link ColorPalettePreference}.
     */
    private ColorPalettePreference colorPalettePreference;

    /**
     * The {@link SwitchPreference}.
     */
    private SwitchPreference switchPreference;

    /**
     * Adapts the summary of the {@link SwitchPreference}, depending on whether the values of
     * preferences should be shown as their summaries.
     *
     * @param showValueAsSummary
     *         True, if the values of preferences should be shown as their summaries, false
     *         otherwise
     */
    private void adaptSwitchPreferenceSummary(final boolean showValueAsSummary) {
        if (showValueAsSummary) {
            switchPreference.setSummaryOn(R.string.switch_preference_summary_on);
            switchPreference.setSummaryOff(R.string.switch_preference_summary_off);
        } else {
            switchPreference.setSummaryOn(null);
            switchPreference.setSummaryOff(null);
        }
    }

    /**
     * Creates and returns a listener, which allows to adapt, whether the preference's values should
     * be shown as summaries, or not, when the appropriate setting has been changed.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceChangeListener}
     */
    private OnPreferenceChangeListener createShowValueAsSummaryListener() {
        return new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                boolean showValueAsSummary = (Boolean) newValue;
                editTextPreference.showValueAsSummary(showValueAsSummary);
                listPreference.showValueAsSummary(showValueAsSummary);
                multiChoiceListPreference.showValueAsSummary(showValueAsSummary);
                seekBarPreference.showValueAsSummary(showValueAsSummary);
                numberPickerPreference.showValueAsSummary(showValueAsSummary);
                digitPickerPreference.showValueAsSummary(showValueAsSummary);
                resolutionPreference.showValueAsSummary(showValueAsSummary);
                colorPalettePreference.showValueAsSummary(showValueAsSummary);
                adaptSwitchPreferenceSummary(showValueAsSummary);
                return true;
            }

        };
    }

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean showValueAsSummary = sharedPreferences
                .getBoolean(getString(R.string.show_value_as_summary_preference_key), true);

        Preference showValueAsSummaryPreference =
                findPreference(getString(R.string.show_value_as_summary_preference_key));
        showValueAsSummaryPreference
                .setOnPreferenceChangeListener(createShowValueAsSummaryListener());

        editTextPreference =
                (EditTextPreference) findPreference(getString(R.string.edit_text_preference_key));
        editTextPreference.addValidator(
                Validators.notEmpty(getActivity(), R.string.not_empty_validator_error_message));
        editTextPreference.showValueAsSummary(showValueAsSummary);
        listPreference = (ListPreference) findPreference(getString(R.string.list_preference_key));
        listPreference.showValueAsSummary(showValueAsSummary);
        multiChoiceListPreference = (MultiChoiceListPreference) findPreference(
                getString(R.string.multi_choice_list_preference_key));
        multiChoiceListPreference.showValueAsSummary(showValueAsSummary);
        seekBarPreference =
                (SeekBarPreference) findPreference(getString(R.string.seek_bar_preference_key));
        seekBarPreference.showValueAsSummary(showValueAsSummary);
        numberPickerPreference = (NumberPickerPreference) findPreference(
                getString(R.string.number_picker_preference_key));
        numberPickerPreference.showValueAsSummary(showValueAsSummary);
        digitPickerPreference = (DigitPickerPreference) findPreference(
                getString(R.string.digit_picker_preference_key));
        digitPickerPreference.showValueAsSummary(showValueAsSummary);
        resolutionPreference = (ResolutionPreference) findPreference(
                getString(R.string.resolution_preference_key));
        resolutionPreference.showValueAsSummary(showValueAsSummary);
        colorPalettePreference = (ColorPalettePreference) findPreference(
                getString(R.string.color_palette_preference_key));
        colorPalettePreference.showValueAsSummary(showValueAsSummary);
        switchPreference =
                (SwitchPreference) findPreference(getString(R.string.switch_preference_key));
        adaptSwitchPreferenceSummary(showValueAsSummary);
    }

}