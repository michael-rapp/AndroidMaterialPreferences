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
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

/**
 * Tests the functionality of the class {@link SwitchPreference}.
 *
 * @author Michael Rapp
 */
public class SwitchPreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        Context context = getContext();
        SwitchPreference switchPreference = new SwitchPreference(getContext());
        assertEquals(context, switchPreference.getContext());
        assertNull(switchPreference.getSwitchTextOn());
        assertNull(switchPreference.getSwitchTextOff());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        Context context = getContext();
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.switch_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        SwitchPreference switchPreference = new SwitchPreference(getContext(), attributeSet);
        assertEquals(context, switchPreference.getContext());
        assertNull(switchPreference.getSwitchTextOn());
        assertNull(switchPreference.getSwitchTextOff());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.switch_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        SwitchPreference switchPreference =
                new SwitchPreference(getContext(), attributeSet, defaultStyle);
        assertEquals(context, switchPreference.getContext());
        assertNull(switchPreference.getSwitchTextOn());
        assertNull(switchPreference.getSwitchTextOff());
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
            XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.switch_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            SwitchPreference switchPreference =
                    new SwitchPreference(getContext(), attributeSet, defaultStyle,
                            defaultStyleAttribute);
            assertEquals(context, switchPreference.getContext());
            assertNull(switchPreference.getSwitchTextOn());
            assertNull(switchPreference.getSwitchTextOff());
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the text, which should be
     * displayed on the preference's switch, when it is checked and expects a {@link CharSequence}
     * as a parameter.
     */
    public final void testSwitchTextOn() {
        CharSequence switchTextOn = "switchTextOn";
        SwitchPreference switchPreference = new SwitchPreference(getContext());
        switchPreference.setSwitchTextOn(switchTextOn);
        assertEquals(switchTextOn, switchPreference.getSwitchTextOn());
    }

    /**
     * Tests the functionality of the method, which allows to set the text, which should be
     * displayed on the preference's switch, when it is clicked and expects a resource id as a
     * parameter.
     */
    public final void testSwitchTextOnWithResourceIdParameter() {
        int resourceId = android.R.string.cancel;
        CharSequence switchTextOn = getContext().getText(resourceId);
        SwitchPreference switchPreference = new SwitchPreference(getContext());
        switchPreference.setSwitchTextOn(resourceId);
        assertEquals(switchTextOn, switchPreference.getSwitchTextOn());
    }

    /**
     * Tests the functionality of the method, which allows to set the text, which should be
     * displayed on the preference's switch, when it is not checked and expects a {@link
     * CharSequence} as a parameter.
     */
    public final void testSwitchTextOff() {
        CharSequence switchTextOff = "switchTextOff";
        SwitchPreference switchPreference = new SwitchPreference(getContext());
        switchPreference.setSwitchTextOff(switchTextOff);
        assertEquals(switchTextOff, switchPreference.getSwitchTextOff());
    }

    /**
     * Tests the functionality of the method, which allows to set the text, which should be
     * displayed on the preference's switch, when it is not clicked and expects a resource id as a
     * parameter.
     */
    public final void testSwitchTextOffWithResourceIdParameter() {
        int resourceId = android.R.string.cancel;
        CharSequence switchTextOff = getContext().getText(resourceId);
        SwitchPreference switchPreference = new SwitchPreference(getContext());
        switchPreference.setSwitchTextOff(resourceId);
        assertEquals(switchTextOff, switchPreference.getSwitchTextOff());
    }

}