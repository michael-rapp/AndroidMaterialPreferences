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

        }
    }

    /**
     * Tests the functionality of the needInputMethod-method.
     */
    public final void testNeedInputMethod() {
        assertFalse(new AbstractListPreferenceImplementation(getContext()).needInputMethod());
    }

}