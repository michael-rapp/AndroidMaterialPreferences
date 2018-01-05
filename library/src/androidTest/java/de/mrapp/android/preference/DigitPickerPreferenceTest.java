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

import junit.framework.Assert;

import org.xmlpull.v1.XmlPullParser;

/**
 * Tests the functionality of the class {@link DigitPickerPreference}.
 *
 * @author Michael Rapp
 */
public class DigitPickerPreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        int defaultNumberOfDigits = getContext().getResources()
                .getInteger(R.integer.digit_picker_preference_default_number_of_digits);
        Context context = getContext();
        DigitPickerPreference digitPickerPreference = new DigitPickerPreference(context);
        assertEquals(defaultNumberOfDigits, digitPickerPreference.getNumberOfDigits());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        int defaultNumberOfDigits = getContext().getResources()
                .getInteger(R.integer.digit_picker_preference_default_number_of_digits);
        Context context = getContext();
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.digit_picker_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        DigitPickerPreference digitPickerPreference =
                new DigitPickerPreference(context, attributeSet);
        assertEquals(defaultNumberOfDigits, digitPickerPreference.getNumberOfDigits());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        int defaultNumberOfDigits = getContext().getResources()
                .getInteger(R.integer.digit_picker_preference_default_number_of_digits);
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.digit_picker_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        DigitPickerPreference digitPickerPreference =
                new DigitPickerPreference(context, attributeSet, defaultStyle);
        assertEquals(defaultNumberOfDigits, digitPickerPreference.getNumberOfDigits());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set, a default style and a default style attribute as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleAndDefaultStyleAttributeParameters() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int defaultNumberOfDigits = getContext().getResources()
                    .getInteger(R.integer.digit_picker_preference_default_number_of_digits);
            Context context = getContext();
            int defaultStyle = 0;
            int defaultStyleAttribute = 0;
            XmlPullParser xmlPullParser =
                    context.getResources().getXml(R.xml.digit_picker_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            DigitPickerPreference digitPickerPreference =
                    new DigitPickerPreference(context, attributeSet, defaultStyle,
                            defaultStyleAttribute);
            assertEquals(defaultNumberOfDigits, digitPickerPreference.getNumberOfDigits());
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the current persisted number of
     * the preference.
     */
    public final void testSetNumber() {
        int number = 33;
        DigitPickerPreference digitPickerPreference = new DigitPickerPreference(getContext());
        digitPickerPreference.setNumber(number);
        assertEquals(number, digitPickerPreference.getNumber());
        assertEquals(number, digitPickerPreference.getCurrentNumber());
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown by the method, which allows to set
     * the current persisted number of the preference, if the number has an invalid number of
     * digits.
     */
    public final void testSetNumberThrowsException() {
        try {
            DigitPickerPreference digitPickerPreference = new DigitPickerPreference(getContext());
            digitPickerPreference.setNumber(12345);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the number of digits of the
     * numbers, the preference allows to choose.
     */
    public final void testSetNumberOfDigits() {
        int numberOfDigits = 2;
        DigitPickerPreference digitPickerPreference = new DigitPickerPreference(getContext());
        digitPickerPreference.setNumberOfDigits(numberOfDigits);
        assertEquals(numberOfDigits, digitPickerPreference.getNumberOfDigits());
    }

    /**
     * Tests the functionality of the onSaveInstanceState-method.
     */
    public final void testOnSaveInstanceState() {
        DigitPickerPreference digitPickerPreference = new DigitPickerPreference(getContext());
        digitPickerPreference.setPersistent(false);
        DigitPickerPreference.SavedState savedState =
                (DigitPickerPreference.SavedState) digitPickerPreference.onSaveInstanceState();
        assertEquals(digitPickerPreference.getCurrentNumber(), savedState.currentNumber);
    }

    /**
     * Tests the functionality of the onRestoreInstanceState-method.
     */
    public final void testOnRestoreInstanceState() {
        DigitPickerPreference digitPickerPreference = new DigitPickerPreference(getContext());
        digitPickerPreference.setPersistent(false);
        Parcelable parcelable = digitPickerPreference.onSaveInstanceState();
        DigitPickerPreference restoredDigitPickerPreference =
                new DigitPickerPreference(getContext());
        restoredDigitPickerPreference.onRestoreInstanceState(parcelable);
        assertEquals(digitPickerPreference.getCurrentNumber(),
                restoredDigitPickerPreference.getCurrentNumber());
    }

}