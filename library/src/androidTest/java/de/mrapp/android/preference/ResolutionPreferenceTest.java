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
import android.util.Pair;
import android.util.Xml;

import junit.framework.Assert;

import org.xmlpull.v1.XmlPullParser;

import de.mrapp.android.preference.ResolutionPreference.SavedState;

/**
 * Tests the functionality of the class {@link ResolutionPreference}.
 *
 * @author Michael Rapp
 */
public class ResolutionPreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        Context context = getContext();
        ResolutionPreference resolutionPreference = new ResolutionPreference(context);
        assertEquals(0, resolutionPreference.getWidth());
        assertEquals(0, resolutionPreference.getHeight());
        assertTrue(resolutionPreference.getUnit().length() > 0);
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        Context context = getContext();
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.resolution_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        ResolutionPreference resolutionPreference = new ResolutionPreference(context, attributeSet);
        assertEquals(0, resolutionPreference.getWidth());
        assertEquals(0, resolutionPreference.getHeight());
        assertTrue(resolutionPreference.getUnit().length() > 0);
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.resolution_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        ResolutionPreference resolutionPreference =
                new ResolutionPreference(context, attributeSet, defaultStyle);
        assertEquals(0, resolutionPreference.getWidth());
        assertEquals(0, resolutionPreference.getHeight());
        assertTrue(resolutionPreference.getUnit().length() > 0);
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
                    context.getResources().getXml(R.xml.resolution_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            ResolutionPreference resolutionPreference =
                    new ResolutionPreference(context, attributeSet, defaultStyle,
                            defaultStyleAttribute);
            assertEquals(0, resolutionPreference.getWidth());
            assertEquals(0, resolutionPreference.getHeight());
            assertTrue(resolutionPreference.getUnit().length() > 0);
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the preference's resolution.
     */
    public final void testSetResolution() {
        int width = 1;
        int height = 2;
        ResolutionPreference resolutionPreference = new ResolutionPreference(getContext());
        resolutionPreference.setResolution(width, height);
        assertEquals(width, resolutionPreference.getWidth());
        assertEquals(height, resolutionPreference.getHeight());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the preference's resolution, if the width is less than 1.
     */
    public final void testSetResolutionThrowsExceptionIfWidthIsLessThanOne() {
        try {
            ResolutionPreference resolutionPreference = new ResolutionPreference(getContext());
            resolutionPreference.setResolution(0, 1);
            Assert.fail();
        } catch (IllegalArgumentException e) {

        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the preference's resolution, if the height is less than 1.
     */
    public final void testSetResolutionThrowsExceptionIfHeightIsLessThanOne() {
        try {
            ResolutionPreference resolutionPreference = new ResolutionPreference(getContext());
            resolutionPreference.setResolution(1, 0);
            Assert.fail();
        } catch (IllegalArgumentException e) {

        }
    }

    /**
     * Tests the functionality of the method, which allows to set the unit of the preference's
     * resolution.
     */
    public final void testSetUnit() {
        CharSequence unit = "unit";
        ResolutionPreference resolutionPreference = new ResolutionPreference(getContext());
        resolutionPreference.setUnit(unit);
        assertEquals(unit, resolutionPreference.getUnit());
    }

    /**
     * Tests the functionality of the method, which allows to set the unit of the preference's
     * resolution and expects a resource id as a parameter.
     */
    public final void testSetUnitWithResourceIdParameter() {
        int resourceId = android.R.string.cancel;
        CharSequence unit = getContext().getText(resourceId);
        ResolutionPreference resolutionPreference = new ResolutionPreference(getContext());
        resolutionPreference.setUnit(resourceId);
        assertEquals(unit, resolutionPreference.getUnit());
    }

    /**
     * Tests the functionality of the method, which allows to retrieve the preference's summary,
     * depending on whether the preference's value should be shown as its summary, or not.
     */
    public final void testGetSummary() {
        String summary = "summary";
        CharSequence unit = "unit";
        int width = 2;
        int height = 3;
        ResolutionPreference resolutionPreference = new ResolutionPreference(getContext());
        resolutionPreference.setUnit(unit);
        resolutionPreference.setSummary(summary);
        resolutionPreference.setResolution(width, height);
        assertEquals(summary, resolutionPreference.getSummary());
        resolutionPreference.showValueAsSummary(true);
        assertEquals(width + " x " + height + " " + unit, resolutionPreference.getSummary());
    }

    /**
     * Tests the functionality of the method, which allows to create a textual representation of a
     * resolution.
     */
    public final void testFormatResolution() {
        int width = 2;
        int height = 3;
        String text = ResolutionPreference.formatResolution(getContext(), width, height);
        assertEquals(width + "x" + height, text);
    }

    /**
     * Tests the functionality of the method, which allows to parse the textual representation of a
     * resolution.
     */
    public final void testParseResolution() {
        int width = 2;
        int height = 3;
        String text = width + "x" + height;
        Pair<Integer, Integer> resolution =
                ResolutionPreference.parseResolution(getContext(), text);
        assertEquals(width, (int) resolution.first);
        assertEquals(height, (int) resolution.second);
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * parse the textual representation of a resolution, if the separator is invalid.
     */
    public final void testParseResolutionThrowsExceptionWhenSeparatorIsInvalid() {
        try {
            ResolutionPreference.parseResolution(getContext(), "1y2");
            Assert.fail();
        } catch (IllegalArgumentException e) {

        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * parse the textual representation of a resolution, if a dimension is invalid.
     */
    public final void testParseResolutionThrowsExceptionWhenDimensionIsInvalid() {
        try {
            ResolutionPreference.parseResolution(getContext(), "1x.");
            Assert.fail();
        } catch (IllegalArgumentException e) {

        }
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
        int width = 2;
        int height = 3;
        ResolutionPreference resolutionPreference = new ResolutionPreference(getContext());
        resolutionPreference.setPersistent(false);
        resolutionPreference.setResolution(width, height);
        SavedState savedState = (SavedState) resolutionPreference.onSaveInstanceState();
        assertEquals(width, savedState.width);
        assertEquals(height, savedState.height);
    }

    /**
     * Tests the functionality of the onRestoreInstanceState-method.
     */
    public final void testOnRestoreInstanceState() {
        ResolutionPreference resolutionPreference = new ResolutionPreference(getContext());
        resolutionPreference.setResolution(2, 3);
        resolutionPreference.setPersistent(false);
        Parcelable parcelable = resolutionPreference.onSaveInstanceState();
        ResolutionPreference restoredResolutionPreference = new ResolutionPreference(getContext());
        restoredResolutionPreference.onRestoreInstanceState(parcelable);
        assertEquals(resolutionPreference.getWidth(), restoredResolutionPreference.getWidth());
        assertEquals(resolutionPreference.getHeight(), restoredResolutionPreference.getHeight());
    }

}