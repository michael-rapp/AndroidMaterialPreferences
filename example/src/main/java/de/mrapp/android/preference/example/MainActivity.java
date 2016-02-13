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

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * The main activity of the example app.
 *
 * @author Michael Rapp
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The fragment, which contains the preferences.
     */
    private Fragment preferenceFragment;

    /**
     * Initializes the activity's toolbar.
     */
    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Shows the preference fragment, which contains the activity's content.
     */
    private void showPreferenceFragment() {
        if (preferenceFragment == null) {
            preferenceFragment = Fragment.instantiate(this, PreferenceFragment.class.getName());
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("preferenceBackStack");
        transaction.replace(R.id.fragment, preferenceFragment);
        transaction.commit();
    }

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            preferenceFragment =
                    getFragmentManager().getFragment(savedInstanceState, "preferenceFragment");
        }

        initializeToolbar();
        showPreferenceFragment();
    }

    @Override
    protected final void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentManager().putFragment(outState, "preferenceFragment", preferenceFragment);
    }

}