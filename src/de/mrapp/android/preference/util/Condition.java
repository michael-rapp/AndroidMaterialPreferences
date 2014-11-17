/*
 * AndroidMaterialPreferences Copyright 2014 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>. 
 */
package de.mrapp.android.preference.util;

/**
 * An utility class, which offers static methods to ensure, that attributes
 * satisfy specific conditions by throwing exceptions, if the conditions are not
 * satisfied.
 * 
 * @author Michael Rapp
 * 
 * @since 1.0.0
 */
public final class Condition {

	/**
	 * An utility class, which offers static methods to ensure, that attributes
	 * satisfy specific conditions by throwing exceptions, if the conditions are
	 * not satisfied.
	 */
	private Condition() {

	}

	/**
	 * Ensures, that a reference is not null. Otherwise a
	 * {@link NullPointerException} with a specific message will be thrown.
	 * 
	 * @param reference
	 *            The reference, which should be checked, as an instance of the
	 *            class {@link Object}
	 * @param exceptionMessage
	 *            The message of the {@link NullPointerException}, which is
	 *            thrown, if the given reference is null, as a {@link String}
	 */
	public static void ensureNotNull(final Object reference,
			final String exceptionMessage) {
		if (reference == null) {
			throw new NullPointerException(exceptionMessage);
		}
	}

	/**
	 * Ensures, that a {@link Float} value is at least a specific value.
	 * Otherwise an {@link IllegalArgumentException} with a specific message
	 * will be thrown.
	 * 
	 * @param value
	 *            The value, which should be checked, as a {@link Float} value
	 * @param referenceValue
	 *            The value, the given value must be at least, as a
	 *            {@link Float} value
	 * @param exceptionMessage
	 *            The message of the {@link IllegalArgumentException}, which is
	 *            thrown, if the given value is less than the reference value,
	 *            as a {@link String}
	 */
	public static void ensureAtLeast(final float value,
			final float referenceValue, final String exceptionMessage) {
		if (value < referenceValue) {
			throw new IllegalArgumentException(exceptionMessage);
		}
	}

	/**
	 * Ensures, that a {@link Float} value is at maximum a specific value.
	 * Otherwise an {@link IllegalArgumentException} with a specific message
	 * will be thrown.
	 * 
	 * @param value
	 *            The value, which should be checked, as a {@link Float} value
	 * @param referenceValue
	 *            The value, the given value must be at maximum, as a
	 *            {@link Float} value
	 * @param exceptionMessage
	 *            The message of the {@link IllegalArgumentException}, which is
	 *            thrown, if the given value is greater than the reference
	 *            value, as a {@link String}
	 */
	public static void ensureAtMaximum(final float value,
			final float referenceValue, final String exceptionMessage) {
		if (value > referenceValue) {
			throw new IllegalArgumentException(exceptionMessage);
		}
	}

	/**
	 * Ensures, that a {@link Float} value is less than a reference value.
	 * Otherwise an {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param value
	 *            The value, which should be checked, as a {@link Float} value
	 * @param referenceValue
	 *            The value, the given value must be less than, as a
	 *            {@link Float} value
	 * @param exceptionMessage
	 *            The message of the {@link IllegalArgumentException}, which is
	 *            thrown, if the given value is greater or equal than the
	 *            reference value, as a {@link String}
	 */
	public static void ensureLessThan(final float value,
			final float referenceValue, final String exceptionMessage) {
		if (value >= referenceValue) {
			throw new IllegalArgumentException(exceptionMessage);
		}
	}

	/**
	 * Ensures, that a {@link Float} value is greater than a reference value.
	 * Otherwise an {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param value
	 *            The value, which should be checked, as a {@link Float} value
	 * @param referenceValue
	 *            The value, the given value must be greater than, as a
	 *            {@link Float} value
	 * @param exceptionMessage
	 *            The message of the {@link IllegalArgumentException}, which is
	 *            thrown, if the given value is less or equal than the reference
	 *            value, as a {@link String}
	 */
	public static void ensureGreaterThan(final float value,
			final float referenceValue, final String exceptionMessage) {
		if (value <= referenceValue) {
			throw new IllegalArgumentException(exceptionMessage);
		}
	}

}