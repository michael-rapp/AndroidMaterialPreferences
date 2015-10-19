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