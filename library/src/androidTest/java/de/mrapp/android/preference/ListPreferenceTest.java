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

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.util.Xml;

/**
 * Tests the functionality of the class {@link ListPreference}.
 *
 * @author Michael Rapp
 */
public class ListPreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        Context context = getContext();
        ListPreference listPreference = new ListPreference(context);
        assertNull(listPreference.getValue());
        assertNull(listPreference.getEntry());
        assertNull(listPreference.getSummary());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        Context context = getContext();
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.list_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        ListPreference listPreference = new ListPreference(context, attributeSet);
        assertNull(listPreference.getValue());
        assertNull(listPreference.getEntry());
        assertNull(listPreference.getSummary());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.list_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        ListPreference listPreference = new ListPreference(context, attributeSet, defaultStyle);
        assertNull(listPreference.getValue());
        assertNull(listPreference.getEntry());
        assertNull(listPreference.getSummary());
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
            XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.list_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            ListPreference listPreference =
                    new ListPreference(context, attributeSet, defaultStyle, defaultStyleAttribute);
            assertNull(listPreference.getValue());
            assertNull(listPreference.getEntry());
            assertNull(listPreference.getSummary());
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the preference's currently
     * persisted value.
     */
    public final void testSetValue() {
        String value = "value";
        ListPreference listPreference = new ListPreference(getContext());
        listPreference.setValue(value);
        assertEquals(value, listPreference.getValue());
    }

    /**
     * Tests the functionality of the method, which allows to set the index of the preference's
     * currently persisted value.
     */
    public final void testSetValueIndex() {
        String value = "value";
        CharSequence[] entryValues = new CharSequence[]{value};
        ListPreference listPreference = new ListPreference(getContext());
        listPreference.setEntryValues(entryValues);
        listPreference.setValueIndex(0);
        assertEquals(value, listPreference.getValue());
    }

    /**
     * Tests the functionality of the method, which allows to retrieve the entry, which corresponds
     * to the preference's current value.
     */
    public final void testGetEntry() {
        String entry = "entry";
        CharSequence[] entryValues = new CharSequence[]{"value"};
        CharSequence[] entries = new CharSequence[]{entry};
        ListPreference listPreference = new ListPreference(getContext());
        listPreference.setEntryValues(entryValues);
        listPreference.setEntries(entries);
        listPreference.setValueIndex(0);
        assertEquals(entry, listPreference.getEntry());
    }

    /**
     * Tests the functionality of the method, which allows to retrieve the preference's summary,
     * depending on whether the preference's value should be shown as its summary, or not.
     */
    public final void testGetSummary() {
        CharSequence summary = "summary";
        String entry = "entry";
        CharSequence[] entryValues = new CharSequence[]{"value"};
        CharSequence[] entries = new CharSequence[]{entry};
        ListPreference listPreference = new ListPreference(getContext());
        listPreference.setSummary(summary);
        listPreference.setEntryValues(entryValues);
        listPreference.setEntries(entries);
        listPreference.setValueIndex(0);
        assertEquals(summary, listPreference.getSummary());
        listPreference.showValueAsSummary(true);
        assertEquals(entry, listPreference.getSummary());
    }

    /**
     * Tests the functionality of the onSaveInstanceState-method.
     */
    public final void testOnSaveInstanceState() {
        String value = "value";
        ListPreference listPreference = new ListPreference(getContext());
        listPreference.setValue(value);
        listPreference.setPersistent(false);
        ListPreference.SavedState savedState =
                (ListPreference.SavedState) listPreference.onSaveInstanceState();
        assertEquals(value, savedState.value);
    }

    /**
     * Tests the functionality of the onRestoreInstanceState-method.
     */
    public final void testOnRestoreInstanceState() {
        String value = "value";
        ListPreference listPreference = new ListPreference(getContext());
        listPreference.setValue(value);
        listPreference.setPersistent(false);
        Parcelable parcelable = listPreference.onSaveInstanceState();
        ListPreference restoredListPreference = new ListPreference(getContext());
        restoredListPreference.onRestoreInstanceState(parcelable);
        assertEquals(listPreference.getValue(), restoredListPreference.getValue());
    }

}