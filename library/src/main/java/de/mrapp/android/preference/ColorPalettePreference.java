/*
 * Copyright 2014 - 2019 Michael Rapp
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
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mrapp.android.dialog.AbstractButtonBarDialog;
import de.mrapp.android.dialog.builder.AbstractButtonBarDialogBuilder;
import de.mrapp.android.dialog.builder.AbstractListDialogBuilder;
import de.mrapp.android.dialog.model.ListDialog;
import de.mrapp.android.preference.adapter.ColorPaletteAdapter;
import de.mrapp.util.Condition;

/**
 * A preference, which allows to choose a color from a pre-defined color palette. The chosen color
 * will only be persisted, if confirmed by the user.
 *
 * @author Michael Rapp
 * @since 1.4.0
 */
public class ColorPalettePreference extends AbstractColorPickerPreference {

    /**
     * An array, which contains the colors, the preference allows to choose.
     */
    private int[] colorPalette;

    /**
     * The size, which is used to preview colors in the preference's dialog.
     */
    private int dialogPreviewSize;

    /**
     * The shape, which is used to preview colors in the preference's dialog.
     */
    private PreviewShape dialogPreviewShape;

    /**
     * The border width, which is used to preview colors in the preference's dialog.
     */
    private int dialogPreviewBorderWidth;

    /**
     * The border color, which is used to preview colors in the preference's dialog.
     */
    private int dialogPreviewBorderColor;

    /**
     * The background, which is used to preview colors in the preference's dialog.
     */
    private Drawable dialogPreviewBackground;

    /**
     * The number of columns, which are used to preview colors in the preference's dialog.
     */
    private int numberOfColumns;

    /**
     * The adapter, which provides the colors for visualization using the preference dialog's
     * recycler view.
     */
    private ColorPaletteAdapter adapter;

    /**
     * The index of the currently selected list item.
     */
    private int selectedIndex = -1;

    /**
     * Initializes the preference.
     *
     * @param attributeSet
     *         The attribute set, which should be used to initialize the preferences, as an instance
     *         of the type {@link AttributeSet} or null, if no attributes should be obtained
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the
     *         preference, used only if the default style is 0 or can not be found in the theme. Can
     *         be 0 to not look for defaults
     */
    private void initialize(@Nullable final AttributeSet attributeSet,
                            @AttrRes final int defaultStyle,
                            @StyleRes final int defaultStyleResource) {
        colorPalette = new int[0];
        setNegativeButtonText(android.R.string.cancel);
        setPositiveButtonText(null);
        obtainStyledAttributes(attributeSet, defaultStyle, defaultStyleResource);
    }

