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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.util.Xml;

import junit.framework.Assert;

import org.xmlpull.v1.XmlPullParser;

import de.mrapp.android.preference.AbstractColorPickerPreference.ColorFormat;
import de.mrapp.android.preference.AbstractColorPickerPreference.PreviewShape;

/**
 * Tests the functionality of the class {@link AbstractColorPickerPreference}.
 *
 * @author Michael Rapp
 */
public class AbstractColorPickerPreferenceTest extends AndroidTestCase {

    /**
     * Tests the functionality of the method, which allows to retrieve the value of a value of the
     * enum {@link PreviewShape}.
     */
    public final void testPreviewShapeGetValue() {
        assertEquals(0, PreviewShape.CIRCLE.getValue());
        assertEquals(1, PreviewShape.SQUARE.getValue());
    }

    /**
     * Tests the functionality of the method, which allows to retrieve the value of the enum {@link
     * PreviewShape}, which corresponds to a specific value.
     */
    public final void testPreviewShapeFromValue() {
        assertEquals(PreviewShape.CIRCLE, PreviewShape.fromValue(0));
        assertEquals(PreviewShape.SQUARE, PreviewShape.fromValue(1));
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * retrieve the value of the enum {@link PreviewShape}, which corresponds to a specific value,
     * if the value is invalid.
     */
    public final void testPreviewShapeFromValueThrowsException() {
        try {
            PreviewShape.fromValue(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to retrieve the value of a value of the
     * enum {@link ColorFormat}.
     */
    public final void testColorFormatGetValue() {
        assertEquals(0, ColorFormat.RGB.getValue());
        assertEquals(1, ColorFormat.ARGB.getValue());
        assertEquals(2, ColorFormat.HEX_3_BYTES.getValue());
        assertEquals(3, ColorFormat.HEX_4_BYTES.getValue());
    }

    /**
     * Tests the functionality of the method, which allows to retrieve the value of the enum {@link
     * ColorFormat}, which corresponds to a specific value.
     */
    public final void testColorFormatFromValue() {
        assertEquals(ColorFormat.RGB, ColorFormat.fromValue(0));
        assertEquals(ColorFormat.ARGB, ColorFormat.fromValue(1));
        assertEquals(ColorFormat.HEX_3_BYTES, ColorFormat.fromValue(2));
        assertEquals(ColorFormat.HEX_4_BYTES, ColorFormat.fromValue(3));
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * retrieve the value of the enum {@link ColorFormat}, which corresponds to a specific value, if
     * the value is invalid.
     */
    public final void testColorFormatFromValueThrowsException() {
        try {
            ColorFormat.fromValue(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        boolean defaultShowPreview = getContext().getResources()
                .getBoolean(R.bool.color_picker_preference_default_show_preview);
        int defaultPreviewSize = getContext().getResources()
                .getDimensionPixelSize(R.dimen.color_picker_preference_default_preview_size);
        PreviewShape defaultPreviewShape = PreviewShape.fromValue(getContext().getResources()
                .getInteger(R.integer.color_picker_preference_default_preview_shape));
        int defaultPreviewBorderWidth = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_picker_preference_default_preview_border_width);
        int defaultPreviewBorderColor = getContext().getResources()
                .getColor(R.color.color_picker_preference_default_preview_border_color);
        ColorFormat defaultColorFormat = ColorFormat.fromValue(getContext().getResources()
                .getInteger(R.integer.color_picker_preference_default_color_format));
        Context context = getContext();
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(context);
        assertEquals(context, colorPickerPreference.getContext());
        assertEquals(0, colorPickerPreference.getColor());
        assertEquals(defaultShowPreview, colorPickerPreference.isPreviewShown());
        assertEquals(defaultPreviewSize, colorPickerPreference.getPreviewSize());
        assertEquals(defaultPreviewShape, colorPickerPreference.getPreviewShape());
        assertEquals(defaultPreviewBorderWidth, colorPickerPreference.getPreviewBorderWidth());
        assertEquals(defaultPreviewBorderColor, colorPickerPreference.getPreviewBorderColor());
        assertNotNull(colorPickerPreference.getPreviewBackground());
        assertEquals(defaultColorFormat, colorPickerPreference.getColorFormat());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        boolean defaultShowPreview = getContext().getResources()
                .getBoolean(R.bool.color_picker_preference_default_show_preview);
        int defaultPreviewSize = getContext().getResources()
                .getDimensionPixelSize(R.dimen.color_picker_preference_default_preview_size);
        PreviewShape defaultPreviewShape = PreviewShape.fromValue(getContext().getResources()
                .getInteger(R.integer.color_picker_preference_default_preview_shape));
        int defaultPreviewBorderWidth = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_picker_preference_default_preview_border_width);
        int defaultPreviewBorderColor = getContext().getResources()
                .getColor(R.color.color_picker_preference_default_preview_border_color);
        ColorFormat defaultColorFormat = ColorFormat.fromValue(getContext().getResources()
                .getInteger(R.integer.color_picker_preference_default_color_format));
        Context context = getContext();
        XmlPullParser xmlPullParser =
                context.getResources().getXml(R.xml.abstract_color_picker_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(context, attributeSet);
        assertEquals(context, colorPickerPreference.getContext());
        assertEquals(0, colorPickerPreference.getColor());
        assertEquals(defaultShowPreview, colorPickerPreference.isPreviewShown());
        assertEquals(defaultPreviewSize, colorPickerPreference.getPreviewSize());
        assertEquals(defaultPreviewShape, colorPickerPreference.getPreviewShape());
        assertEquals(defaultPreviewBorderWidth, colorPickerPreference.getPreviewBorderWidth());
        assertEquals(defaultPreviewBorderColor, colorPickerPreference.getPreviewBorderColor());
        assertNotNull(colorPickerPreference.getPreviewBackground());
        assertEquals(defaultColorFormat, colorPickerPreference.getColorFormat());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        boolean defaultShowPreview = getContext().getResources()
                .getBoolean(R.bool.color_picker_preference_default_show_preview);
        int defaultPreviewSize = getContext().getResources()
                .getDimensionPixelSize(R.dimen.color_picker_preference_default_preview_size);
        PreviewShape defaultPreviewShape = PreviewShape.fromValue(getContext().getResources()
                .getInteger(R.integer.color_picker_preference_default_preview_shape));
        int defaultPreviewBorderWidth = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_picker_preference_default_preview_border_width);
        int defaultPreviewBorderColor = getContext().getResources()
                .getColor(R.color.color_picker_preference_default_preview_border_color);
        ColorFormat defaultColorFormat = ColorFormat.fromValue(getContext().getResources()
                .getInteger(R.integer.color_picker_preference_default_color_format));
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser =
                context.getResources().getXml(R.xml.abstract_color_picker_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(context, attributeSet,
                        defaultStyle);
        assertEquals(context, colorPickerPreference.getContext());
        assertEquals(0, colorPickerPreference.getColor());
        assertEquals(defaultShowPreview, colorPickerPreference.isPreviewShown());
        assertEquals(defaultPreviewSize, colorPickerPreference.getPreviewSize());
        assertEquals(defaultPreviewShape, colorPickerPreference.getPreviewShape());
        assertEquals(defaultPreviewBorderWidth, colorPickerPreference.getPreviewBorderWidth());
        assertEquals(defaultPreviewBorderColor, colorPickerPreference.getPreviewBorderColor());
        assertNotNull(colorPickerPreference.getPreviewBackground());
        assertEquals(defaultColorFormat, colorPickerPreference.getColorFormat());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set, a default style and a default style attribute as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleAndDefaultStyleAttributeParameters() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean defaultShowPreview = getContext().getResources()
                    .getBoolean(R.bool.color_picker_preference_default_show_preview);
            int defaultPreviewSize = getContext().getResources()
                    .getDimensionPixelSize(R.dimen.color_picker_preference_default_preview_size);
            PreviewShape defaultPreviewShape = PreviewShape.fromValue(getContext().getResources()
                    .getInteger(R.integer.color_picker_preference_default_preview_shape));
            int defaultPreviewBorderWidth = getContext().getResources().getDimensionPixelSize(
                    R.dimen.color_picker_preference_default_preview_border_width);
            int defaultPreviewBorderColor = getContext().getResources()
                    .getColor(R.color.color_picker_preference_default_preview_border_color);
            ColorFormat defaultColorFormat = ColorFormat.fromValue(getContext().getResources()
                    .getInteger(R.integer.color_picker_preference_default_color_format));
            Context context = getContext();
            int defaultStyle = 0;
            int defaultStyleAttribute = 0;
            XmlPullParser xmlPullParser =
                    context.getResources().getXml(R.xml.abstract_color_picker_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            AbstractColorPickerPreference colorPickerPreference =
                    new AbstractColorPickerPreferenceImplementation(context, attributeSet,
                            defaultStyle, defaultStyleAttribute);
            assertEquals(context, colorPickerPreference.getContext());
            assertEquals(0, colorPickerPreference.getColor());
            assertEquals(defaultShowPreview, colorPickerPreference.isPreviewShown());
            assertEquals(defaultPreviewSize, colorPickerPreference.getPreviewSize());
            assertEquals(defaultPreviewShape, colorPickerPreference.getPreviewShape());
            assertEquals(defaultPreviewBorderWidth, colorPickerPreference.getPreviewBorderWidth());
            assertEquals(defaultPreviewBorderColor, colorPickerPreference.getPreviewBorderColor());
            assertNotNull(colorPickerPreference.getPreviewBackground());
            assertEquals(defaultColorFormat, colorPickerPreference.getColorFormat());
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the preference's color.
     */
    public final void testSetColor() {
        int color = Color.RED;
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setColor(color);
        assertEquals(color, colorPickerPreference.getColor());
    }

    /**
     * Tests the functionality of the method, which allows to set, whether a preview of the
     * preference's color should be shown, or not.
     */
    public final void testShowPreview() {
        boolean showPreview = false;
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.showPreview(showPreview);
        assertEquals(showPreview, colorPickerPreference.isPreviewShown());
    }

    /**
     * Tests the functionality of the method, which allows to set the size of the preview of the
     * preference's color.
     */
    public final void testSetPreviewSize() {
        int previewSize = 1;
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setPreviewSize(previewSize);
        assertEquals(previewSize, colorPickerPreference.getPreviewSize());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the size of the preview of the preference's color, if the size is less than 1.
     */
    public final void testSetPreviewSizeThrowsException() {
        try {
            AbstractColorPickerPreference colorPickerPreference =
                    new AbstractColorPickerPreferenceImplementation(getContext());
            colorPickerPreference.setPreviewSize(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the shape of the preview of the
     * preference's color.
     */
    public final void testSetPreviewShape() {
        PreviewShape previewShape = PreviewShape.SQUARE;
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setPreviewShape(previewShape);
        assertEquals(previewShape, colorPickerPreference.getPreviewShape());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * shape of the preview of the preference's color, if the shape is null.
     */
    public final void testSetPreviewShapeThrowsException() {
        try {
            AbstractColorPickerPreference colorPickerPreference =
                    new AbstractColorPickerPreferenceImplementation(getContext());
            colorPickerPreference.setPreviewShape(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the border width of the preview of
     * the preference's color.
     */
    public final void testSetPreviewBorderWidth() {
        int previewBorderWidth = 0;
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setPreviewBorderWidth(previewBorderWidth);
        assertEquals(previewBorderWidth, colorPickerPreference.getPreviewBorderWidth());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the border width of the preview of the preference's color, if the border width is less
     * than 0.
     */
    public final void testSetPreviewBorderWidthThrowsException() {
        try {
            AbstractColorPickerPreference colorPickerPreference =
                    new AbstractColorPickerPreferenceImplementation(getContext());
            colorPickerPreference.setPreviewBorderWidth(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the border color of the preview of
     * the preference's color.
     */
    public final void testSetPreviewBorderColor() {
        int previewBorderColor = Color.RED;
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setPreviewBorderColor(previewBorderColor);
        assertEquals(previewBorderColor, colorPickerPreference.getPreviewBorderColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the background of the preview of
     * the preference's color, and expects a {@link Drawable} as a parameter.
     */
    public final void testSetPreviewBackground() {
        Drawable previewBackground = new ColorDrawable(Color.RED);
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setPreviewBackground(previewBackground);
        assertEquals(previewBackground, colorPickerPreference.getPreviewBackground());
    }

    /**
     * Tests the functionality of the method, which allows to set the background of the preview of
     * the preference's color, and expects a resource id as a parameter.
     */
    public final void testSetPreviewBackgroundWithResourceIdParameter() {
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setPreviewBackground(android.R.drawable.ic_dialog_info);
        assertNotNull(colorPickerPreference.getPreviewBackground());
    }

    /**
     * Tests the functionality of the method, which allows to set the background color of the
     * preview of the preference's color.
     */
    public final void testSetPreviewBackgroundColor() {
        int backgroundColor = Color.RED;
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setPreviewBackgroundColor(backgroundColor);
        ColorDrawable colorDrawable = (ColorDrawable) colorPickerPreference.getPreviewBackground();
        assertEquals(backgroundColor, colorDrawable.getColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the format, which should be used
     * to print a textual representation of the preference's color.
     */
    public final void testSetColorFormat() {
        ColorFormat colorFormat = ColorFormat.ARGB;
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setColorFormat(colorFormat);
        assertEquals(colorFormat, colorPickerPreference.getColorFormat());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * format, which should be used to print a textual representation of the preference's color, if
     * the format is null.
     */
    public final void testSetColorFormatThrowsException() {
        try {
            AbstractColorPickerPreference colorPickerPreference =
                    new AbstractColorPickerPreferenceImplementation(getContext());
            colorPickerPreference.setColorFormat(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the getSummary-method.
     */
    public final void testGetSummary() {
        CharSequence summary = "summary";
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setSummary(summary);
        colorPickerPreference.showValueAsSummary(true);
        int alpha = 1;
        int red = 2;
        int green = 3;
        int blue = 4;
        colorPickerPreference.setColor(Color.argb(alpha, red, green, blue));
        colorPickerPreference.setColorFormat(ColorFormat.RGB);
        assertEquals("R = " + red + ", G = " + green + ", B = " + blue,
                colorPickerPreference.getSummary());
        colorPickerPreference.setColorFormat(ColorFormat.ARGB);
        assertEquals("A = " + alpha + ", R = " + red + ", G = " + green + ", B = " + blue,
                colorPickerPreference.getSummary());
        colorPickerPreference.setColor(Color.parseColor("#11223344"));
        colorPickerPreference.setColorFormat(ColorFormat.HEX_3_BYTES);
        assertEquals("#223344", colorPickerPreference.getSummary());
        colorPickerPreference.setColorFormat(ColorFormat.HEX_4_BYTES);
        assertEquals("#11223344", colorPickerPreference.getSummary());
        colorPickerPreference.showValueAsSummary(false);
        assertEquals(summary, colorPickerPreference.getSummary());
    }

    /**
     * Tests the functionality of the onSaveInstanceState-method.
     */
    public final void testOnSaveInstanceState() {
        int color = Color.RED;
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setColor(color);
        colorPickerPreference.setPersistent(false);
        AbstractColorPickerPreference.SavedState savedState =
                (AbstractColorPickerPreference.SavedState) colorPickerPreference
                        .onSaveInstanceState();
        assertEquals(color, savedState.color);
    }

    /**
     * Tests the functionality of the onRestoreInstanceState-method.
     */
    public final void testOnRestoreInstanceState() {
        int color = Color.RED;
        AbstractColorPickerPreference colorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        colorPickerPreference.setColor(color);
        colorPickerPreference.setPersistent(false);
        Parcelable parcelable = colorPickerPreference.onSaveInstanceState();
        AbstractColorPickerPreference restoredColorPickerPreference =
                new AbstractColorPickerPreferenceImplementation(getContext());
        restoredColorPickerPreference.onRestoreInstanceState(parcelable);
        assertEquals(color, restoredColorPickerPreference.getColor());
    }

}