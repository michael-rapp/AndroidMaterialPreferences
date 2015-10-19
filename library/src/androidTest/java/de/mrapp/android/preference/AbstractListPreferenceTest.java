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
import android.graphics.Color;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.util.Xml;

import junit.framework.Assert;

import org.xmlpull.v1.XmlPullParser;

/**
 * Tests the functionality of the class {@link AbstractListPreference}.
 *
 * @author Michael Rapp
 */
public class AbstractListPreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        Context context = getContext();
        AbstractListPreference listPreference = new AbstractListPreferenceImplementation(context);
        assertEquals(-1, listPreference.getDialogItemColor());
        assertEquals(-1, listPreference.getDialogItemControlColor());
        assertNotNull(listPreference.getEntries());
        assertEquals(0, listPreference.getEntries().length);
        assertNotNull(listPreference.getEntryValues());
        assertEquals(0, listPreference.getEntryValues().length);
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        Context context = getContext();
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.abstract_list_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        AbstractListPreference listPreference =
                new AbstractListPreferenceImplementation(context, attributeSet);
        assertEquals(-1, listPreference.getDialogItemColor());
        assertEquals(-1, listPreference.getDialogItemControlColor());
        assertNotNull(listPreference.getEntries());
        assertEquals(0, listPreference.getEntries().length);
        assertNotNull(listPreference.getEntryValues());
        assertEquals(0, listPreference.getEntryValues().length);
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.abstract_list_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        AbstractListPreference listPreference =
                new AbstractListPreferenceImplementation(context, attributeSet, defaultStyle);
        assertEquals(-1, listPreference.getDialogItemColor());
        assertEquals(-1, listPreference.getDialogItemControlColor());
        assertNotNull(listPreference.getEntries());
        assertEquals(0, listPreference.getEntries().length);
        assertNotNull(listPreference.getEntryValues());
        assertEquals(0, listPreference.getEntryValues().length);
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
                    context.getResources().getXml(R.xml.abstract_list_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            AbstractListPreference listPreference =
                    new AbstractListPreferenceImplementation(context, attributeSet, defaultStyle,
                            defaultStyleAttribute);
            assertEquals(-1, listPreference.getDialogItemColor());
            assertEquals(-1, listPreference.getDialogItemControlColor());
            assertNotNull(listPreference.getEntries());
            assertEquals(0, listPreference.getEntries().length);
            assertNotNull(listPreference.getEntryValues());
            assertEquals(0, listPreference.getEntryValues().length);
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the item color of the preference's
     * dialog.
     */
    public final void testSetDialogItemColor() {
        int color = Color.BLACK;
        AbstractListPreference listPreference =
                new AbstractListPreferenceImplementation(getContext());
        listPreference.setDialogItemColor(color);
        assertEquals(color, listPreference.getDialogItemColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the item control color of the
     * preference's dialog.
     */
    public final void testSetDialogItemControlColor() {
        int color = Color.BLACK;
        AbstractListPreference listPreference =
                new AbstractListPreferenceImplementation(getContext());
        listPreference.setDialogItemControlColor(color);
        assertEquals(color, listPreference.getDialogItemControlColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the preference's entries and
     * expects a {@link CharSequence} array as a parameter.
     */
    public final void testSetEntriesWithCharSequenceArrayParameter() {
        CharSequence[] entries = new CharSequence[]{"entry1", "entry2"};
        AbstractListPreference listPreference =
                new AbstractListPreferenceImplementation(getContext());
        listPreference.setEntries(entries);
        assertEquals(entries.length, listPreference.getEntries().length);
        assertEquals(entries[0], listPreference.getEntries()[0]);
        assertEquals(entries[1], listPreference.getEntries()[1]);
    }

    /**
     * Tests the functionality of the method, which allows to set the preference's entries and
     * expects a resource id as a parameter.
     */
    public final void testSetEntriesWithResourceIdParameter() {
        int resourceId = android.R.array.emailAddressTypes;
        CharSequence[] array = getContext().getResources().getTextArray(resourceId);
        AbstractListPreference listPreference =
                new AbstractListPreferenceImplementation(getContext());
        listPreference.setEntries(resourceId);
        assertEquals(array.length, listPreference.getEntries().length);
        assertEquals(array[0], listPreference.getEntries()[0]);
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * preference's entries, if the entries are null.
     */
    public final void testSetEntriesThrowsException() {
        try {
            new AbstractListPreferenceImplementation(getContext()).setEntries(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the preference's entry values and
     * expects a {@link CharSequence} array as a parameter.
     */
    public final void testSetEntryValuesWithCharSequenceArrayParameter() {
        CharSequence[] entryValues = new CharSequence[]{"value1", "value2"};
        AbstractListPreference listPreference =
                new AbstractListPreferenceImplementation(getContext());
        listPreference.setEntryValues(entryValues);
        assertEquals(entryValues.length, listPreference.getEntryValues().length);
        assertEquals(entryValues[0], listPreference.getEntryValues()[0]);
        assertEquals(entryValues[1], listPreference.getEntryValues()[1]);
    }

    /**
     * Tests the functionality of the method, which allows to set the preference's entry values and
     * expects a resource id as a parameter.
     */
    public final void testSetEntryValuesWithResourceIdParameter() {
        int resourceId = android.R.array.emailAddressTypes;
        CharSequence[] array = getContext().getResources().getTextArray(resourceId);
        AbstractListPreference listPreference =
                new AbstractListPreferenceImplementation(getContext());
        listPreference.setEntryValues(resourceId);
        assertEquals(array.length, listPreference.getEntryValues().length);
        assertEquals(array[0], listPreference.getEntryValues()[0]);
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * preference's entry values, if the entry values are null.
     */
    public final void testSetEntryValuesThrowsException() {
        try {
            new AbstractListPreferenceImplementation(getContext()).setEntryValues(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the needInputMethod-method.
     */
    public final void testNeedInputMethod() {
        assertFalse(new AbstractListPreferenceImplementation(getContext()).needInputMethod());
    }

}