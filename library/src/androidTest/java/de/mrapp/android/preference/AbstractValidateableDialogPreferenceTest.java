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
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import de.mrapp.android.validation.Validator;
import de.mrapp.android.validation.Validators;

/**
 * Tests the functionality of the class {@link AbstractValidateableDialogPreference}.
 *
 * @author Michael Rapp
 */
public class AbstractValidateableDialogPreferenceTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        Context context = getContext();
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(context);
        assertEquals(context, dialogPreference.getContext());
        assertTrue(dialogPreference.getValidators().isEmpty());
        assertTrue(dialogPreference.isValidatedOnValueChange());
        assertTrue(dialogPreference.isValidatedOnFocusLost());
        assertNull(dialogPreference.getHelperText());
        assertEquals(context.getResources().getColor(R.color.default_error_color),
                dialogPreference.getErrorColor());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        Context context = getContext();
        XmlPullParser xmlPullParser =
                context.getResources().getXml(R.xml.abstract_validateable_dialog_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(context, attributeSet);
        assertEquals(context, dialogPreference.getContext());
        assertTrue(dialogPreference.getValidators().isEmpty());
        assertTrue(dialogPreference.isValidatedOnValueChange());
        assertTrue(dialogPreference.isValidatedOnFocusLost());
        assertNull(dialogPreference.getHelperText());
        assertEquals(context.getResources().getColor(R.color.default_error_color),
                dialogPreference.getErrorColor());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser =
                context.getResources().getXml(R.xml.abstract_validateable_dialog_preference);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(context, attributeSet,
                        defaultStyle);
        assertEquals(context, dialogPreference.getContext());
        assertTrue(dialogPreference.getValidators().isEmpty());
        assertTrue(dialogPreference.isValidatedOnValueChange());
        assertTrue(dialogPreference.isValidatedOnFocusLost());
        assertNull(dialogPreference.getHelperText());
        assertEquals(context.getResources().getColor(R.color.default_error_color),
                dialogPreference.getErrorColor());
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
                    context.getResources().getXml(R.xml.abstract_validateable_dialog_preference);
            AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                    new AbstractValidateableDialogPreferenceImplementation(context, attributeSet,
                            defaultStyle, defaultStyleAttribute);
            assertEquals(context, dialogPreference.getContext());
            assertTrue(dialogPreference.getValidators().isEmpty());
            assertTrue(dialogPreference.isValidatedOnValueChange());
            assertTrue(dialogPreference.isValidatedOnFocusLost());
            assertNull(dialogPreference.getHelperText());
            assertEquals(context.getResources().getColor(R.color.default_error_color),
                    dialogPreference.getErrorColor());
        }
    }

    /**
     * Tests the functionality of the method, which allows to add a validator.
     */
    public final void testAddValidator() {
        Validator<CharSequence> validator = Validators.notEmpty("foo");
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.addValidator(validator);
        dialogPreference.addValidator(validator);
        Collection<Validator<CharSequence>> validators = dialogPreference.getValidators();
        assertEquals(1, validators.size());
        assertEquals(validator, validators.iterator().next());
    }

    /**
     * Tests the functionality of the method, which allows to add all validators, which are
     * contained by a collection.
     */
    public final void testAddAllValidatorsFromCollection() {
        Validator<CharSequence> validator1 = Validators.notEmpty("foo");
        Validator<CharSequence> validator2 = Validators.noWhitespace("bar");
        Collection<Validator<CharSequence>> validators1 = new LinkedList<>();
        validators1.add(validator1);
        validators1.add(validator2);
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.addAllValidators(validators1);
        dialogPreference.addAllValidators(validators1);
        Collection<Validator<CharSequence>> validators2 = dialogPreference.getValidators();
        assertEquals(validators1.size(), validators2.size());
        Iterator<Validator<CharSequence>> iterator = validators2.iterator();
        assertEquals(validator1, iterator.next());
        assertEquals(validator2, iterator.next());
    }

    /**
     * Tests the functionality of the method, which allows to add all validators, which are
     * contained by an array.
     */
    @SuppressWarnings("unchecked")
    public final void testAddAllValidatorsFromArray() {
        Validator<CharSequence> validator1 = Validators.notEmpty("foo");
        Validator<CharSequence> validator2 = Validators.noWhitespace("bar");
        Validator<CharSequence>[] validators1 = new Validator[2];
        validators1[0] = validator1;
        validators1[1] = validator2;
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.addAllValidators(validators1);
        dialogPreference.addAllValidators(validators1);
        Collection<Validator<CharSequence>> validators2 = dialogPreference.getValidators();
        assertEquals(validators1.length, validators2.size());
        Iterator<Validator<CharSequence>> iterator = validators2.iterator();
        assertEquals(validator1, iterator.next());
        assertEquals(validator2, iterator.next());
    }

    /**
     * Tests the functionality of the method, which allows to remove a validator.
     */
    public final void testRemoveValidator() {
        Validator<CharSequence> validator1 = Validators.notEmpty("foo");
        Validator<CharSequence> validator2 = Validators.noWhitespace("bar");
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.addValidator(validator1);
        dialogPreference.addValidator(validator2);
        dialogPreference.removeValidator(validator1);
        dialogPreference.removeValidator(validator1);
        Collection<Validator<CharSequence>> validators = dialogPreference.getValidators();
        assertEquals(1, validators.size());
        assertEquals(validator2, validators.iterator().next());
    }

    /**
     * Tests the functionality of the method, which allows to remove all validators, which are
     * contained by a collection.
     */
    public final void testRemoveAllValidatorsFromCollection() {
        Validator<CharSequence> validator1 = Validators.notEmpty("foo");
        Validator<CharSequence> validator2 = Validators.noWhitespace("bar");
        Validator<CharSequence> validator3 = Validators.number("foo2");
        Collection<Validator<CharSequence>> validators1 = new LinkedList<>();
        validators1.add(validator1);
        validators1.add(validator2);
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.addAllValidators(validators1);
        dialogPreference.addValidator(validator3);
        dialogPreference.removeAllValidators(validators1);
        dialogPreference.removeAllValidators(validators1);
        Collection<Validator<CharSequence>> validators2 = dialogPreference.getValidators();
        assertEquals(1, validators2.size());
        assertEquals(validator3, validators2.iterator().next());
    }

    /**
     * Tests the functionality of the method, which allows to remove all validators, which are
     * contained by an array.
     */
    @SuppressWarnings("unchecked")
    public final void testRemoveAllValidatorsFromArray() {
        Validator<CharSequence> validator1 = Validators.notEmpty("foo");
        Validator<CharSequence> validator2 = Validators.noWhitespace("bar");
        Validator<CharSequence> validator3 = Validators.number("foo2");
        Validator<CharSequence>[] validators1 = new Validator[2];
        validators1[0] = validator1;
        validators1[1] = validator2;
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.addAllValidators(validators1);
        dialogPreference.addValidator(validator3);
        dialogPreference.removeAllValidators(validators1);
        dialogPreference.removeAllValidators(validators1);
        Collection<Validator<CharSequence>> validators2 = dialogPreference.getValidators();
        assertEquals(1, validators2.size());
        assertEquals(validator3, validators2.iterator().next());
    }

    /**
     * Tests the functionality of the method, which allows to remove all validators.
     */
    public final void testRemoveAllValidators() {
        Validator<CharSequence> validator1 = Validators.notEmpty("foo");
        Validator<CharSequence> validator2 = Validators.noWhitespace("bar");
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.addValidator(validator1);
        dialogPreference.addValidator(validator2);
        dialogPreference.removeAllValidators();
        assertTrue(dialogPreference.getValidators().isEmpty());
    }

    /**
     * Tests the functionality of the method, which allows to set the helper text.
     */
    public final void testSetHelperText() {
        CharSequence helperText = "helperText";
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.setHelperText(helperText);
        assertEquals(helperText, dialogPreference.getHelperText());
    }

    /**
     * Tests the functionality of the method, which allows to set the error color.
     */
    public final void testSetErrorColor() {
        int errorColor = Color.RED;
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.setErrorColor(errorColor);
        assertEquals(errorColor, dialogPreference.getErrorColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the helper text color.
     */
    public final void testSetHelperTextColor() {
        int helperTextColor = Color.RED;
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.setHelperTextColor(helperTextColor);
        assertEquals(helperTextColor, dialogPreference.getHelperTextColor());
    }

    /**
     * Tests the functionality of the method, which allows to set, whether the view's value should
     * be validated on value changes, or not.
     */
    public final void testValidateOnValueChange() {
        boolean validateOnValueChange = false;
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.validateOnValueChange(validateOnValueChange);
        assertEquals(validateOnValueChange, dialogPreference.isValidatedOnValueChange());
    }

    /**
     * Tests the functionality of the method, which allows to set, whether the view's value should
     * be validated when the view loses its focus, or not.
     */
    public final void testValidateOnFocusLost() {
        boolean validateOnFocusLost = false;
        AbstractValidateableDialogPreference<CharSequence> dialogPreference =
                new AbstractValidateableDialogPreferenceImplementation(getContext());
        dialogPreference.validateOnFocusLost(validateOnFocusLost);
        assertEquals(validateOnFocusLost, dialogPreference.isValidatedOnFocusLost());
    }

}