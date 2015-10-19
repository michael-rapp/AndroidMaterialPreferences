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
 * Tests the functionality of the class {@link SeekBarPreference}.
 *
 * @author Michael Rapp
 */
public class SeekBarPreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        int defaultDecimals = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_decimals);
        int defaultMaxValue = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_max_value);
        int defaultMinValue = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_min_value);
        int defaultStepSize = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_step_size);
        boolean defaultShowProgress = getContext().getResources()
                .getBoolean(R.bool.seek_bar_preference_default_show_progress);
        Context context = getContext();
        SeekBarPreference seekBarPreference = new SeekBarPreference(context);
        assertEquals(defaultDecimals, seekBarPreference.getDecimals());
        assertNull(seekBarPreference.getFloatingPointSeparator());
        assertEquals(defaultMaxValue, seekBarPreference.getMaxValue());
        assertEquals(defaultMinValue, seekBarPreference.getMinValue());
        assertEquals(defaultMaxValue - defaultMinValue, seekBarPreference.getRange());
        assertEquals(0.0f, seekBarPreference.getValue());
        assertEquals(0.0f, seekBarPreference.getCurrentValue());
        assertEquals(defaultStepSize, seekBarPreference.getStepSize());
        assertNull(seekBarPreference.getUnit());
        assertNull(seekBarPreference.getSummaries());
        assertNull(seekBarPreference.getSummary());
        assertEquals(defaultShowProgress, seekBarPreference.isProgressShown());
        assertEquals(context, seekBarPreference.getContext());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        int defaultDecimals = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_decimals);
        int defaultMaxValue = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_max_value);
        int defaultMinValue = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_min_value);
        int defaultStepSize = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_step_size);
        boolean defaultShowProgress = getContext().getResources()
                .getBoolean(R.bool.seek_bar_preference_default_show_progress);
        Context context = getContext();
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.seek_bar_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        SeekBarPreference seekBarPreference = new SeekBarPreference(context, attributeSet);
        assertEquals(defaultDecimals, seekBarPreference.getDecimals());
        assertNull(seekBarPreference.getFloatingPointSeparator());
        assertEquals(defaultMaxValue, seekBarPreference.getMaxValue());
        assertEquals(defaultMinValue, seekBarPreference.getMinValue());
        assertEquals(defaultMaxValue - defaultMinValue, seekBarPreference.getRange());
        assertEquals(0.0f, seekBarPreference.getValue());
        assertEquals(0.0f, seekBarPreference.getCurrentValue());
        assertEquals(defaultStepSize, seekBarPreference.getStepSize());
        assertNull(seekBarPreference.getUnit());
        assertNull(seekBarPreference.getSummaries());
        assertNull(seekBarPreference.getSummary());
        assertEquals(defaultShowProgress, seekBarPreference.isProgressShown());
        assertEquals(context, seekBarPreference.getContext());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        int defaultDecimals = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_decimals);
        int defaultMaxValue = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_max_value);
        int defaultMinValue = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_min_value);
        int defaultStepSize = getContext().getResources()
                .getInteger(R.integer.seek_bar_preference_default_step_size);
        boolean defaultShowProgress = getContext().getResources()
                .getBoolean(R.bool.seek_bar_preference_default_show_progress);
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.seek_bar_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        SeekBarPreference seekBarPreference =
                new SeekBarPreference(context, attributeSet, defaultStyle);
        assertEquals(defaultDecimals, seekBarPreference.getDecimals());
        assertNull(seekBarPreference.getFloatingPointSeparator());
        assertEquals(defaultMaxValue, seekBarPreference.getMaxValue());
        assertEquals(defaultMinValue, seekBarPreference.getMinValue());
        assertEquals(defaultMaxValue - defaultMinValue, seekBarPreference.getRange());
        assertEquals(0.0f, seekBarPreference.getValue());
        assertEquals(0.0f, seekBarPreference.getCurrentValue());
        assertEquals(defaultStepSize, seekBarPreference.getStepSize());
        assertNull(seekBarPreference.getUnit());
        assertNull(seekBarPreference.getSummaries());
        assertNull(seekBarPreference.getSummary());
        assertEquals(defaultShowProgress, seekBarPreference.isProgressShown());
        assertEquals(context, seekBarPreference.getContext());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set, a default style and a default style attribute as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleAndDefaultStyleAttributeParameters() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int defaultDecimals = getContext().getResources()
                    .getInteger(R.integer.seek_bar_preference_default_decimals);
            int defaultMaxValue = getContext().getResources()
                    .getInteger(R.integer.seek_bar_preference_default_max_value);
            int defaultMinValue = getContext().getResources()
                    .getInteger(R.integer.seek_bar_preference_default_min_value);
            int defaultStepSize = getContext().getResources()
                    .getInteger(R.integer.seek_bar_preference_default_step_size);
            boolean defaultShowProgress = getContext().getResources()
                    .getBoolean(R.bool.seek_bar_preference_default_show_progress);
            Context context = getContext();
            int defaultStyle = 0;
            int defaultStyleAttribute = 0;
            XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.seek_bar_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            SeekBarPreference seekBarPreference =
                    new SeekBarPreference(context, attributeSet, defaultStyle,
                            defaultStyleAttribute);
            assertEquals(defaultDecimals, seekBarPreference.getDecimals());
            assertNull(seekBarPreference.getFloatingPointSeparator());
            assertEquals(defaultMaxValue, seekBarPreference.getMaxValue());
            assertEquals(defaultMinValue, seekBarPreference.getMinValue());
            assertEquals(defaultMaxValue - defaultMinValue, seekBarPreference.getRange());
            assertEquals(0.0f, seekBarPreference.getValue());
            assertEquals(0.0f, seekBarPreference.getCurrentValue());
            assertEquals(defaultStepSize, seekBarPreference.getStepSize());
            assertNull(seekBarPreference.getUnit());
            assertNull(seekBarPreference.getSummaries());
            assertNull(seekBarPreference.getSummary());
            assertEquals(defaultShowProgress, seekBarPreference.isProgressShown());
            assertEquals(context, seekBarPreference.getContext());
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the current persisted value of the
     * preference.
     */
    public final void testSetValue() {
        float value = 33.3f;
        Context context = getContext();
        SeekBarPreference seekBarPreference = new SeekBarPreference(context);
        seekBarPreference.setValue(value);
        assertEquals(value, seekBarPreference.getValue());
    }

    /**
     * Tests, if the value is correctly rounded, when a value is set, which has more decimals than
     * allowed.
     */
    public final void testSetValueRoundsValue() {
        float value = 33.36f;
        Context context = getContext();
        SeekBarPreference seekBarPreference = new SeekBarPreference(context);
        seekBarPreference.setValue(value);
        assertEquals(33.4f, seekBarPreference.getValue());
    }

    /**
     * Tests, if the value is correctly adapted to the step size, when a value is set, which does
     * not match the step size.
     */
    public final void testSetValueAdaptsToStepSize() {
        float value = 33.36f;
        Context context = getContext();
        SeekBarPreference seekBarPreference = new SeekBarPreference(context);
        seekBarPreference.setStepSize(5);
        seekBarPreference.setValue(value);
        assertEquals(35.0f, seekBarPreference.getValue());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the current persisted value
     * is set to less than the minimum value.
     */
    public final void testSetValueLessThanMinValueThrowsException() {
        try {
            SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
            seekBarPreference.setValue(-1.0f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the current persisted value
     * is set to a greater value than the maximum value.
     */
    public final void testSetValueGreaterThanMaxValueThrowsException() {
        try {
            SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
            seekBarPreference.setValue(101.0f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the minimum value, the preference
     * should allow to choose.
     */
    public final void testSetMinValue() {
        int minValue = 10;
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setMinValue(minValue);
        assertEquals(minValue, seekBarPreference.getMinValue());
    }

    /**
     * Tests, if the preference's value is increased correctly, when the minimal value is set to a
     * value greater than the current persisted value.
     */
    public final void testSetMinValueIncreasesValue() {
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setMinValue(60);
        assertEquals(60.0f, seekBarPreference.getValue());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the minimum value is set to
     * less than 0.
     */
    public final void testSetMinValueLessThanZeroThrowsException() {
        try {
            SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
            seekBarPreference.setMinValue(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the minimum value is set to a
     * value equals or greater than the maximum value.
     */
    public final void testSetMinValueGreaterOrEqualsThanMaxValueThrowsException() {
        try {
            SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
            seekBarPreference.setMinValue(100);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the maximum value, the preference
     * should allow to choose.
     */
    public final void testSetMaxValue() {
        int maxValue = 150;
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setMaxValue(maxValue);
        assertEquals(maxValue, seekBarPreference.getMaxValue());
    }

    /**
     * Tests, if the preference's value is correctly decreased, when the maximum value is set to a
     * value less than the current persisted value.
     */
    public final void testSetMaxValueDecreasesValue() {
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setValue(50);
        seekBarPreference.setMaxValue(40);
        assertEquals(40.0f, seekBarPreference.getValue());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the maximum value is set to a
     * value less or equal than the minimum value.
     */
    public final void testSetMaxValueLessOrEqualThanMinValueThrowsException() {
        try {
            SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
            seekBarPreference.setMaxValue(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the step size, the value should be
     * increased or decreased by, when moving the seek bar.
     */
    public final void testSetStepSize() {
        int stepSize = 2;
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setStepSize(stepSize);
        assertEquals(stepSize, seekBarPreference.getStepSize());
    }

    /**
     * Tests, if the preference's value is adapted correctly when a step size is set.
     */
    public final void testSetStepSizeAdaptsValue() {
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setValue(50);
        seekBarPreference.setStepSize(3);
        assertEquals(51.0f, seekBarPreference.getValue());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the step size is set to a
     * greater value than the range.
     */
    public final void testSetStepSizeGreaterThanRangeThrowsException() {
        try {
            SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
            seekBarPreference.setStepSize(101);
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
            SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
            seekBarPreference.setStepSize(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the number of decimals of the
     * floating point numbers, the preference should allow to choose.
     */
    public final void testSetDecimals() {
        int decimals = 2;
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setDecimals(decimals);
        assertEquals(decimals, seekBarPreference.getDecimals());
    }

    /**
     * Tests, if the preference's value is rounded, when a number of decimals is set.
     */
    public final void testSetDecimalsRoundsValue() {
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setValue(50.5f);
        seekBarPreference.setDecimals(0);
        assertEquals(51.0f, seekBarPreference.getValue());
        seekBarPreference.setDecimals(2);
        assertEquals(51.00f, seekBarPreference.getValue());
        seekBarPreference.setValue(50.04f);
        seekBarPreference.setDecimals(1);
        assertEquals(50.0f, seekBarPreference.getValue());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the number of decimals is set
     * to a value less than 0.
     */
    public final void testSetDecimalsLessThanZeroThrowsException() {
        try {
            SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
            seekBarPreference.setDecimals(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the unit, which should be attached
     * to the current value for textual representation.
     */
    public final void testSetUnit() {
        String unit = "unit";
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setUnit(unit);
        assertEquals(unit, seekBarPreference.getUnit());
    }

    /**
     * Tests the functionality of the method, which allows to set the unit, which should be attached
     * to the current value for textual representation, and expects a resource id as a parameter.
     */
    public final void testSetUnitWithResourceIdParameter() {
        int resourceId = android.R.string.cancel;
        CharSequence unit = getContext().getText(resourceId);
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setUnit(resourceId);
        assertEquals(unit, seekBarPreference.getUnit());
    }

    /**
     * Tests the functionality of the method, which allows to set the symbol, which should be used
     * to separate floating point numbers for textual representation.
     */
    public final void testSetFloatingPointSeparator() {
        String floatingPointSeparator = ".";
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setFloatingPointSeparator(floatingPointSeparator);
        assertEquals(floatingPointSeparator, seekBarPreference.getFloatingPointSeparator());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown, if the floating point separator
     * is set to a string with more than one char.
     */
    public final void testSetFloatingPointSeparatorWithMoreThanOneCharThrowsException() {
        try {
            SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
            seekBarPreference.setFloatingPointSeparator("12");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set, whether the currently selected
     * value of the seek bar should be shown, or not.
     */
    public final void testShowProgress() {
        boolean showProgress = false;
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.showProgress(showProgress);
        assertEquals(showProgress, seekBarPreference.isProgressShown());
    }

    /**
     * Tests the functionality of the method, which allows to set the summaries, which should be
     * shown depending on the currently persisted value.
     */
    public final void testSetSummaries() {
        String[] summaries = new String[]{"summary1", "summary2"};
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setSummaries(summaries);
        assertEquals(summaries, seekBarPreference.getSummaries());
        seekBarPreference.setValue(0.0f);
        assertEquals(summaries[0], seekBarPreference.getSummary());
        seekBarPreference.setValue(49.9f);
        assertEquals(summaries[0], seekBarPreference.getSummary());
        seekBarPreference.setValue(50.0f);
        assertEquals(summaries[1], seekBarPreference.getSummary());
        seekBarPreference.setValue(100.0f);
        assertEquals(summaries[1], seekBarPreference.getSummary());
    }

    /**
     * Tests the functionality of the method, which allows to set the preference's summary.
     */
    public final void testSetSummary() {
        CharSequence summary = "summary";
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setSummary(summary);
        assertEquals(summary, seekBarPreference.getSummary());
        assertEquals(null, seekBarPreference.getSummaries());
    }

    /**
     * Tests the functionality of the method, which allows to retrieve the range of values, the
     * preference allows to choose from.
     */
    public final void testGetRange() {
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        assertEquals(100, seekBarPreference.getRange());
    }

    /**
     * Tests the functionality of the method, which allows to retrieve the preference's summary,
     * depending on whether the preference's value should be shown as its summary, or not.
     */
    public final void testGetSummary() {
        CharSequence summary = "summary";
        float value = 10.0f;
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setSummary(summary);
        seekBarPreference.setValue(value);
        seekBarPreference.setFloatingPointSeparator(",");
        assertEquals(summary, seekBarPreference.getSummary());
        seekBarPreference.showValueAsSummary(true);
        assertEquals("10,0", seekBarPreference.getSummary());
        seekBarPreference.setSummaries(new String[]{"summary1", "summary2"});
        seekBarPreference.showValueAsSummary(false);
        assertEquals("summary1", seekBarPreference.getSummary());
    }

    /**
     * Tests the functionality of the needInputMethod-method.
     */
    public final void testNeedInputMethod() {
        assertFalse(new SeekBarPreference(getContext()).needInputMethod());
    }

    /**
     * Tests the functionality of the onSaveInstanceState-method.
     */
    public final void testOnSaveInstanceState() {
        float value = 55.55f;
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setValue(value);
        seekBarPreference.setPersistent(false);
        SeekBarPreference.SavedState savedState =
                (SeekBarPreference.SavedState) seekBarPreference.onSaveInstanceState();
        assertEquals(seekBarPreference.getValue(), savedState.value);
        assertEquals(seekBarPreference.getCurrentValue(), savedState.currentValue);
    }

    /**
     * Tests the functionality of the onRestoreInstanceState-method.
     */
    public final void testRestoreInstanceState() {
        float value = 55.55f;
        SeekBarPreference seekBarPreference = new SeekBarPreference(getContext());
        seekBarPreference.setValue(value);
        seekBarPreference.setPersistent(false);
        Parcelable parcelable = seekBarPreference.onSaveInstanceState();
        SeekBarPreference restoredSeekBarPreference = new SeekBarPreference(getContext());
        restoredSeekBarPreference.onRestoreInstanceState(parcelable);
        assertEquals(seekBarPreference.getValue(), restoredSeekBarPreference.getValue());
        assertEquals(seekBarPreference.getCurrentValue(),
                restoredSeekBarPreference.getCurrentValue());
    }

}