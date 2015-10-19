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