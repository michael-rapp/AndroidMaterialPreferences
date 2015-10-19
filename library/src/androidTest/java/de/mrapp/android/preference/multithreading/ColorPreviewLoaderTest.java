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
package de.mrapp.android.preference.multithreading;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.test.AndroidTestCase;

import de.mrapp.android.preference.AbstractColorPickerPreference.PreviewShape;

import junit.framework.Assert;

/**
 * Tests the functionality of the class {@link ColorPreviewLoader}.
 *
 * @author Michael Rapp
 */
public class ColorPreviewLoaderTest extends AndroidTestCase {

    /**
     * Tests if all properties are set correctly by the constructor.
     */
    public final void testConstructor() {
        Drawable background = new ColorDrawable(Color.BLACK);
        PreviewShape shape = PreviewShape.CIRCLE;
        int size = 2;
        int borderWidth = 1;
        int borderColor = Color.BLACK;
        ColorPreviewLoader colorPreviewLoader =
                new ColorPreviewLoader(getContext(), background, shape, size, borderWidth,
                        borderColor);
        assertEquals(background, colorPreviewLoader.getBackground());
        assertEquals(shape, colorPreviewLoader.getShape());
        assertEquals(size, colorPreviewLoader.getSize());
        assertEquals(borderWidth, colorPreviewLoader.getBorderWidth());
        assertEquals(borderColor, colorPreviewLoader.getBorderColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the background of the preview.
     */
    public final void testSetBackground() {
        Drawable background = new ColorDrawable(Color.BLACK);
        ColorPreviewLoader colorPreviewLoader =
                new ColorPreviewLoader(getContext(), null, PreviewShape.CIRCLE, 1, 0, 0);
        colorPreviewLoader.setBackground(background);
        assertEquals(background, colorPreviewLoader.getBackground());
    }

    /**
     * Tests the functionality of the method, which allows to set the shape of the preview.
     */
    public final void testSetShape() {
        PreviewShape shape = PreviewShape.SQUARE;
        ColorPreviewLoader colorPreviewLoader =
                new ColorPreviewLoader(getContext(), null, PreviewShape.CIRCLE, 1, 0, 0);
        colorPreviewLoader.setShape(shape);
        assertEquals(shape, colorPreviewLoader.getShape());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * shape of the preview, if the shape is null.
     */
    public final void testSetShapeThrowsException() {
        try {
            ColorPreviewLoader colorPreviewLoader =
                    new ColorPreviewLoader(getContext(), null, PreviewShape.CIRCLE, 1, 0, 0);
            colorPreviewLoader.setShape(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the size of the preview.
     */
    public final void testSetSize() {
        int size = 2;
        ColorPreviewLoader colorPreviewLoader =
                new ColorPreviewLoader(getContext(), null, PreviewShape.CIRCLE, 1, 0, 0);
        colorPreviewLoader.setSize(size);
        assertEquals(size, colorPreviewLoader.getSize());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the size of the preview, if the size is less than 1.
     */
    public final void testSetSizeThrowsException() {
        try {
            ColorPreviewLoader colorPreviewLoader =
                    new ColorPreviewLoader(getContext(), null, PreviewShape.CIRCLE, 1, 0, 0);
            colorPreviewLoader.setSize(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the border width of the preview.
     */
    public final void testSetBorderWidth() {
        int borderWidth = 1;
        ColorPreviewLoader colorPreviewLoader =
                new ColorPreviewLoader(getContext(), null, PreviewShape.CIRCLE, 1, 0, 0);
        colorPreviewLoader.setBorderWidth(borderWidth);
        assertEquals(borderWidth, colorPreviewLoader.getBorderWidth());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the border width of the preview, if the border width is less than 0.
     */
    public final void testSetBorderWidthThrowsException() {
        try {
            ColorPreviewLoader colorPreviewLoader =
                    new ColorPreviewLoader(getContext(), null, PreviewShape.CIRCLE, 1, 0, 0);
            colorPreviewLoader.setBorderWidth(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the border color of the preview.
     */
    public final void testSetBorderColor() {
        int borderColor = Color.BLUE;
        ColorPreviewLoader colorPreviewLoader =
                new ColorPreviewLoader(getContext(), null, PreviewShape.CIRCLE, 1, 0, 0);
        colorPreviewLoader.setBorderColor(borderColor);
        assertEquals(borderColor, colorPreviewLoader.getBorderColor());
    }

}