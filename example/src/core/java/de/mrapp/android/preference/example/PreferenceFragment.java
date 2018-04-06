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
package de.mrapp.android.preference.example;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.widget.Toast;

import de.mrapp.android.preference.ActionPreference;
import de.mrapp.android.preference.ColorPalettePreference;
import de.mrapp.android.preference.DialogPreference;
import de.mrapp.android.preference.DigitPickerPreference;
import de.mrapp.android.preference.EditTextPreference;
import de.mrapp.android.preference.ListPreference;
import de.mrapp.android.preference.MultiChoiceListPreference;
import de.mrapp.android.preference.NumberPickerPreference;
import de.mrapp.android.preference.ResolutionPreference;
import de.mrapp.android.preference.SeekBarPreference;
import de.mrapp.android.preference.SwitchPreference;
import de.mrapp.android.preference.example.R;
import de.mrapp.android.validation.Validators;

/**
 * A preference fragment, which contains the example app's settings.
 *
 * @author Michael Rapp
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {

    /**
     * The {@link DialogPreference}.
     */
    private DialogPreference dialogPreference;

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
     * Initializes the preference, which allows to change the app's theme.
     */
    private void initializeThemePreference() {
        Preference themePreference = findPreference(getString(de.mrapp.android.preference.example.R.string.theme_preference_key));
        themePreference.setOnPreferenceChangeListener(createThemeChangeListener());
    }

    /**
     * Creates and returns a listener, which allows to adapt the app's theme, when the value of the
     * corresponding preference has been changed.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * Preference.OnPreferenceChangeListener}
     */
    private Preference.OnPreferenceChangeListener createThemeChangeListener() {
        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                getActivity().recreate();
                return true;
            }

        };
    }

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
            switchPreference.setSummaryOn(de.mrapp.android.preference.example.R.string.switch_preference_summary_on);
            switchPreference.setSummaryOff(de.mrapp.android.preference.example.R.string.switch_preference_summary_off);
        } else {
            switchPreference.setSummaryOn(null);
            switchPreference.setSummaryOff(null);
        }
    }

    /**
     * Creates and returns a listener, which allows to adapt, whether the preference's values should
     * be shown as summaries, or not, when the corresponding setting has been changed.
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

    /**
     * Creates and returns a listener, which allows to adapt, whether the headers of the
     * preference's dialogs should be shown, or not, when the corresponding setting has been
     * changed.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceChangeListener}
     */
    private OnPreferenceChangeListener createShowDialogHeaderListener() {
        return new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                boolean showDialogHeader = (Boolean) newValue;
                dialogPreference.showDialogHeader(showDialogHeader);
                editTextPreference.showDialogHeader(showDialogHeader);
                listPreference.showDialogHeader(showDialogHeader);
                multiChoiceListPreference.showDialogHeader(showDialogHeader);
                seekBarPreference.showDialogHeader(showDialogHeader);
                numberPickerPreference.showDialogHeader(showDialogHeader);
                digitPickerPreference.showDialogHeader(showDialogHeader);
                resolutionPreference.showDialogHeader(showDialogHeader);
                colorPalettePreference.showDialogHeader(showDialogHeader);
                return true;
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to adapt, whether the button bar dividers of the
     * preference's dialogs should be shown, or not, when the corresponding setting has been
     * changed.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceChangeListener}
     */
    private OnPreferenceChangeListener createShowDialogButtonBarDividerListener() {
        return new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                boolean showDialogButtonBarDivider = (Boolean) newValue;
                dialogPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
                editTextPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
                listPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
                multiChoiceListPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
                seekBarPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
                numberPickerPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
                digitPickerPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
                resolutionPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
                colorPalettePreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
                return true;
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to show a toast, when a button of the {@link
     * DialogPreference}'s dialog has been clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnClickListener}
     */
    private OnClickListener createDialogPreferenceClickListener() {
        return new OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                String button = getString(
                        which == DialogInterface.BUTTON_POSITIVE ? android.R.string.ok :
                                android.R.string.cancel);
                String text = String.format(getString(de.mrapp.android.preference.example.R.string.dialog_dismissed_toast), button);
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to show a toast, when the {@link
     * ActionPreference} has been clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceClickListener}
     */
    private OnPreferenceClickListener createActionPreferenceClickListener() {
        return new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), de.mrapp.android.preference.example.R.string.action_preference_toast, Toast.LENGTH_SHORT)
                        .show();
                return true;
            }

        };
    }

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(de.mrapp.android.preference.example.R.xml.preferences);
        initializeThemePreference();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean showValueAsSummary = sharedPreferences
                .getBoolean(getString(de.mrapp.android.preference.example.R.string.show_value_as_summary_preference_key), true);
        boolean showDialogHeader = sharedPreferences
                .getBoolean(getString(de.mrapp.android.preference.example.R.string.show_dialog_header_preference_key), false);
        boolean showDialogButtonBarDivider = sharedPreferences
                .getBoolean(getString(de.mrapp.android.preference.example.R.string.show_dialog_button_bar_divider_preference_key),
                        false);

        Preference showValueAsSummaryPreference =
                findPreference(getString(de.mrapp.android.preference.example.R.string.show_value_as_summary_preference_key));
        showValueAsSummaryPreference
                .setOnPreferenceChangeListener(createShowValueAsSummaryListener());

        Preference showDialogHeaderPreference =
                findPreference(getString(de.mrapp.android.preference.example.R.string.show_dialog_header_preference_key));
        showDialogHeaderPreference.setOnPreferenceChangeListener(createShowDialogHeaderListener());

        Preference showDialogButtonBarDividerPreference =
                findPreference(getString(de.mrapp.android.preference.example.R.string.show_dialog_button_bar_divider_preference_key));
        showDialogButtonBarDividerPreference
                .setOnPreferenceChangeListener(createShowDialogButtonBarDividerListener());

        dialogPreference =
                (DialogPreference) findPreference(getString(de.mrapp.android.preference.example.R.string.dialog_preference_key));
        dialogPreference.showDialogHeader(showDialogHeader);
        dialogPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
        dialogPreference.setOnClickListener(createDialogPreferenceClickListener());
        editTextPreference =
                (EditTextPreference) findPreference(getString(de.mrapp.android.preference.example.R.string.edit_text_preference_key));
        editTextPreference.addValidator(
                Validators.notEmpty(getActivity(), de.mrapp.android.preference.example.R.string.not_empty_validator_error_message));
        editTextPreference.showValueAsSummary(showValueAsSummary);
        editTextPreference.showDialogHeader(showDialogHeader);
        editTextPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
        listPreference = (ListPreference) findPreference(getString(de.mrapp.android.preference.example.R.string.list_preference_key));
        listPreference.showValueAsSummary(showValueAsSummary);
        listPreference.showDialogHeader(showDialogHeader);
        listPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
        multiChoiceListPreference = (MultiChoiceListPreference) findPreference(
                getString(de.mrapp.android.preference.example.R.string.multi_choice_list_preference_key));
        multiChoiceListPreference.showValueAsSummary(showValueAsSummary);
        multiChoiceListPreference.showDialogHeader(showDialogHeader);
        multiChoiceListPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
        seekBarPreference =
                (SeekBarPreference) findPreference(getString(de.mrapp.android.preference.example.R.string.seek_bar_preference_key));
        seekBarPreference.showValueAsSummary(showValueAsSummary);
        seekBarPreference.showDialogHeader(showDialogHeader);
        seekBarPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
        numberPickerPreference = (NumberPickerPreference) findPreference(
                getString(de.mrapp.android.preference.example.R.string.number_picker_preference_key));
        numberPickerPreference.showValueAsSummary(showValueAsSummary);
        numberPickerPreference.showDialogHeader(showDialogHeader);
        numberPickerPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
        digitPickerPreference = (DigitPickerPreference) findPreference(
                getString(de.mrapp.android.preference.example.R.string.digit_picker_preference_key));
        digitPickerPreference.showValueAsSummary(showValueAsSummary);
        digitPickerPreference.showDialogHeader(showDialogHeader);
        digitPickerPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
        resolutionPreference = (ResolutionPreference) findPreference(
                getString(de.mrapp.android.preference.example.R.string.resolution_preference_key));
        resolutionPreference.showValueAsSummary(showValueAsSummary);
        resolutionPreference.showDialogHeader(showDialogHeader);
        resolutionPreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
        colorPalettePreference = (ColorPalettePreference) findPreference(
                getString(de.mrapp.android.preference.example.R.string.color_palette_preference_key));
        colorPalettePreference.showValueAsSummary(showValueAsSummary);
        colorPalettePreference.showDialogHeader(showDialogHeader);
        colorPalettePreference.showDialogButtonBarDivider(showDialogButtonBarDivider);
        switchPreference =
                (SwitchPreference) findPreference(getString(de.mrapp.android.preference.example.R.string.switch_preference_key));
        ActionPreference actionPreference =
                (ActionPreference) findPreference(getString(R.string.action_preference_key));
        actionPreference.setOnPreferenceClickListener(createActionPreferenceClickListener());
        adaptSwitchPreferenceSummary(showValueAsSummary);
    }

}