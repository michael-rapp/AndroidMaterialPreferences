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
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.util.Xml;

import junit.framework.Assert;

import org.xmlpull.v1.XmlPullParser;

import de.mrapp.android.preference.AbstractColorPickerPreference.PreviewShape;

/**
 * Tests the functionality of the class {@link ColorPalettePreference}.
 *
 * @author Michael Rapp
 */
public class ColorPalettePreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    @SuppressWarnings("deprecation")
    public final void testConstructorWithContextParameter() {
        int defaultDialogPreviewSize = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_palette_preference_default_dialog_preview_size);
        PreviewShape defaultDialogPreviewShape = PreviewShape.fromValue(getContext().getResources()
                .getInteger(R.integer.color_palette_preference_default_dialog_preview_shape));
        int defaultDialogPreviewBorderWidth = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_palette_preference_default_dialog_preview_border_width);
        int defaultDialogPreviewBorderColor = getContext().getResources()
                .getColor(R.color.color_palette_preference_default_dialog_preview_border_color);
        int defaultNumberOfColumns = getContext().getResources()
                .getInteger(R.integer.color_palette_preference_default_number_of_columns);
        Context context = getContext();
        ColorPalettePreference colorPalettePreference = new ColorPalettePreference(context);
        assertEquals(context, colorPalettePreference.getContext());
        assertNotNull(colorPalettePreference.getColorPalette());
        assertEquals(0, colorPalettePreference.getColorPalette().length);
        assertEquals(defaultDialogPreviewSize, colorPalettePreference.getDialogPreviewSize());
        assertEquals(defaultDialogPreviewShape, colorPalettePreference.getDialogPreviewShape());
        assertEquals(defaultDialogPreviewBorderWidth,
                colorPalettePreference.getDialogPreviewBorderWidth());
        assertEquals(defaultDialogPreviewBorderColor,
                colorPalettePreference.getDialogPreviewBorderColor());
        assertNotNull(colorPalettePreference.getDialogPreviewBackground());
        assertEquals(defaultNumberOfColumns, colorPalettePreference.getNumberOfColumns());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    @SuppressWarnings("deprecation")
    public final void testConstructorWithContextAndAttributeSetParameters() {
        int defaultDialogPreviewSize = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_palette_preference_default_dialog_preview_size);
        PreviewShape defaultDialogPreviewShape = PreviewShape.fromValue(getContext().getResources()
                .getInteger(R.integer.color_palette_preference_default_dialog_preview_shape));
        int defaultDialogPreviewBorderWidth = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_palette_preference_default_dialog_preview_border_width);
        int defaultDialogPreviewBorderColor = getContext().getResources()
                .getColor(R.color.color_palette_preference_default_dialog_preview_border_color);
        int defaultNumberOfColumns = getContext().getResources()
                .getInteger(R.integer.color_palette_preference_default_number_of_columns);
        Context context = getContext();
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.color_palette_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        ColorPalettePreference colorPalettePreference =
                new ColorPalettePreference(context, attributeSet);
        assertEquals(context, colorPalettePreference.getContext());
        assertNotNull(colorPalettePreference.getColorPalette());
        assertEquals(0, colorPalettePreference.getColorPalette().length);
        assertEquals(defaultDialogPreviewSize, colorPalettePreference.getDialogPreviewSize());
        assertEquals(defaultDialogPreviewShape, colorPalettePreference.getDialogPreviewShape());
        assertEquals(defaultDialogPreviewBorderWidth,
                colorPalettePreference.getDialogPreviewBorderWidth());
        assertEquals(defaultDialogPreviewBorderColor,
                colorPalettePreference.getDialogPreviewBorderColor());
        assertNotNull(colorPalettePreference.getDialogPreviewBackground());
        assertEquals(defaultNumberOfColumns, colorPalettePreference.getNumberOfColumns());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    @SuppressWarnings("deprecation")
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        int defaultDialogPreviewSize = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_palette_preference_default_dialog_preview_size);
        PreviewShape defaultDialogPreviewShape = PreviewShape.fromValue(getContext().getResources()
                .getInteger(R.integer.color_palette_preference_default_dialog_preview_shape));
        int defaultDialogPreviewBorderWidth = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_palette_preference_default_dialog_preview_border_width);
        int defaultDialogPreviewBorderColor = getContext().getResources()
                .getColor(R.color.color_palette_preference_default_dialog_preview_border_color);
        int defaultNumberOfColumns = getContext().getResources()
                .getInteger(R.integer.color_palette_preference_default_number_of_columns);
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.color_palette_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        ColorPalettePreference colorPalettePreference =
                new ColorPalettePreference(context, attributeSet, defaultStyle);
        assertEquals(context, colorPalettePreference.getContext());
        assertNotNull(colorPalettePreference.getColorPalette());
        assertEquals(0, colorPalettePreference.getColorPalette().length);
        assertEquals(defaultDialogPreviewSize, colorPalettePreference.getDialogPreviewSize());
        assertEquals(defaultDialogPreviewShape, colorPalettePreference.getDialogPreviewShape());
        assertEquals(defaultDialogPreviewBorderWidth,
                colorPalettePreference.getDialogPreviewBorderWidth());
        assertEquals(defaultDialogPreviewBorderColor,
                colorPalettePreference.getDialogPreviewBorderColor());
        assertNotNull(colorPalettePreference.getDialogPreviewBackground());
        assertEquals(defaultNumberOfColumns, colorPalettePreference.getNumberOfColumns());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set, a default style and a default style attribute as parameters.
     */
    @SuppressWarnings("deprecation")
    public final void testConstructorWithContextAttributeSetAndDefaultStyleAndDefaultStyleAttributeParameters() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int defaultDialogPreviewSize = getContext().getResources().getDimensionPixelSize(
                    R.dimen.color_palette_preference_default_dialog_preview_size);
            PreviewShape defaultDialogPreviewShape = PreviewShape.fromValue(
                    getContext().getResources().getInteger(
                            R.integer.color_palette_preference_default_dialog_preview_shape));
            int defaultDialogPreviewBorderWidth = getContext().getResources().getDimensionPixelSize(
                    R.dimen.color_palette_preference_default_dialog_preview_border_width);
            int defaultDialogPreviewBorderColor = getContext().getResources()
                    .getColor(R.color.color_palette_preference_default_dialog_preview_border_color);
            int defaultNumberOfColumns = getContext().getResources()
                    .getInteger(R.integer.color_palette_preference_default_number_of_columns);
            Context context = getContext();
            int defaultStyle = 0;
            int defaultStyleAttribute = 0;
            XmlPullParser xmlPullParser =
                    context.getResources().getXml(R.xml.color_palette_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            ColorPalettePreference colorPalettePreference =
                    new ColorPalettePreference(context, attributeSet, defaultStyle,
                            defaultStyleAttribute);
            assertEquals(context, colorPalettePreference.getContext());
            assertNotNull(colorPalettePreference.getColorPalette());
            assertEquals(0, colorPalettePreference.getColorPalette().length);
            assertEquals(defaultDialogPreviewSize, colorPalettePreference.getDialogPreviewSize());
            assertEquals(defaultDialogPreviewShape, colorPalettePreference.getDialogPreviewShape());
            assertEquals(defaultDialogPreviewBorderWidth,
                    colorPalettePreference.getDialogPreviewBorderWidth());
            assertEquals(defaultDialogPreviewBorderColor,
                    colorPalettePreference.getDialogPreviewBorderColor());
            assertNotNull(colorPalettePreference.getDialogPreviewBackground());
            assertEquals(defaultNumberOfColumns, colorPalettePreference.getNumberOfColumns());
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the colors, the preference should
     * allow to choose.
     */
    public final void testSetColorPalette() {
        int[] colorPalette = new int[0];
        ColorPalettePreference colorPalettePreference = new ColorPalettePreference(getContext());
        colorPalettePreference.setColorPalette(colorPalette);
        assertEquals(colorPalette, colorPalettePreference.getColorPalette());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * colors, the preference should allow to choose, if the colors are null.
     */
    public final void testSetColorPaletteThrowsException() {
        try {
            ColorPalettePreference colorPalettePreference =
                    new ColorPalettePreference(getContext());
            colorPalettePreference.setColorPalette(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the colors, the preference should
     * allow to choose, and expects a resource id as a parameter.
     */
    public final void testSetColorPaletteWithResourceIdParameter() {
        int resourceId = R.array.color_palette;
        int[] colorPalette = getContext().getResources().getIntArray(resourceId);
        ColorPalettePreference colorPalettePreference = new ColorPalettePreference(getContext());
        colorPalettePreference.setColorPalette(resourceId);
        assertEquals(colorPalette.length, colorPalettePreference.getColorPalette().length);
        assertEquals(colorPalette[0], colorPalettePreference.getColorPalette()[0]);
        assertEquals(colorPalette[1], colorPalettePreference.getColorPalette()[1]);
    }

    /**
     * Tests the functionality of the method, which allows to set the size, which should be used to
     * preview colors in the preference's dialog.
     */
    public final void testSetDialogPreviewSize() {
        int dialogPreviewSize = 1;
        ColorPalettePreference colorPalettePreference = new ColorPalettePreference(getContext());
        colorPalettePreference.setDialogPreviewSize(dialogPreviewSize);
        assertEquals(dialogPreviewSize, colorPalettePreference.getDialogPreviewSize());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the size, which should be used to preview colors in the preference's dialog, if the size
     * is less than 1.
     */
    public final void testSetDialogPreviewSizeThrowsException() {
        try {
            ColorPalettePreference colorPalettePreference =
                    new ColorPalettePreference(getContext());
            colorPalettePreference.setDialogPreviewSize(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the shape, which should be used to
     * preview colors in the preference's dialog.
     */
    public final void testSetDialogPreviewShape() {
        PreviewShape dialogPreviewShape = PreviewShape.SQUARE;
        ColorPalettePreference colorPalettePreference = new ColorPalettePreference(getContext());
        colorPalettePreference.setDialogPreviewShape(dialogPreviewShape);
        assertEquals(dialogPreviewShape, colorPalettePreference.getDialogPreviewShape());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * shape, which should be used to preview colors in the preference's dialog, if the shape is
     * null.
     */
    public final void testSetDialogPreviewShapeThrowsException() {
        try {
            ColorPalettePreference colorPalettePreference =
                    new ColorPalettePreference(getContext());
            colorPalettePreference.setDialogPreviewShape(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the border width, which should be
     * used to preview colors in the preference's dialog.
     */
    public final void testSetDialogPreviewBorderWidth() {
        int dialogPreviewBorderWidth = 0;
        ColorPalettePreference colorPalettePreference = new ColorPalettePreference(getContext());
        colorPalettePreference.setDialogPreviewBorderWidth(dialogPreviewBorderWidth);
        assertEquals(dialogPreviewBorderWidth,
                colorPalettePreference.getDialogPreviewBorderWidth());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the border width, which should be used to preview colors in the preference's dialog, if
     * the border width is less than 0.
     */
    public final void testSetDialogPreviewBorderWidthThrowsException() {
        try {
            ColorPalettePreference colorPalettePreference =
                    new ColorPalettePreference(getContext());
            colorPalettePreference.setDialogPreviewBorderWidth(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the border color, which should be
     * used to preview colors in the preference's dialog.
     */
    public final void testSetDialogPreviewBorderColor() {
        int dialogPreviewBorderColor = Color.RED;
        ColorPalettePreference colorPalettePreference = new ColorPalettePreference(getContext());
        colorPalettePreference.setDialogPreviewBorderColor(dialogPreviewBorderColor);
        assertEquals(dialogPreviewBorderColor,
                colorPalettePreference.getDialogPreviewBorderColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the background, which should be
     * used to preview colors in the preference's dialog.
     */
    public final void testSetDialogPreviewBackground() {
        Drawable dialogPreviewBackground = new ColorDrawable(Color.RED);
        ColorPalettePreference colorPalettePreference = new ColorPalettePreference(getContext());
        colorPalettePreference.setDialogPreviewBackground(dialogPreviewBackground);
        assertEquals(dialogPreviewBackground, colorPalettePreference.getDialogPreviewBackground());
    }

    /**
     * Tests the functionality of the method, which allows to set the background, which should be
     * used to preview colors in the preference's dialog, and expects a resource id as a parameter.
     */
    public final void testSetDialogPreviewBackgroundWithResourceIdParameter() {
        ColorPalettePreference colorPalettePreference = new ColorPalettePreference(getContext());
        colorPalettePreference.setDialogPreviewBackground(android.R.drawable.ic_dialog_info);
        assertNotNull(colorPalettePreference.getDialogPreviewBackground());
    }

    /**
     * Tests the functionality of the method, which allows to set the background color, which should
     * be used to preview colors in the preference's dialog.
     */
    public final void testSetDialogPreviewBackgroundColor() {
        int backgroundColor = Color.RED;
        ColorPalettePreference colorPalettePreference = new ColorPalettePreference(getContext());
        colorPalettePreference.setDialogPreviewBackgroundColor(backgroundColor);
        ColorDrawable colorDrawable =
                (ColorDrawable) colorPalettePreference.getDialogPreviewBackground();
        assertEquals(backgroundColor, colorDrawable.getColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the number of columns, which
     * should be used to preview colors in the preference's dialog.
     */
    public final void testSetNumberOfColumns() {
        int numberOfColumns = 1;
        ColorPalettePreference colorPalettePreference = new ColorPalettePreference(getContext());
        colorPalettePreference.setNumberOfColumns(numberOfColumns);
        assertEquals(numberOfColumns, colorPalettePreference.getNumberOfColumns());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the number of columns, which should be used to preview colors in the preference's dialog,
     * if the number of columns is less than 1.
     */
    public final void testSetNumberOfColumnsThrowsException() {
        try {
            ColorPalettePreference colorPalettePreference =
                    new ColorPalettePreference(getContext());
            colorPalettePreference.setNumberOfColumns(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the needInputMethod-method.
     */
    public final void testNeedInputMethod() {
        assertFalse(new ColorPalettePreference(getContext()).needInputMethod());
    }

}