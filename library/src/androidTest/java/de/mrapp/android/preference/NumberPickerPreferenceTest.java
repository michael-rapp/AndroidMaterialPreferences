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

/**
 * Tests the functionality of the class {@link NumberPickerPreference}.
 *
 * @author Michael Rapp
 */
public class NumberPickerPreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        int defaultMinNumber = getContext().getResources()
                .getInteger(R.integer.number_picker_preference_default_min_number);
        int defaultMaxNumber = getContext().getResources()
                .getInteger(R.integer.number_picker_preference_default_max_number);
        Context context = getContext();
        NumberPickerPreference numberPickerPreference = new NumberPickerPreference(context);
        assertEquals(defaultMinNumber, numberPickerPreference.getMinNumber());
        assertEquals(defaultMaxNumber, numberPickerPreference.getMaxNumber());
        assertEquals(defaultMaxNumber - defaultMinNumber, numberPickerPreference.getRange());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        int defaultMinNumber = getContext().getResources()
                .getInteger(R.integer.number_picker_preference_default_min_number);
        int defaultMaxNumber = getContext().getResources()
                .getInteger(R.integer.number_picker_preference_default_max_number);
        Context context = getContext();
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.number_picker_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        NumberPickerPreference numberPickerPreference =
                new NumberPickerPreference(context, attributeSet);
        assertEquals(defaultMinNumber, numberPickerPreference.getMinNumber());
        assertEquals(defaultMaxNumber, numberPickerPreference.getMaxNumber());
        assertEquals(defaultMaxNumber - defaultMinNumber, numberPickerPreference.getRange());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        int defaultMinNumber = getContext().getResources()
                .getInteger(R.integer.number_picker_preference_default_min_number);
        int defaultMaxNumber = getContext().getResources()
                .getInteger(R.integer.number_picker_preference_default_max_number);
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.number_picker_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        NumberPickerPreference numberPickerPreference =
                new NumberPickerPreference(context, attributeSet, defaultStyle);
        assertEquals(defaultMinNumber, numberPickerPreference.getMinNumber());
        assertEquals(defaultMaxNumber, numberPickerPreference.getMaxNumber());
        assertEquals(defaultMaxNumber - defaultMinNumber, numberPickerPreference.getRange());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set, a default style and a default style attribute as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleAndDefaultStyleAttributeParameters() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int defaultMinNumber = getContext().getResources()
                    .getInteger(R.integer.number_picker_preference_default_min_number);
            int defaultMaxNumber = getContext().getResources()
                    .getInteger(R.integer.number_picker_preference_default_max_number);
            Context context = getContext();
            int defaultStyle = 0;
            int defaultStyleAttribute = 0;
            XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.list_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            NumberPickerPreference numberPickerPreference =
                    new NumberPickerPreference(context, attributeSet, defaultStyle,
                            defaultStyleAttribute);
            assertEquals(defaultMinNumber, numberPickerPreference.getMinNumber());
            assertEquals(defaultMaxNumber, numberPickerPreference.getMaxNumber());
            assertEquals(defaultMaxNumber - defaultMinNumber, numberPickerPreference.getRange());
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the current persisted number of
     * the preference.
     */
    public final void testSetNumber() {
        int number = 8;
        NumberPickerPreference numberPickerPreference = new NumberPickerPreference(getContext());
        numberPickerPreference.setNumber(number);
        assertEquals(number, numberPickerPreference.getNumber());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the current persisted value
     * is set to less than the minimum value.
     */
    public final void testSetNumberLessThanMinNumberThrowsException() {
        try {
            NumberPickerPreference numberPickerPreference =
                    new NumberPickerPreference(getContext());
            numberPickerPreference.setNumber(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the current persisted value
     * is set to a greater value than the maximum value.
     */
    public final void testSetNumberGreaterThanMaxNumberThrowsException() {
        try {
            NumberPickerPreference numberPickerPreference =
                    new NumberPickerPreference(getContext());
            numberPickerPreference.setNumber(101);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests, if the number is correctly adapted to the step size, when a number is set, which does
     * not match the step size.
     */
    public final void testSetNumberAdaptsToStepSize() {
        NumberPickerPreference numberPickerPreference = new NumberPickerPreference(getContext());
        numberPickerPreference.setStepSize(3);
        numberPickerPreference.setNumber(2);
        assertEquals(3, numberPickerPreference.getNumber());
    }

    /**
     * Tests the functionality of the method, which allows to set the minimum number, the preference
     * allows to choose.
     */
    public final void testSetMinNumber() {
        int minNumber = 1;
        NumberPickerPreference numberPickerPreference = new NumberPickerPreference(getContext());
        numberPickerPreference.setMinNumber(minNumber);
        assertEquals(minNumber, numberPickerPreference.getMinNumber());
        assertEquals(minNumber, numberPickerPreference.getNumber());
    }

    /**
     * Tests, if the preference's number is increased correctly, when the minimal number is set to a
     * number greater than the current persisted number.
     */
    public final void testSetMinNumberIncreasesNumber() {
        NumberPickerPreference numberPickerPreference = new NumberPickerPreference(getContext());
        numberPickerPreference.setMinNumber(6);
        assertEquals(6, numberPickerPreference.getNumber());
    }

    /**
     * Tests the functionality of the method, which allows to set the maximum number, the preference
     * allows to choose.
     */
    public final void testSetMaxNumber() {
        int maxNumber = 2;
        NumberPickerPreference numberPickerPreference = new NumberPickerPreference(getContext());
        numberPickerPreference.setMaxNumber(maxNumber);
        assertEquals(maxNumber, numberPickerPreference.getMaxNumber());
    }

    /**
     * Tests, if the preference's value is correctly decreased, when the maximum value is set to a
     * value less than the current persisted value.
     */
    public final void testSetMaxNumberDecreasesNumber() {
        NumberPickerPreference numberPickerPreference = new NumberPickerPreference(getContext());
        numberPickerPreference.setNumber(5);
        numberPickerPreference.setMaxNumber(4);
        assertEquals(4, numberPickerPreference.getNumber());
    }

    /**
     * Tests the functionality of the method, which allows to set the step size, the number should
     * be increased or decreased by, when moving the selector wheel.
     */
    public final void testSetStepSize() {
        int stepSize = 2;
        NumberPickerPreference numberPickerPreference = new NumberPickerPreference(getContext());
        numberPickerPreference.setStepSize(stepSize);
        assertEquals(stepSize, numberPickerPreference.getStepSize());
    }

    /**
     * Tests, if the preference's number is adapted correctly when a step size is set.
     */
    public final void testSetStepSizeAdaptsNumber() {
        NumberPickerPreference numberPickerPreference = new NumberPickerPreference(getContext());
        numberPickerPreference.setNumber(5);
        numberPickerPreference.setStepSize(3);
        assertEquals(6, numberPickerPreference.getNumber());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the step size is set to a
     * greater value than the range.
     */
    public final void testSetStepSizeGreaterThanRangeThrowsException() {
        try {
            NumberPickerPreference numberPickerPreference =
                    new NumberPickerPreference(getContext());
            numberPickerPreference.setStepSize(11);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the step size is set to a
     * value less than 1.
     */
    public final void testSetStepSizeLessThanOneThrowsException() {
        try {
            NumberPickerPreference numberPickerPreference =
                    new NumberPickerPreference(getContext());
            numberPickerPreference.setStepSize(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the onSaveInstanceState-method.
     */
    public final void testOnSaveInstanceState() {
        NumberPickerPreference numberPickerPreference = new NumberPickerPreference(getContext());
        numberPickerPreference.setPersistent(false);
        NumberPickerPreference.SavedState savedState =
                (NumberPickerPreference.SavedState) numberPickerPreference.onSaveInstanceState();
        assertEquals(numberPickerPreference.getCurrentIndex(), savedState.currentNumber);
    }

    /**
     * Tests the functionality of the onRestoreInstanceState-method.
     */
    public final void testOnRestoreInstanceState() {
        NumberPickerPreference numberPickerPreference = new NumberPickerPreference(getContext());
        numberPickerPreference.setPersistent(false);
        Parcelable parcelable = numberPickerPreference.onSaveInstanceState();
        NumberPickerPreference restoredNumberPickerPreference =
                new NumberPickerPreference(getContext());
        restoredNumberPickerPreference.onRestoreInstanceState(parcelable);
        assertEquals(numberPickerPreference.getCurrentIndex(),
                restoredNumberPickerPreference.getCurrentIndex());
    }

}