    /**
     * Obtains all attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the
     *         preference, used only if the default style is 0 or can not be found in the theme. Can
     *         be 0 to not look for defaults
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet,
                                        @AttrRes final int defaultStyle,
                                        @StyleRes final int defaultStyleResource) {
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attributeSet, R.styleable.ColorPalettePreference,
                        defaultStyle, defaultStyleResource);

        try {
            obtainColorPalette(typedArray);
            obtainDialogPreviewSize(typedArray);
            obtainDialogPreviewShape(typedArray);
            obtainDialogPreviewBorderWidth(typedArray);
            obtainDialogPreviewBorderColor(typedArray);
            obtainDialogPreviewBackground(typedArray);
            obtainNumberOfColumns(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the color palette from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color palette, should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainColorPalette(@NonNull final TypedArray typedArray) {
        int resourceId =
                typedArray.getResourceId(R.styleable.ColorPalettePreference_colorPalette, -1);

        if (resourceId != -1) {
            int[] obtainedColorPalette = getContext().getResources().getIntArray(resourceId);
            setColorPalette(obtainedColorPalette);
        }
    }

    /**
     * Obtains the size, which should be used to preview colors in the preference's dialog, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the size should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainDialogPreviewSize(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_palette_preference_default_dialog_preview_size);
        setDialogPreviewSize(typedArray
                .getDimensionPixelSize(R.styleable.ColorPalettePreference_dialogPreviewSize,
                        defaultValue));
    }

    /**
     * Obtains the shape, which should be used to preview colors in the preference's dialog, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the shape should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogPreviewShape(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.color_palette_preference_default_dialog_preview_shape);
        setDialogPreviewShape(PreviewShape.fromValue(typedArray
                .getInteger(R.styleable.ColorPalettePreference_dialogPreviewShape, defaultValue)));
    }

    /**
     * Obtains the border width, which should be used to preview colors in the preference's dialog,
     * from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the border width should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogPreviewBorderWidth(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_palette_preference_default_dialog_preview_border_width);
        setDialogPreviewBorderWidth(typedArray
                .getDimensionPixelSize(R.styleable.ColorPalettePreference_dialogPreviewBorderWidth,
                        defaultValue));
    }

    /**
     * Obtains the border color, which should be used to preview colors in the preference's dialog,
     * from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the border color should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogPreviewBorderColor(@NonNull final TypedArray typedArray) {
        int defaultValue = ContextCompat.getColor(getContext(),
                R.color.color_palette_preference_default_dialog_preview_border_color);
        setDialogPreviewBorderColor(typedArray
                .getColor(R.styleable.ColorPalettePreference_dialogPreviewBorderColor,
                        defaultValue));
    }

    /**
     * Obtains the background, which should be used to preview colors in the preference's dialog,
     * from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the background should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDialogPreviewBackground(@NonNull final TypedArray typedArray) {
        int backgroundColor =
                typedArray.getColor(R.styleable.ColorPalettePreference_dialogPreviewBackground, -1);

        if (backgroundColor != -1) {
            setPreviewBackgroundColor(backgroundColor);
        } else {
            int resourceId = typedArray
                    .getResourceId(R.styleable.ColorPalettePreference_dialogPreviewBackground,
                            R.drawable.color_picker_default_preview_background);
            setDialogPreviewBackground(ContextCompat.getDrawable(getContext(), resourceId));
        }
    }

    /**
     * Obtains the number of columns, which should be used to preview colors in the preference's
     * dialog, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the number of columns should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainNumberOfColumns(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.color_palette_preference_default_number_of_columns);
        setNumberOfColumns(typedArray
                .getInteger(R.styleable.ColorPalettePreference_android_numColumns, defaultValue));
    }

    /**
     * Creates and returns a listener, which allows to observe when a list item has been selected.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * ListDialog.OnItemSelectedListener}. The listener may not be null
     */
    @NonNull
    private ListDialog.OnItemSelectedListener createItemSelectedListener() {
        return new ListDialog.OnItemSelectedListener() {

            @Override
            public void onItemSelected(final int position) {
                ColorPalettePreference.this.selectedIndex = position;
                ColorPalettePreference.this.onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);

                if (getDialog() != null) {
                    getDialog().dismiss();
                }
            }

        };
    }

    /**
     * Creates a new preference, which allows to choose a color from a pre-defined color palette.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public ColorPalettePreference(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new preference, which allows to choose a color from a pre-defined color palette.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public ColorPalettePreference(@NonNull final Context context,
                                  @Nullable final AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.dialogPreferenceStyle);
    }

    /**
     * Creates a new preference, which allows to choose a color from a pre-defined color palette.
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
    public ColorPalettePreference(@NonNull final Context context,
                                  @Nullable final AttributeSet attributeSet,
                                  @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet, defaultStyle, 0);
    }

    /**
     * Creates a new preference, which allows to choose a color from a pre-defined color palette.
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
    public ColorPalettePreference(@NonNull final Context context,
                                  @Nullable final AttributeSet attributeSet,
                                  @AttrRes final int defaultStyle,
                                  @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet, defaultStyle, defaultStyleResource);
    }

    /**
     * Returns the colors, the preference allows to choose.
     *
     * @return The colors, the preference allows to choose, as an {@link Integer} array
     */
    public final int[] getColorPalette() {
        return colorPalette;
    }

    /**
     * Sets the colors, the preference should allow to choose.
     *
     * @param colorPalette
     *         The colors, which should be set, as an {@link Integer} array
     */
    public final void setColorPalette(@NonNull final int[] colorPalette) {
        Condition.INSTANCE.ensureNotNull(colorPalette, "The color palette may not be null");
        this.colorPalette = colorPalette;
    }

    /**
     * Sets the colors, the preference should allow to choose.
     *
     * @param resourceId
     *         The resource id of the color palette, which should be set, as an {@link Integer}
     *         value. The resource id must correspond to a valid integer array resource
     */
    public final void setColorPalette(@ArrayRes final int resourceId) {
        setColorPalette(getContext().getResources().getIntArray(resourceId));
    }

    /**
     * Returns the size, which is used to preview colors in the preference's dialog.
     *
     * @return The size, which is used to preview colors in the preference's dialog, as an {@link
     * Integer} value in pixels
     */
    public final int getDialogPreviewSize() {
        return dialogPreviewSize;
    }

    /**
     * Sets the size, which should be used to preview colors in the preference's dialog.
     *
     * @param previewSize
     *         The size, which should be set, as an {@link Integer} value in pixels. The size must
     *         be at least 1
     */
    public final void setDialogPreviewSize(final int previewSize) {
        Condition.INSTANCE.ensureAtLeast(previewSize, 1, "The preview size must be at least 1");
        this.dialogPreviewSize = previewSize;
    }

    /**
     * Returns the shape, which is used to preview colors in the preference's dialog.
     *
     * @return The shape, which is used to preview colors in the preference's dialog, as a value of
     * the enum {@link PreviewShape}. The shape may either be <code>CIRCLE</code> or
     * <code>SQUARE</code>
     */
    public final PreviewShape getDialogPreviewShape() {
        return dialogPreviewShape;
    }

    /**
     * Sets the shape, which should be used to preview colors in the preference's dialog.
     *
     * @param previewShape
     *         The shape, which should be set, as a value of the enum {@link PreviewShape}. The
     *         shape may not be null
     */
    public final void setDialogPreviewShape(@NonNull final PreviewShape previewShape) {
        Condition.INSTANCE.ensureNotNull(previewShape, "The preview shape may not be null");
        this.dialogPreviewShape = previewShape;
    }

    /**
     * Returns the border width, which is used to preview colors in the preference's dialog.
     *
     * @return The border width, which is used to preview colors in the preference's dialog, as an
     * {@link Integer} value in pixels
     */
    public final int getDialogPreviewBorderWidth() {
        return dialogPreviewBorderWidth;
    }

    /**
     * Sets the border width, which should be used to preview colors in the preference's dialog.
     *
     * @param borderWidth
     *         The border width, which should be set, as an {@link Integer} value in pixels. The
     *         border width must be at least 0
     */
    public final void setDialogPreviewBorderWidth(final int borderWidth) {
        Condition.INSTANCE.ensureAtLeast(borderWidth, 0, "The border width must be at least 0");
        this.dialogPreviewBorderWidth = borderWidth;
    }

    /**
     * Returns the border color, which is used to preview colors in the preference's dialog.
     *
     * @return The border color, which is used to preview colors in the preference's dialog, as an
     * {@link Integer} value
     */
    public final int getDialogPreviewBorderColor() {
        return dialogPreviewBorderColor;
    }

    /**
     * Sets the border color, which should be used to preview colors in the preference's dialog.
     *
     * @param borderColor
     *         The border color, which should be set, as an {@link Integer} value
     */
    public final void setDialogPreviewBorderColor(@ColorInt final int borderColor) {
        this.dialogPreviewBorderColor = borderColor;
    }

    /**
     * Returns the background, which is used to preview colors in the preference's dialog.
     *
     * @return The background, which is used to preview colors in the preference's dialog, as an
     * instance of the class {@link Drawable}
     */
    public final Drawable getDialogPreviewBackground() {
        return dialogPreviewBackground;
    }

    /**
     * Sets the background, which should be used to preview colors in the preference's dialog.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Drawable} or
     *         null, if no background should be shown
     */
    public final void setDialogPreviewBackground(@Nullable final Drawable background) {
        this.dialogPreviewBackground = background;
    }

    /**
     * Sets the background, which should be used to preview colors in the preference's dialog.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     */
    public final void setDialogPreviewBackground(@DrawableRes final int resourceId) {
        setDialogPreviewBackground(ContextCompat.getDrawable(getContext(), resourceId));
    }

    /**
     * Sets the background color, which should be used to preview colors in the preference's
     * dialog.
     *
     * @param color
     *         The background color, which should be set, as an {@link Integer} value
     */
    public final void setDialogPreviewBackgroundColor(@ColorInt final int color) {
        setDialogPreviewBackground(new ColorDrawable(color));
    }

    /**
     * Returns the number of columns, which are used to preview colors in the preference's dialog.
     *
     * @return The number of columns, which are used to preview colors in the preference's dialog,
     * as an {@link Integer} value
     */
    public final int getNumberOfColumns() {
        return numberOfColumns;
    }

    /**
     * Sets the number of columns, which should be used to preview colors in the preference's
     * dialog.
     *
     * @param numberOfColumns
     *         The number of columns, which should be set, as an {@link Integer} value. The number
     *         of columns must be at least 1
     */
    public final void setNumberOfColumns(final int numberOfColumns) {
        Condition.INSTANCE
                .ensureAtLeast(numberOfColumns, 1, "The number of columns must be at least 1");
        this.numberOfColumns = numberOfColumns;
    }

    @CallSuper
    @Override
    protected void onPrepareDialog(
            @NonNull final AbstractButtonBarDialogBuilder<?, ?> dialogBuilder) {
        AbstractListDialogBuilder<?, ?> listDialogBuilder =
                (AbstractListDialogBuilder<?, ?>) dialogBuilder;
        adapter = new ColorPaletteAdapter(listDialogBuilder.getContext(), getColorPalette(),
                getDialogPreviewSize(), getDialogPreviewShape(), getDialogPreviewBorderWidth(),
                getDialogPreviewBorderColor(), getDialogPreviewBackground());
        this.selectedIndex = adapter.indexOf(getColor());
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getContext(), getNumberOfColumns());
        listDialogBuilder.setSingleChoiceItems(adapter, layoutManager, selectedIndex, null);
        listDialogBuilder.setOnItemSelectedListener(createItemSelectedListener());
    }

    @CallSuper
    @Override
    protected void onDialogClosed(@NonNull final AbstractButtonBarDialog dialog,
                                  final boolean positiveResult) {
        if (positiveResult) {
            int newValue = adapter.getItem(selectedIndex);

            if (callChangeListener(newValue)) {
                setColor(newValue);
            }
        }

        adapter = null;
    }

}