/*
 * AndroidMaterialPreferences Copyright 2014 - 2015 Michael Rapp
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
package de.mrapp.android.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * A preference, which provides a two-state toggleable option using a
 * {@link SwitchCompat} widget.
 * 
 * @author Michael Rapp
 *
 * @since 1.4.0
 */
public class SwitchPreference extends AbstractTwoStatePreference {

	/**
	 * The switch, which allows to toggle the preference's value.
	 */
	private SwitchCompat switchCompat;

	/**
	 * Creates and returns a listener, which allows to change the preference's
	 * value, depending on the preference's switch's state.
	 * 
	 * @return The listener, which has been created, as an instance of the type
	 *         {@link OnCheckedChangeListener}
	 */
	private OnCheckedChangeListener createCheckedChangeListener() {
		return new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
				setChecked(isChecked);
			}

		};
	}

	/**
	 * Creates a new preference, which provides a two-state toggleable option
	 * using a {@link SwitchCompat} widget.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 */
	public SwitchPreference(final Context context) {
		this(context, null);
	}

	/**
	 * Creates a new preference, which provides a two-state toggleable option
	 * using a {@link SwitchCompat} widget.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the
	 *            preference, as an instance of the type {@link AttributeSet}
	 */
	public SwitchPreference(final Context context, final AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	/**
	 * Creates a new preference, which provides a two-state toggleable option
	 * using a {@link SwitchCompat} widget.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the
	 *            preference, as an instance of the type {@link AttributeSet}
	 * @param defaultStyle
	 *            The default style to apply to this preference. If 0, no style
	 *            will be applied (beyond what is included in the theme). This
	 *            may either be an attribute resource, whose value will be
	 *            retrieved from the current theme, or an explicit style
	 *            resource
	 */
	public SwitchPreference(final Context context, final AttributeSet attributeSet, final int defaultStyle) {
		super(context, attributeSet, defaultStyle);
	}

	/**
	 * Creates a new preference, which provides a two-state toggleable option
	 * using a {@link SwitchCompat} widget.
	 * 
	 * @param context
	 *            The context, which should be used by the preference, as an
	 *            instance of the class {@link Context}
	 * @param attributeSet
	 *            The attributes of the XML tag that is inflating the
	 *            preference, as an instance of the type {@link AttributeSet}
	 * @param defaultStyle
	 *            The default style to apply to this preference. If 0, no style
	 *            will be applied (beyond what is included in the theme). This
	 *            may either be an attribute resource, whose value will be
	 *            retrieved from the current theme, or an explicit style
	 *            resource
	 * @param defaultStyleResource
	 *            A resource identifier of a style resource that supplies
	 *            default values for the preference, used only if the default
	 *            style is 0 or can not be found in the theme. Can be 0 to not
	 *            look for defaults
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public SwitchPreference(final Context context, final AttributeSet attributeSet, final int defaultStyle,
			final int defaultStyleResource) {
		super(context, attributeSet, defaultStyle, defaultStyleResource);
	}

	@Override
	protected final View onCreateView(final ViewGroup parent) {
		View view = super.onCreateView(parent);
		LinearLayout widgetFrame = (LinearLayout) view.findViewById(android.R.id.widget_frame);
		widgetFrame.setVisibility(View.VISIBLE);
		switchCompat = new SwitchCompat(getContext());
		switchCompat.setFocusable(false);
		switchCompat.setOnCheckedChangeListener(createCheckedChangeListener());
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER_VERTICAL;
		widgetFrame.addView(switchCompat, layoutParams);
		switchCompat.setChecked(isChecked());
		return view;
	}

}