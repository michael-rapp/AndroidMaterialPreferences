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
import android.os.Build;
import android.os.Parcelable;
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

/**
 * Tests the functionality of the class {@link AbstractTwoStatePreference}.
 *
 * @author Michael Rapp
 */
public class AbstractTwoStatePreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        Context context = getContext();
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(context);
        assertEquals(context, twoStatePreference.getContext());
        assertNull(twoStatePreference.getSummaryOn());
        assertNull(twoStatePreference.getSummaryOff());
        assertFalse(twoStatePreference.getDisableDependentsState());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        Context context = getContext();
        XmlPullParser xmlPullParser =
                context.getResources().getXml(R.xml.abstract_two_state_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(context, attributeSet);
        assertEquals(context, twoStatePreference.getContext());
        assertNull(twoStatePreference.getSummaryOn());
        assertNull(twoStatePreference.getSummaryOff());
        assertFalse(twoStatePreference.getDisableDependentsState());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser =
                context.getResources().getXml(R.xml.abstract_two_state_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(context, attributeSet, defaultStyle);
        assertEquals(context, twoStatePreference.getContext());
        assertNull(twoStatePreference.getSummaryOn());
        assertNull(twoStatePreference.getSummaryOff());
        assertFalse(twoStatePreference.getDisableDependentsState());
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
                    context.getResources().getXml(R.xml.abstract_two_state_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            AbstractTwoStatePreference twoStatePreference =
                    new AbstractTwoStatePreferenceImplementation(context, attributeSet,
                            defaultStyle, defaultStyleAttribute);
            assertEquals(context, twoStatePreference.getContext());
            assertNull(twoStatePreference.getSummaryOn());
            assertNull(twoStatePreference.getSummaryOff());
            assertFalse(twoStatePreference.getDisableDependentsState());
        }
    }

    /**
     * Tests the functionality of the method, which allows to set, wehther the preference should be
     * checked, or not.
     */
    public final void testSetChecked() {
        boolean checked = true;
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        twoStatePreference.setChecked(checked);
        assertEquals(checked, twoStatePreference.isChecked());
    }

    /**
     * Tests the functionality of the method, which allows to set the summary, which should be shown
     * when the preference is checked, and expects a {@link CharSequence} as a parameter.
     */
    public final void testSetSummaryOn() {
        CharSequence summaryOn = "summaryOn";
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        twoStatePreference.setSummaryOn(summaryOn);
        assertEquals(summaryOn, twoStatePreference.getSummaryOn());
    }

    /**
     * Tests the functionality of the method, which allows to set the summary, which should be shown
     * when the preference is checked, and expects a resource id as a parameter.
     */
    public final void testSetSummaryOnWithResourceIdParameter() {
        int resourceId = android.R.string.cancel;
        CharSequence summaryOn = getContext().getText(resourceId);
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        twoStatePreference.setSummaryOn(resourceId);
        assertEquals(summaryOn, twoStatePreference.getSummaryOn());
    }

    /**
     * Tests the functionality of the method, which allows to set the summary, which should be shown
     * when the preference is not checked, and expects a {@link CharSequence} as a parameter.
     */
    public final void testSetSummaryOff() {
        CharSequence summaryOff = "summaryOff";
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        twoStatePreference.setSummaryOff(summaryOff);
        assertEquals(summaryOff, twoStatePreference.getSummaryOff());
    }

    /**
     * Tests the functionality of the method, which allows to set the summary, which should be shown
     * when the preference is not checked, and expects a resource id as a parameter.
     */
    public final void testSetSummaryOffWithResourceIdParameter() {
        int resourceId = android.R.string.cancel;
        CharSequence summaryOff = getContext().getText(resourceId);
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        twoStatePreference.setSummaryOff(resourceId);
        assertEquals(summaryOff, twoStatePreference.getSummaryOff());
    }

    /**
     * Tests the functionality of the method, which allows to set the state, when dependent
     * preferences should be disabled.
     */
    public final void testSetDisableDependentsState() {
        boolean disableDependentsState = true;
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        twoStatePreference.setDisableDependentsState(disableDependentsState);
        assertEquals(disableDependentsState, twoStatePreference.getDisableDependentsState());
    }

    /**
     * Tests the functionality of the shouldDisableDependents-method.
     */
    public final void testShouldDisableDependents() {
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        twoStatePreference.setChecked(false);
        assertTrue(twoStatePreference.shouldDisableDependents());
        twoStatePreference.setChecked(true);
        assertFalse(twoStatePreference.shouldDisableDependents());
        twoStatePreference.setDisableDependentsState(true);
        twoStatePreference.setChecked(false);
        assertFalse(twoStatePreference.shouldDisableDependents());
        twoStatePreference.setChecked(true);
        assertTrue(twoStatePreference.shouldDisableDependents());
        twoStatePreference.setDisableDependentsState(false);
        twoStatePreference.setChecked(true);
        twoStatePreference.setEnabled(false);
        assertTrue(twoStatePreference.shouldDisableDependents());
    }

    /**
     * Tests the functionality of the getSummary-method.
     */
    public final void testGetSummary() {
        CharSequence summary = "summary";
        CharSequence summaryOn = "summaryOn";
        CharSequence summaryOff = "summaryOff";
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        twoStatePreference.setSummary(summary);
        assertEquals(summary, twoStatePreference.getSummary());
        twoStatePreference.setSummaryOn(summaryOn);
        twoStatePreference.setChecked(true);
        assertEquals(summaryOn, twoStatePreference.getSummary());
        twoStatePreference.setSummaryOn(null);
        assertEquals(summary, twoStatePreference.getSummary());
        twoStatePreference.setSummaryOn("");
        assertEquals(summary, twoStatePreference.getSummary());
        twoStatePreference.setSummaryOff(summaryOff);
        twoStatePreference.setChecked(false);
        assertEquals(summaryOff, twoStatePreference.getSummary());
        twoStatePreference.setSummaryOff(null);
        assertEquals(summary, twoStatePreference.getSummary());
        twoStatePreference.setSummaryOff("");
        assertEquals(summary, twoStatePreference.getSummary());
    }

    /**
     * Tests the functionality of the onClick-method.
     */
    public final void testOnClick() {
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        twoStatePreference.setChecked(false);
        twoStatePreference.onClick();
        assertTrue(twoStatePreference.isChecked());
        twoStatePreference.onClick();
        assertFalse(twoStatePreference.isChecked());
    }

    /**
     * Tests the functionality of the onSaveInstanceState-method.
     */
    public final void testOnSaveInstanceState() {
        boolean checked = true;
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        twoStatePreference.setChecked(checked);
        twoStatePreference.setPersistent(false);
        AbstractTwoStatePreference.SavedState savedState =
                (AbstractTwoStatePreference.SavedState) twoStatePreference.onSaveInstanceState();
        assertEquals(checked, savedState.checked);
    }

    /**
     * Tests the functionality of the onRestoreInstanceState-method.
     */
    public final void testOnRestoreInstanceState() {
        boolean checked = true;
        AbstractTwoStatePreference twoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        twoStatePreference.setPersistent(false);
        twoStatePreference.setChecked(checked);
        Parcelable parcelable = twoStatePreference.onSaveInstanceState();
        AbstractTwoStatePreference restoredTwoStatePreference =
                new AbstractTwoStatePreferenceImplementation(getContext());
        restoredTwoStatePreference.onRestoreInstanceState(parcelable);
        assertEquals(checked, restoredTwoStatePreference.isChecked());
    }

}