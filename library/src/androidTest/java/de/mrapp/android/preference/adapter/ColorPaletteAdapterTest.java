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
package de.mrapp.android.preference.adapter;

import android.graphics.Color;
import android.test.AndroidTestCase;

import de.mrapp.android.preference.AbstractColorPickerPreference.PreviewShape;

/**
 * Tests the functionality of the class {@link ColorPaletteAdapter}.
 *
 * @author Michael Rapp
 */
public class ColorPaletteAdapterTest extends AndroidTestCase {

    /**
     * Tests the functionality of the getCount-method.
     */
    public final void testGetCount() {
        int[] colorPalette = new int[2];
        ColorPaletteAdapter colorPaletteAdapter =
                new ColorPaletteAdapter(getContext(), colorPalette, 2, PreviewShape.CIRCLE, 1,
                        Color.BLACK, null);
        assertEquals(colorPalette.length, colorPaletteAdapter.getCount());
    }

    /**
     * Tests the functionality of the getItem-method.
     */
    public final void testGetItem() {
        int[] colorPalette = new int[2];
        int item1 = 1;
        int item2 = 2;
        colorPalette[0] = item1;
        colorPalette[1] = item2;
        ColorPaletteAdapter colorPaletteAdapter =
                new ColorPaletteAdapter(getContext(), colorPalette, 2, PreviewShape.CIRCLE, 1,
                        Color.BLACK, null);
        assertEquals((Integer) item1, colorPaletteAdapter.getItem(0));
        assertEquals((Integer) item2, colorPaletteAdapter.getItem(1));
    }

    /**
     * Tests the functionality of the getItemId-method.
     */
    public final void testGetItemId() {
        ColorPaletteAdapter colorPaletteAdapter =
                new ColorPaletteAdapter(getContext(), new int[0], 2, PreviewShape.CIRCLE, 1,
                        Color.BLACK, null);
        int position = 2;
        assertEquals(position, colorPaletteAdapter.getItemId(position));
    }

    /**
     * Tests the functionality of the getView-method.
     */
    public final void testGetView() {
        int[] colorPalette = new int[1];
        int item = 1;
        colorPalette[0] = item;
        ColorPaletteAdapter colorPaletteAdapter =
                new ColorPaletteAdapter(getContext(), colorPalette, 2, PreviewShape.CIRCLE, 1,
                        Color.BLACK, null);
        assertNotNull(colorPaletteAdapter.getView(0, null, null));
    }

}