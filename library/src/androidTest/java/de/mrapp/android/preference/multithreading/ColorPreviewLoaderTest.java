/*
 * Copyright 2014 - 2017 Michael Rapp
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