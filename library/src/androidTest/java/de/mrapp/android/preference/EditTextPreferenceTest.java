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

import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import de.mrapp.android.preference.EditTextPreference.SavedState;

/**
 * Tests the functionality of the class {@link EditTextPreference}.
 *
 * @author Michael Rapp
 */
public class EditTextPreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        Context context = getContext();
        EditTextPreference editTextPreference = new EditTextPreference(context);
        assertNull(editTextPreference.getText());
        assertNull(editTextPreference.getSummary());
        assertTrue(editTextPreference.shouldDisableDependents());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        Context context = getContext();
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.edit_text_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        EditTextPreference editTextPreference = new EditTextPreference(context, attributeSet);
        assertNull(editTextPreference.getText());
        assertNull(editTextPreference.getSummary());
        assertTrue(editTextPreference.shouldDisableDependents());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser =
                context.getResources().getXml(R.xml.abstract_dialog_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        EditTextPreference editTextPreference =
                new EditTextPreference(context, attributeSet, defaultStyle);
        assertNull(editTextPreference.getText());
        assertNull(editTextPreference.getSummary());
        assertTrue(editTextPreference.shouldDisableDependents());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set, a default style and a default style attribute as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleAndDefaultStyleAttributeParameters() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Context context = getContext();
            int defaultStyle = 0;
            int defaultStyleAttribute = 0;
            XmlPullParser xmlPullParser =
                    context.getResources().getXml(R.xml.abstract_dialog_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            EditTextPreference editTextPreference =
                    new EditTextPreference(context, attributeSet, defaultStyle,
                            defaultStyleAttribute);
            assertNull(editTextPreference.getText());
            assertNull(editTextPreference.getSummary());
            assertTrue(editTextPreference.shouldDisableDependents());
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the preference's text.
     */
    public final void testSetText() {
        String text = "text";
        EditTextPreference editTextPreference = new EditTextPreference(getContext());
        editTextPreference.setText(text);
        assertEquals(text, editTextPreference.getText());
        assertFalse(editTextPreference.shouldDisableDependents());
    }

    /**
     * Tests the functionality of the method, which allows to retrieve the preference's summary,
     * depending on whether the preference's value should be shown as its summary, or not.
     */
    public final void testGetSummary() {
        CharSequence summary = "summary";
        String text = "text";
        EditTextPreference editTextPreference = new EditTextPreference(getContext());
        editTextPreference.setSummary(summary);
        editTextPreference.setText(text);
        assertEquals(summary, editTextPreference.getSummary());
        editTextPreference.showValueAsSummary(true);
        assertEquals(text, editTextPreference.getSummary());
    }

    /**
     * Tests the functionality of the needInputMethod-method.
     */
    public final void testNeedInputMethod() {
        assertTrue(new EditTextPreference(getContext()).needInputMethod());
    }

    /**
     * Tests the functionality of the onSaveInstanceState-method.
     */
    public final void testOnSaveInstanceState() {
        String text = "text";
        EditTextPreference editTextPreference = new EditTextPreference(getContext());
        editTextPreference.setText(text);
        editTextPreference.setPersistent(false);
        SavedState savedState = (SavedState) editTextPreference.onSaveInstanceState();
        assertEquals(text, savedState.text);
    }

    /**
     * Tests the functionality of the onRestoreInstanceState-method.
     */
    public final void testOnRestoreInstanceState() {
        EditTextPreference editTextPreference = new EditTextPreference(getContext());
        editTextPreference.setText("text");
        editTextPreference.setPersistent(false);
        Parcelable parcelable = editTextPreference.onSaveInstanceState();
        EditTextPreference restoredEditTextPreference = new EditTextPreference(getContext());
        restoredEditTextPreference.onRestoreInstanceState(parcelable);
        assertEquals(editTextPreference.getText(), restoredEditTextPreference.getText());
    }

}