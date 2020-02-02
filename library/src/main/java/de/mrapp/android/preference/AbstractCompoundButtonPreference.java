/*
 * Copyright 2014 - 2020 Michael Rapp
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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.AttrRes;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.preference.PreferenceViewHolder;

/**
 * An abstract base class for all {@link AbstractTwoStatePreference}s that contain a {@link
 * CompoundButton} as their widget.
 *
 * @author Michael Rapp
 * @since 5.2.0
 */
public class AbstractCompoundButtonPreference extends AbstractTwoStatePreference {

    /**
     * Adapts the preference's {@link CompoundButton}, depending on the preference's properties and
     * on whether it is currently checked or not.
     *
     * @param viewHolder
     *         The preference's view holder as an instance of the class {@link
     *         PreferenceViewHolder}. The view holder may not be null
     */
    private void adaptCompoundButton(@NonNull final PreferenceViewHolder viewHolder) {
        View view = viewHolder.findViewById(R.id.compound_button);

        if (view instanceof CompoundButton) {
            CompoundButton compoundButton = (CompoundButton) view;
            compoundButton.setOnCheckedChangeListener(null);
            compoundButton.setChecked(isChecked());
            compoundButton.setOnCheckedChangeListener(createCheckedChangeListener());
        }
    }

    /**
     * Creates and returns a listener, which allows to change the preference's value, depending on
     * the preference's {@link CompoundButton}'s state.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * CompoundButton.OnCheckedChangeListener}
     */
    private CompoundButton.OnCheckedChangeListener createCheckedChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (getOnPreferenceChangeListener() == null || getOnPreferenceChangeListener()
                        .onPreferenceChange(AbstractCompoundButtonPreference.this, isChecked)) {
                    setChecked(isChecked);
                } else {
                    setChecked(!isChecked);
                }
            }

        };
    }

    /**
     * Creates a new {@link AbstractTwoStatePreference}s that contain a {@link CompoundButton} as
     * their widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public AbstractCompoundButtonPreference(@NonNull final Context context) {
        super(context);
    }

    /**
     * Creates a new {@link AbstractTwoStatePreference}s that contain a {@link CompoundButton} as
     * their widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public AbstractCompoundButtonPreference(@NonNull final Context context,
                                            @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /**
     * Creates a new {@link AbstractTwoStatePreference}s that contain a {@link CompoundButton} as
     * their widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     */
    public AbstractCompoundButtonPreference(@NonNull final Context context,
                                            @Nullable final AttributeSet attributeSet,
                                            @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
    }

    /**
     * Creates a new {@link AbstractTwoStatePreference}s that contain a {@link CompoundButton} as
     * their widget.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the
     *         preference, used only if the default style is 0 or can not be found in the theme. Can
     *         be 0 to not look for defaults
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbstractCompoundButtonPreference(@NonNull final Context context,
                                            @Nullable final AttributeSet attributeSet,
                                            @AttrRes final int defaultStyle,
                                            @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
    }

    @CallSuper
    @Override
    public void onBindViewHolder(final PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        adaptCompoundButton(holder);
    }

    @CallSuper
    @Override
    protected void onCheckedChanged(final boolean checked) {
        notifyChanged();
    }

}
