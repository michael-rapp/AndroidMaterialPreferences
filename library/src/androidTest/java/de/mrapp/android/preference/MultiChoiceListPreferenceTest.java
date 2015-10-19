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

import junit.framework.Assert;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests the functionality of the class {@link MultiChoiceListPreference}.
 *
 * @author Michael Rapp
 */
public class MultiChoiceListPreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        Context context = getContext();
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(context);
        assertNull(multiChoiceListPreference.getValues());
        assertNull(multiChoiceListPreference.getSelectedEntries());
        assertNull(multiChoiceListPreference.getSummary());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        Context context = getContext();
        XmlPullParser xmlPullParser =
                context.getResources().getXml(R.xml.multi_choice_list_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(context, attributeSet);
        assertNull(multiChoiceListPreference.getValues());
        assertNull(multiChoiceListPreference.getSelectedEntries());
        assertNull(multiChoiceListPreference.getSummary());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser =
                context.getResources().getXml(R.xml.multi_choice_list_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(context, attributeSet, defaultStyle);
        assertNull(multiChoiceListPreference.getValues());
        assertNull(multiChoiceListPreference.getSelectedEntries());
        assertNull(multiChoiceListPreference.getSummary());
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
                    context.getResources().getXml(R.xml.multi_choice_list_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            MultiChoiceListPreference multiChoiceListPreference =
                    new MultiChoiceListPreference(context, attributeSet, defaultStyle,
                            defaultStyleAttribute);
            assertNull(multiChoiceListPreference.getValues());
            assertNull(multiChoiceListPreference.getSelectedEntries());
            assertNull(multiChoiceListPreference.getSummary());
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the preference's currently
     * persisted values.
     */
    public final void testSetValues() {
        String value1 = "value1";
        String value2 = "value2";
        Set<String> values = new HashSet<>();
        values.add(value1);
        values.add(value2);
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.setValues(values);
        assertEquals(values.size(), multiChoiceListPreference.getValues().size());
        assertTrue(multiChoiceListPreference.getValues().contains(value1));
        assertTrue(multiChoiceListPreference.getValues().contains(value2));
    }

    /**
     * Tests the functionality of the method, which allows to add a value to the preference, if the
     * preference's currently persisted value is null.
     */
    public final void testAddValueIfCurrentValueIsNull() {
        String value = "value";
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.addValue(value);
        assertEquals(1, multiChoiceListPreference.getValues().size());
        assertTrue(multiChoiceListPreference.getValues().contains(value));
    }

    /**
     * Tests the functionality of the method, which allows to add a value to the preference, if the
     * preference's currently persisted value is not null.
     */
    public final void testAddValueIfCurrentValueIsNotNull() {
        String value1 = "value1";
        String value2 = "value2";
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.addValue(value1);
        multiChoiceListPreference.addValue(value2);
        assertEquals(2, multiChoiceListPreference.getValues().size());
        assertTrue(multiChoiceListPreference.getValues().contains(value1));
        assertTrue(multiChoiceListPreference.getValues().contains(value2));
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to add a
     * value to the preference, if the value is null.
     */
    public final void testAddValueThrowsException() {
        try {
            MultiChoiceListPreference multiChoiceListPreference =
                    new MultiChoiceListPreference(getContext());
            multiChoiceListPreference.addValue(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to remove a value from the preference, if
     * the preference's currently persisted value is null.
     */
    public final void testRemoveValueIfCurrentValueIsNull() {
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.removeValue("value");
        assertNull(multiChoiceListPreference.getValues());
    }

    /**
     * Tests the functionality of the method, which allows to remove a value from the preference, if
     * the preference's currently persisted value is not null.
     */
    public final void testRemoveValueIfCurrentValueIsNotNull() {
        String value = "value";
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.addValue(value);
        multiChoiceListPreference.removeValue(value);
        assertEquals(0, multiChoiceListPreference.getValues().size());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to remove
     * a value to the preference, if the value is null.
     */
    public final void testRemoveValueThrowsException() {
        try {
            MultiChoiceListPreference multiChoiceListPreference =
                    new MultiChoiceListPreference(getContext());
            multiChoiceListPreference.removeValue(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to add all values, which are contained by
     * a collection, to the preference, if the preference's currently persisted value is null.
     */
    public final void testAddAllValuesIfCurrentValueIsNull() {
        String value1 = "value1";
        String value2 = "value2";
        Collection<String> values = new ArrayList<>();
        values.add(value1);
        values.add(value2);
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.addAllValues(values);
        assertEquals(2, multiChoiceListPreference.getValues().size());
        assertTrue(multiChoiceListPreference.getValues().contains(value1));
        assertTrue(multiChoiceListPreference.getValues().contains(value2));
    }

    /**
     * Tests the functionality of the method, which allows to add all values, which are contained by
     * a collection, to the preference, if the preference's currently persisted value is not null.
     */
    public final void testAddAllValuesIfCurrentValueIsNotNull() {
        String value1 = "value1";
        String value2 = "value2";
        String value3 = "value3";
        Collection<String> values = new ArrayList<>();
        values.add(value2);
        values.add(value3);
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.addValue(value1);
        multiChoiceListPreference.addAllValues(values);
        assertEquals(3, multiChoiceListPreference.getValues().size());
        assertTrue(multiChoiceListPreference.getValues().contains(value1));
        assertTrue(multiChoiceListPreference.getValues().contains(value2));
        assertTrue(multiChoiceListPreference.getValues().contains(value3));
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to add all
     * values, which are contained by a collection, to the preference, if the collection is null.
     */
    public final void testAddAllValuesThrowsException() {
        try {
            MultiChoiceListPreference multiChoiceListPreference =
                    new MultiChoiceListPreference(getContext());
            multiChoiceListPreference.addAllValues(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to remove all values, which are contained
     * by a collection, from the preference, if the preference's currently persisted value is null.
     */
    public final void testRemoveAllValuesIfCurrentValueIsNull() {
        String value1 = "value1";
        String value2 = "value2";
        Collection<String> values = new ArrayList<>();
        values.add(value1);
        values.add(value2);
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.removeAllValues(values);
        assertNull(multiChoiceListPreference.getValues());
    }

    /**
     * Tests the functionality of the method, which allows to remove all values, which are contained
     * by a collection, from the preference, if the preference's currently persisted value is not
     * null.
     */
    public final void testRemoveAllValuesIfCurrentValueIsNotNull() {
        String value1 = "value1";
        String value2 = "value2";
        Collection<String> values = new ArrayList<>();
        values.add(value1);
        values.add(value2);
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.addValue(value1);
        multiChoiceListPreference.addValue(value2);
        multiChoiceListPreference.removeAllValues(values);
        assertEquals(0, multiChoiceListPreference.getValues().size());
    }

    /**
     * Tests the functionality of the method, which allows to retrieve the entries, which correspond
     * to the preference's current values.
     */
    public final void testGetSelectedEntries() {
        String entry1 = "entry1";
        String entry2 = "entry2";
        String entry3 = "entry3";
        CharSequence[] entryValues = new CharSequence[]{"value1", "value2", "value3"};
        CharSequence[] entries = new CharSequence[]{entry1, entry2, entry3};
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.setEntryValues(entryValues);
        multiChoiceListPreference.setEntries(entries);
        multiChoiceListPreference.addValue(entryValues[2].toString());
        multiChoiceListPreference.addValue(entryValues[0].toString());
        CharSequence[] selectedEntries = multiChoiceListPreference.getSelectedEntries();
        assertEquals(2, selectedEntries.length);
        assertEquals(entry1, selectedEntries[0]);
        assertEquals(entry3, selectedEntries[1]);
    }

    /**
     * Tests the functionality of the method, which allows to retrieve the preference's summary,
     * depending on whether the preference's values should be shown as its summary, or not.
     */
    public final void testGetSummary() {
        String entry1 = "entry1";
        String entry2 = "entry2";
        String entry3 = "entry3";
        CharSequence[] entryValues = new CharSequence[]{"value1", "value2", "value3"};
        CharSequence[] entries = new CharSequence[]{entry1, entry2, entry3};
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.setEntryValues(entryValues);
        multiChoiceListPreference.setEntries(entries);
        multiChoiceListPreference.addValue(entryValues[2].toString());
        multiChoiceListPreference.addValue(entryValues[0].toString());
        multiChoiceListPreference.showValueAsSummary(true);
        assertEquals(entry1 + ", " + entry3, multiChoiceListPreference.getSummary());
    }

    /**
     * Tests the functionality of the onSaveInstanceState-method.
     */
    public final void testOnSaveInstanceState() {
        String value1 = "value1";
        String value2 = "value2";
        Set<String> values = new HashSet<>();
        values.add(value1);
        values.add(value2);
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.setValues(values);
        multiChoiceListPreference.setPersistent(false);
        MultiChoiceListPreference.SavedState savedState =
                (MultiChoiceListPreference.SavedState) multiChoiceListPreference
                        .onSaveInstanceState();
        assertEquals(values.size(), savedState.values.size());
        assertTrue(savedState.values.contains(value1));
        assertTrue(savedState.values.contains(value2));
    }

    /**
     * Tests the functionality of the onRestoreInstanceState-method.
     */
    public final void testOnRestoreInstanceState() {
        String value1 = "value1";
        String value2 = "value2";
        Set<String> values = new HashSet<>();
        values.add(value1);
        values.add(value2);
        MultiChoiceListPreference multiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        multiChoiceListPreference.setValues(values);
        multiChoiceListPreference.setPersistent(false);
        Parcelable parcelable = multiChoiceListPreference.onSaveInstanceState();
        MultiChoiceListPreference restoredMultiChoiceListPreference =
                new MultiChoiceListPreference(getContext());
        restoredMultiChoiceListPreference.onRestoreInstanceState(parcelable);
        assertEquals(values.size(), restoredMultiChoiceListPreference.getValues().size());
        assertTrue(restoredMultiChoiceListPreference.getValues().contains(value1));
        assertTrue(restoredMultiChoiceListPreference.getValues().contains(value2));
    }

}