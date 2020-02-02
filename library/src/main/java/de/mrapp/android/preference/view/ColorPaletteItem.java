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
package de.mrapp.android.preference.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import de.mrapp.android.preference.R;

/**
 * A view that visualizes a single color of a color palette.
 *
 * @author Michael Rapp
 * @since 5.0.0
 */
public class ColorPaletteItem extends FrameLayout implements Checkable {

    /**
     * True, if the item is currently checked, false otherwise.
     */
    private boolean checked;

    /**
     * The image view, which is used to display the item's color.
     */
    private ImageView colorView;

    /**
     * The view, which is used to display the item's selection when it is checked.
     */
    private View selectionView;

    /**
     * Initializes the view.
     */
    private void initialize() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.color_palette_item, this, true);
        colorView = findViewById(R.id.color_view);
        selectionView = findViewById(R.id.selection_view);
        adaptSelectionView();
    }

    /**
     * Adapts the selection view.
     */
    private void adaptSelectionView() {
        if (selectionView != null) {
            selectionView.setActivated(checked);
        }
    }

    /**
     * Creates a new view that visualizes a single color of a color palette.
     *
     * @param context
     *         The context, which should be used by the number picker, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public ColorPaletteItem(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new view that visualizes a single color of a color palette.
     *
     * @param context
     *         The context, which should be used by the number picker, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the view, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes are available
     */
    public ColorPaletteItem(@NonNull final Context context,
                            @Nullable final AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /**
     * Creates a new view that visualizes a single color of a color palette.
     *
     * @param context
     *         The context, which should be used by the number picker, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the view, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes are available
     * @param defaultStyle
     *         The default style to apply to this view. If 0, no style will be applied (beyond what
     *         is included in the theme). This may either be an attribute resource, whose value will
     *         be retrieved from the current theme, or an explicit style resource
     */
    public ColorPaletteItem(@NonNull final Context context,
                            @Nullable final AttributeSet attributeSet,
                            @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize();
    }

    /**
     * Creates a new view that visualizes a single color of a color palette.
     *
     * @param context
     *         The context, which should be used by the number picker, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the view, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes are available
     * @param defaultStyle
     *         The default style to apply to this view. If 0, no style will be applied (beyond what
     *         is included in the theme). This may either be an attribute resource, whose value will
     *         be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the view,
     *         used only if the default style is 0 or can not be found in the theme. Can be 0 to not
     *         look for defaults
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorPaletteItem(@NonNull final Context context,
                            @Nullable final AttributeSet attributeSet,
                            @AttrRes final int defaultStyle,
                            @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize();
    }

    /**
     * Returns the image view, which is used to display the item's color.
     *
     * @return The image view, which is used to display the item's color, as an instance of the
     * class {@link ImageView}. The view image view may not be null
     */
    @NonNull
    public final ImageView getColorView() {
        return colorView;
    }

    @Override
    public final void setChecked(final boolean checked) {
        this.checked = checked;
        adaptSelectionView();
    }

    @Override
    public final boolean isChecked() {
        return checked;
    }

    @Override
    public final void toggle() {
        setChecked(!isChecked());
    }

}
