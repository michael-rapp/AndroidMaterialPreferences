/*
 * Copyright 2014 - 2016 Michael Rapp
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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

/**
 * Tests the functionality of the class {@link AbstractDialogPreference}.
 *
 * @author Michael Rapp
 */
public class AbstractDialogPreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        Context context = getContext();
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(context);
        assertNull(dialogPreference.getDialogTitle());
        assertNull(dialogPreference.getDialogIcon());
        assertNull(dialogPreference.getDialogMessage());
        assertNull(dialogPreference.getDialog());
        assertEquals(-1, dialogPreference.getDialogTitleColor());
        assertEquals(-1, dialogPreference.getDialogMessageColor());
        assertEquals(-1, dialogPreference.getDialogButtonTextColor());
        assertNull(dialogPreference.getDialogBackground());
        assertFalse(dialogPreference.isValueShownAsSummary());
        assertFalse(dialogPreference.isDialogShown());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        Context context = getContext();
        XmlPullParser xmlPullParser =
                context.getResources().getXml(R.xml.abstract_dialog_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(context, attributeSet);
        assertNull(dialogPreference.getDialogTitle());
        assertNull(dialogPreference.getDialogIcon());
        assertNull(dialogPreference.getDialogMessage());
        assertNull(dialogPreference.getDialog());
        assertEquals(-1, dialogPreference.getDialogTitleColor());
        assertEquals(-1, dialogPreference.getDialogMessageColor());
        assertEquals(-1, dialogPreference.getDialogButtonTextColor());
        assertNull(dialogPreference.getDialogBackground());
        assertFalse(dialogPreference.isValueShownAsSummary());
        assertFalse(dialogPreference.isDialogShown());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser =
                context.getResources().getXml(R.xml.abstract_dialog_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(context, attributeSet, defaultStyle);
        assertNull(dialogPreference.getDialogTitle());
        assertNull(dialogPreference.getDialogIcon());
        assertNull(dialogPreference.getDialogMessage());
        assertNull(dialogPreference.getPositiveButtonText());
        assertNull(dialogPreference.getNegativeButtonText());
        assertNull(dialogPreference.getDialog());
        assertEquals(-1, dialogPreference.getDialogTitleColor());
        assertEquals(-1, dialogPreference.getDialogMessageColor());
        assertEquals(-1, dialogPreference.getDialogButtonTextColor());
        assertNull(dialogPreference.getDialogBackground());
        assertFalse(dialogPreference.isValueShownAsSummary());
        assertFalse(dialogPreference.isDialogShown());
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
            XmlPullParser xmlPullParser =
                    context.getResources().getXml(R.xml.abstract_dialog_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            AbstractDialogPreference dialogPreference =
                    new AbstractDialogPreferenceImplementation(context, attributeSet, defaultStyle,
                            defaultStyleAttribute);
            assertNull(dialogPreference.getDialogTitle());
            assertNull(dialogPreference.getDialogIcon());
            assertNull(dialogPreference.getDialogMessage());
            assertNull(dialogPreference.getPositiveButtonText());
            assertNull(dialogPreference.getNegativeButtonText());
            assertNull(dialogPreference.getDialog());
            assertEquals(-1, dialogPreference.getDialogTitleColor());
            assertEquals(-1, dialogPreference.getDialogMessageColor());
            assertEquals(-1, dialogPreference.getDialogButtonTextColor());
            assertNull(dialogPreference.getDialogBackground());
            assertFalse(dialogPreference.isValueShownAsSummary());
            assertFalse(dialogPreference.isDialogShown());
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the title of the preference's
     * dialog and expects an instance of the class {@link CharSequence} as a parameter.
     */
    public final void testSetDialogTitleWithCharSequenceParameter() {
        CharSequence title = "title";
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogTitle(title);
        assertEquals(title, dialogPreference.getDialogTitle());
    }

    /**
     * Tests the functionality of the method, which allows to set the title of the preference's
     * dialog and expects a resource id as a parameter.
     */
    public final void testSetDialogTitleWithResourceIdParameter() {
        int resourceId = android.R.string.ok;
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogTitle(resourceId);
        assertEquals(getContext().getText(resourceId), dialogPreference.getDialogTitle());
    }

    /**
     * Tests the functionality of the method, which allows to set the icon of the preference's
     * dialog and expects an instance of the class {@link Bitmap} as a parameter.
     */
    public final void testSetDialogIconWithBitmapParameter() {
        Bitmap icon = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogIcon(icon);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) dialogPreference.getDialogIcon();
        assertEquals(icon, bitmapDrawable.getBitmap());
    }

    /**
     * Tests the functionality of the method, which allows to set the icon of the preference's
     * dialog and expects a resource id as a parameter.
     */
    public final void testSetDialogIconWithResourceIdParameter() {
        int resourceId = android.R.drawable.ic_dialog_alert;
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogIcon(resourceId);
        assertNotNull(dialogPreference.getDialogIcon());
    }

    /**
     * Tests the functionality of the method, which allows to set the message of the preference's
     * dialog and expects an instance of the class {@link CharSequence} as a parameter.
     */
    public final void testSetDialogMessageWithCharSequenceParameter() {
        CharSequence message = "message";
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogMessage(message);
        assertEquals(message, dialogPreference.getDialogMessage());
    }

    /**
     * Tests the functionality of the method, which allows to set the message of the preference's
     * dialog and expects a resource id as a parameter.
     */
    public final void testSetDialogMessageWithResourceIdParameter() {
        int resourceId = android.R.string.ok;
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogMessage(resourceId);
        assertEquals(getContext().getText(resourceId), dialogPreference.getDialogMessage());
    }

    /**
     * Tests the functionality of the method, which allows to set the title color of the
     * preference's dialog.
     */
    public final void testSetDialogTitleColor() {
        int color = Color.BLACK;
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogTitleColor(color);
        assertEquals(color, dialogPreference.getDialogTitleColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the message color of the
     * preference's dialog.
     */
    public final void testSetDialogMessageColor() {
        int color = Color.BLACK;
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogMessageColor(color);
        assertEquals(color, dialogPreference.getDialogMessageColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the button text color of the
     * preference's dialog.
     */
    public final void testSetDialogButtonTextColor() {
        int color = Color.BLACK;
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogButtonTextColor(color);
        assertEquals(color, dialogPreference.getDialogButtonTextColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the background of the preference's
     * dialog.
     */
    public final void testSetDialogBackground() {
        Bitmap background = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogBackground(background);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) dialogPreference.getDialogBackground();
        assertEquals(background, bitmapDrawable.getBitmap());
    }

    /**
     * Tests the functionality of the method, which allows to set the background of the preference's
     * dialog, and expects a resource id as a parameter.
     */
    public final void testSetDialogBackgroundWithResourceIdParameter() {
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogBackground(android.R.drawable.ic_dialog_info);
        assertNotNull(dialogPreference.getDialogBackground());
    }

    /**
     * Tests the functionality of the method, which allows to set the background color of the
     * preference's dialog.
     */
    public final void testSetDialogBackgroundColor() {
        int color = Color.BLACK;
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setDialogBackgroundColor(color);
        ColorDrawable background = (ColorDrawable) dialogPreference.getDialogBackground();
        assertEquals(color, background.getColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the negative button of the
     * preference's dialog and expects an instance of the class {@link CharSequence} as a
     * parameter.
     */
    public final void testSetNegativeButtonWithCharSequenceParameter() {
        CharSequence buttonText = "buttonText";
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setNegativeButtonText(buttonText);
        assertEquals(buttonText, dialogPreference.getNegativeButtonText());
    }

    /**
     * Tests the functionality of the method, which allows to set the negative button of the
     * preference's dialog and expects a resource id as a parameter.
     */
    public final void testSetNegativeButtonWithResourceIdParameter() {
        int resourceId = android.R.string.cancel;
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setNegativeButtonText(resourceId);
        assertEquals(getContext().getText(resourceId), dialogPreference.getNegativeButtonText());
    }

    /**
     * Tests the functionality of the method, which allows to set the positive button of the
     * preference's dialog and expects an instance of the class {@link CharSequence} as a
     * parameter.
     */
    public final void testSetPositiveButtonWithCharSequenceParameter() {
        CharSequence buttonText = "buttonText";
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setPositiveButtonText(buttonText);
        assertEquals(buttonText, dialogPreference.getPositiveButtonText());
    }

    /**
     * Tests the functionality of the method, which allows to set the positive button of the
     * preference's dialog and expects a resource id as a parameter.
     */
    public final void testSetPositiveButtonWithResourceIdParameter() {
        int resourceId = android.R.string.ok;
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.setPositiveButtonText(resourceId);
        assertEquals(getContext().getText(resourceId), dialogPreference.getPositiveButtonText());
    }

    /**
     * Tests the functionality of the method, which allows to set, whether the preference's
     * currently persisted value should be shown as its summary, or not.
     */
    public final void testShowValueAsSummary() {
        boolean showValueAsSummary = true;
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        dialogPreference.showValueAsSummary(showValueAsSummary);
        assertEquals(showValueAsSummary, dialogPreference.isValueShownAsSummary());
    }

    /**
     * Tests the functionality of the onSaveInstanceState-method.
     */
    public final void testOnSaveInstanceState() {
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        AbstractDialogPreference.SavedState savedState =
                (AbstractDialogPreference.SavedState) dialogPreference.onSaveInstanceState();
        assertNull(savedState.dialogState);
    }

    /**
     * Tests the functionality of the onRestoreInstanceState-method.
     */
    public final void testOnRestoreInstanceState() {
        AbstractDialogPreference dialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        Parcelable parcelable = dialogPreference.onSaveInstanceState();
        AbstractDialogPreference restoredDialogPreference =
                new AbstractDialogPreferenceImplementation(getContext());
        restoredDialogPreference.onRestoreInstanceState(parcelable);
        assertEquals(dialogPreference.isDialogShown(), restoredDialogPreference.isDialogShown());
    }

}