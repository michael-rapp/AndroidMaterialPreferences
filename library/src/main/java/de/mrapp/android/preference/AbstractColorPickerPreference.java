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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.Locale;

import de.mrapp.android.preference.multithreading.ColorPreviewLoader;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all preferences, which allow to choose a color.
 *
 * @author Michael Rapp
 * @since 1.4.0
 */
public abstract class AbstractColorPickerPreference extends AbstractDialogPreference {

    /**
     * The formats, which are supported to print textual representations of colors.
     */
    public enum ColorFormat {

        /**
         * Formats the color by printing its red, green and blue components.
         */
        RGB(0),

        /**
         * Formats the color by printing its alpha, red, green and blue components.
         */
        ARGB(1),

        /**
         * Formats the color as a hexadecimal string, consisting of three bytes.
         */
        HEX_3_BYTES(2),

        /**
         * Formats the color as a hexadecimal string, consisting of four bytes, including its alpha
         * component.
         */
        HEX_4_BYTES(3);

        /**
         * The value of the format.
         */
        private final int value;

        /**
         * Creates a new format.
         *
         * @param value
         *         The value of the format as an {@link Integer} value
         */
        ColorFormat(final int value) {
            this.value = value;
        }

        /**
         * Returns the value of the format.
         *
         * @return The value of the format as an {@link Integer} value
         */
        public final int getValue() {
            return value;
        }

        /**
         * Returns the format, which corresponds to a specific value.
         *
         * @param value
         *         The value of the format, which should be returned, as an {@link Integer} value
         * @return The format, which corresponds to the given value, as an instance of the enum
         * {@link ColorFormat}
         */
        public static ColorFormat fromValue(final int value) {
            for (ColorFormat format : values()) {
                if (format.getValue() == value) {
                    return format;
                }
            }

            throw new IllegalArgumentException("Invalid enum value");
        }

    }

    /**
     * Contains all shapes, which can be used to show previews of colors.
     */
    public enum PreviewShape {

        /**
         * If the preview is shaped as a circle.
         */
        CIRCLE(0),

        /**
         * If the preview is shaped as a square.
         */
        SQUARE(1);

        /**
         * The value of the shape.
         */
        private final int value;

        /**
         * Creates a new shape.
         *
         * @param value
         *         The value of the shape as an {@link Integer} value
         */
        PreviewShape(final int value) {
            this.value = value;
        }

        /**
         * Returns the value of the shape.
         *
         * @return The value of the shape as an {@link Integer} value
         */
        public final int getValue() {
            return value;
        }

        /**
         * Returns the shape, which corresponds to a specific value.
         *
         * @param value
         *         The value of the shape, which should be returned, as an {@link Integer} value
         * @return The shape, which corresponds to the given value, as an instance of the enum
         * {@link PreviewShape}
         */
        public static PreviewShape fromValue(final int value) {
            for (PreviewShape shape : values()) {
                if (shape.getValue() == value) {
                    return shape;
                }
            }

            throw new IllegalArgumentException("Invalid enum value");
        }

    }

    /**
     * A data structure, which allows to save the internal state of an {@link
     * AbstractColorPickerPreference}.
     */
    public static class SavedState extends BaseSavedState {

        /**
         * A creator, which allows to create instances of the class {@link SavedState} from
         * parcels.
         */
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    @Override
                    public SavedState createFromParcel(final Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(final int size) {
                        return new SavedState[size];
                    }

                };

        /**
         * The saved value of the attribute "color".
         */
        public int color;

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * AbstractColorPickerPreference}. This constructor is called by derived classes when saving
         * their states.
         *
         * @param superState
         *         The state of the superclass of this view, as an instance of the type {@link
         *         Parcelable}. The state may not be null
         */
        public SavedState(@NonNull final Parcelable superState) {
            super(superState);
        }

        /**
         * Creates a new data structure, which allows to store the internal state of an {@link
         * AbstractColorPickerPreference}. This constructor is used when reading from a parcel. It
         * reads the state of the superclass.
         *
         * @param source
         *         The parcel to read read from as a instance of the class {@link Parcel}. The
         *         parcel may not be null
         */
        public SavedState(@NonNull final Parcel source) {
            super(source);
            color = source.readInt();
        }

        @Override
        public final void writeToParcel(final Parcel destination, final int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(color);
        }

    }

    /**
     * The currently persisted color.
     */
    private int color;

    /**
     * True, if a preview of the preference's color is shown, false otherwise.
     */
    private boolean showPreview;

    /**
     * The size of the preview of the preference's color in pixels.
     */
    private int previewSize;

    /**
     * The shape of the preview of the preference's color.
     */
    private PreviewShape previewShape;

    /**
     * The border width of the preview of the preference's color in pixels.
     */
    private int previewBorderWidth;

    /**
     * The border color of the preview of the preference's color.
     */
    private int previewBorderColor;

    /**
     * The background of the preview of the preference's color.
     */
    private Drawable previewBackground;

    /**
     * The format, which is used to print a textual representation of the preference's color.
     */
    private ColorFormat colorFormat;

    /**
     * The image view, which is used to show a preview of the preference's color.
     */
    private ImageView previewView;

    /**
     * The data loader, which is used to load the preview of colors asynchronously.
     */
    private ColorPreviewLoader previewLoader;

    /**
     * Initializes the preference.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void initialize(@Nullable final AttributeSet attributeSet) {
        obtainStyledAttributes(attributeSet);
        previewLoader =
                new ColorPreviewLoader(getContext(), getPreviewBackground(), getPreviewShape(),
                        getPreviewSize(), getPreviewBorderWidth(), getPreviewBorderColor());
    }

    /**
     * Obtains all attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet) {
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attributeSet, R.styleable.AbstractColorPickerPreference);
        try {
            obtainShowPreview(typedArray);
            obtainPreviewSize(typedArray);
            obtainPreviewShape(typedArray);
            obtainPreviewBorderWidth(typedArray);
            obtainPreviewBorderColor(typedArray);
            obtainPreviewBackground(typedArray);
            obtainColorFormat(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Obtains the boolean value, which specifies, whether a preview of the preference's color
     * should be shown, or not, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the boolean value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainShowPreview(@NonNull final TypedArray typedArray) {
        boolean defaultValue = getContext().getResources()
                .getBoolean(R.bool.color_picker_preference_default_show_preview);
        showPreview(typedArray
                .getBoolean(R.styleable.AbstractColorPickerPreference_showPreview, defaultValue));
    }

    /**
     * Obtains the size of the preview of the preference's color from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the size should be obtained from, as an instance of the class {@link
     *         TypedArray}. The typed array may not be null
     */
    private void obtainPreviewSize(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getDimensionPixelSize(R.dimen.color_picker_preference_default_preview_size);
        setPreviewSize(typedArray
                .getDimensionPixelSize(R.styleable.AbstractColorPickerPreference_previewSize,
                        defaultValue));
    }

    /**
     * Obtains the shape of the preview of the preference's color from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the shape should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainPreviewShape(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.color_picker_preference_default_preview_shape);
        setPreviewShape(PreviewShape.fromValue(typedArray
                .getInteger(R.styleable.AbstractColorPickerPreference_previewShape, defaultValue)));
    }

    /**
     * Obtains the border width of the preview of the preference's color from a specific typed
     * array.
     *
     * @param typedArray
     *         The typed array, the border width should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainPreviewBorderWidth(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources().getDimensionPixelSize(
                R.dimen.color_picker_preference_default_preview_border_width);
        setPreviewBorderWidth(typedArray
                .getDimensionPixelSize(R.styleable.AbstractColorPickerPreference_previewBorderWidth,
                        defaultValue));
    }

    /**
     * Obtains the border color of the preview of the preference's color from a specific typed
     * array.
     *
     * @param typedArray
     *         The typed array, the border color should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    @SuppressWarnings("deprecation")
    private void obtainPreviewBorderColor(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getColor(R.color.color_picker_preference_default_preview_border_color);
        setPreviewBorderColor(typedArray
                .getColor(R.styleable.AbstractColorPickerPreference_previewBorderColor,
                        defaultValue));
    }

    /**
     * Obtains the background of the preview of the preference's color from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the background should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    @SuppressWarnings("deprecation")
    private void obtainPreviewBackground(@NonNull final TypedArray typedArray) {
        int backgroundColor = typedArray
                .getColor(R.styleable.AbstractColorPickerPreference_previewBackground, -1);

        if (backgroundColor != -1) {
            setPreviewBackgroundColor(backgroundColor);
        } else {
            int resourceId = typedArray
                    .getResourceId(R.styleable.AbstractColorPickerPreference_previewBackground,
                            R.drawable.color_picker_default_preview_background);
            setPreviewBackground(getContext().getResources().getDrawable(resourceId));
        }
    }

    /**
     * Obtains the format, which should be used to print a textual representation of the
     * preference's color, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color format should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainColorFormat(@NonNull final TypedArray typedArray) {
        int defaultValue = getContext().getResources()
                .getInteger(R.integer.color_picker_preference_default_color_format);
        setColorFormat(ColorFormat.fromValue(typedArray
                .getInteger(R.styleable.AbstractColorPickerPreference_colorFormat, defaultValue)));
    }

    /**
     * Creates and returns a textual representation of a color, according to a specific format.
     *
     * @param colorFormat
     *         The format, which should be used to format the color, as a value of the enum {@link
     *         ColorFormat}. The format may not be null
     * @param color
     *         The color, which should be formatted, as an {@link Integer} value
     * @return A textual representation of the given color as an instance of the type {@link
     * CharSequence}
     */
    private CharSequence formatColor(final ColorFormat colorFormat, @ColorInt final int color) {
        ensureNotNull(colorFormat, "The color format may not be null");

        if (colorFormat == ColorFormat.RGB) {
            return String.format(Locale.getDefault(), "R = %d, G = %d, B = %d", Color.red(color),
                    Color.green(color), Color.blue(color));
        } else if (colorFormat == ColorFormat.ARGB) {
            return String.format(Locale.getDefault(), "A = %d, R = %d, G = %d, B = %d",
                    Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
        } else if (colorFormat == ColorFormat.HEX_3_BYTES) {
            return String.format("#%06X", (0xFFFFFF & color));
        } else {
            return String.format("#%08X", (color));
        }
    }

    /**
     * Adapts the view, which is used to show a preview of the preference's color, depending on the
     * preference's properties and the currently persisted color.
     */
    private void adaptPreviewView() {
        if (previewView != null) {
            if (isPreviewShown()) {
                previewView.setVisibility(View.VISIBLE);
                previewView.setLayoutParams(createPreviewLayoutParams());
                previewLoader.load(getColor(), previewView);
            } else {
                previewView.setVisibility(View.INVISIBLE);
                previewView.setImageBitmap(null);
            }
        }
    }

    /**
     * Creates and returns the layout params of the view, which is used to show a preview of the
     * preference's color, depending on the preference's properties.
     *
     * @return The layout params, which have been created, as an instance of the class {@link
     * LayoutParams}
     */
    private LayoutParams createPreviewLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(getPreviewSize(), getPreviewSize());
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        return layoutParams;
    }

    /**
     * Creates a new preference, which allows to choose a color.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public AbstractColorPickerPreference(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new preference, which allows to choose a color.
     *
     * @param context
     *         The context, which should be used by the preference, as an instance of the class
     *         {@link Context}. The context may not be null
     * @param attributeSet
     *         The attributes of the XML tag that is inflating the preference, as an instance of the
     *         type {@link AttributeSet} or null, if no attributes are available
     */
    public AbstractColorPickerPreference(@NonNull final Context context,
                                         @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(attributeSet);
    }

    /**
     * Creates a new preference, which allows to choose a color.
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
    public AbstractColorPickerPreference(@NonNull final Context context,
                                         @Nullable final AttributeSet attributeSet,
                                         final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet);
    }

    /**
     * Creates a new preference, which allows to choose a color.
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
    public AbstractColorPickerPreference(@NonNull final Context context,
                                         @Nullable final AttributeSet attributeSet,
                                         final int defaultStyle, final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet);
    }

    /**
     * Returns the currently persisted color of the preference.
     *
     * @return The currently persisted color as an {@link Integer} value
     */
    public final int getColor() {
        return color;
    }

    /**
     * Sets the current color of the preference. By setting a value, it will be persisted.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setColor(@ColorInt final int color) {
        if (this.color != color) {
            this.color = color;
            persistInt(color);
            notifyChanged();
            adaptPreviewView();
        }
    }

    /**
     * Returns, whether a preview of the preference's color is shown, or not.
     *
     * @return True, if a preview of the preference's color is shown, false otherwise
     */
    public final boolean isPreviewShown() {
        return showPreview;
    }

    /**
     * Sets, whether a preview of the preference's color should be shown, or not.
     *
     * @param showPreview
     *         True, if a preview of the preference's color should be shown, false otherwise
     */
    public final void showPreview(final boolean showPreview) {
        this.showPreview = showPreview;
        adaptPreviewView();
    }

    /**
     * Returns the size of the preview of the preference's color.
     *
     * @return The size of the preview of the preference's color as an {@link Integer} value in
     * pixels
     */
    public final int getPreviewSize() {
        return previewSize;
    }

    /**
     * Sets the size of the preview of the preference's color.
     *
     * @param previewSize
     *         The size, which should be set, as an {@link Integer} value in pixels. The size must
     *         be at least 1
     */
    public final void setPreviewSize(final int previewSize) {
        ensureAtLeast(previewSize, 1, "The preview size must be at least 1");
        this.previewSize = previewSize;

        if (previewLoader != null) {
            previewLoader.setSize(previewSize);
        }
    }

    /**
     * Returns the shape of the preview of the preference's color.
     *
     * @return The shape of the preview of the preference's color as a value of the enum {@link
     * PreviewShape}. The shape may either be <code>CIRCLE</code> or <code>SQUARE</code>
     */
    public final PreviewShape getPreviewShape() {
        return previewShape;
    }

    /**
     * Sets the shape of the preview of the preference's color.
     *
     * @param previewShape
     *         The shape, which should be set, as a value of the enum {@link PreviewShape}. The
     *         shape may not be null
     */
    public final void setPreviewShape(@NonNull final PreviewShape previewShape) {
        ensureNotNull(previewShape, "The preview shape may not be null");
        this.previewShape = previewShape;

        if (previewLoader != null) {
            previewLoader.setShape(previewShape);
        }

        adaptPreviewView();
    }

    /**
     * Returns the border width of the preview of the preference's color.
     *
     * @return The border width of the preview of the preference's color as an {@link Integer} value
     * in pixels
     */
    public final int getPreviewBorderWidth() {
        return previewBorderWidth;
    }

    /**
     * Sets the border width of the preview of the preference's color.
     *
     * @param borderWidth
     *         The border width, which should be set, as an {@link Integer} value in pixels. The
     *         border width must be at least 0
     */
    public final void setPreviewBorderWidth(final int borderWidth) {
        ensureAtLeast(borderWidth, 0, "The border width must be at least 0");
        this.previewBorderWidth = borderWidth;

        if (previewLoader != null) {
            previewLoader.setBorderWidth(borderWidth);
        }

        adaptPreviewView();
    }

    /**
     * Returns the border color of the preview of the preference's color.
     *
     * @return The border color of the preview of the preference's color as an {@link Integer} value
     */
    public final int getPreviewBorderColor() {
        return previewBorderColor;
    }

    /**
     * Sets the border color of the preview of the preference's color.
     *
     * @param borderColor
     *         The border color, which should be set, as an {@link Integer} value
     */
    public final void setPreviewBorderColor(@ColorInt final int borderColor) {
        this.previewBorderColor = borderColor;

        if (previewLoader != null) {
            previewLoader.setBorderColor(borderColor);
        }

        adaptPreviewView();
    }

    /**
     * Returns the background of the preview of the preference's color.
     *
     * @return The background of the preview of the preference's color as an instance of the class
     * {@link Drawable}
     */
    public final Drawable getPreviewBackground() {
        return previewBackground;
    }

    /**
     * Sets the background of the preview of the preference's color.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Drawable} or
     *         null, if no background should be shown
     */
    public final void setPreviewBackground(final Drawable background) {
        this.previewBackground = background;

        if (previewLoader != null) {
            previewLoader.setBackground(background);
        }

        adaptPreviewView();
    }

    /**
     * Sets the background of the preview of the preference's color.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     */
    @SuppressWarnings("deprecation")
    public final void setPreviewBackground(@DrawableRes final int resourceId) {
        setPreviewBackground(getContext().getResources().getDrawable(resourceId));
    }

    /**
     * Sets the background color of the preview of the preference's color.
     *
     * @param backgroundColor
     *         The background color, which should be set, as an {@link Integer} value
     */
    public final void setPreviewBackgroundColor(@ColorInt final int backgroundColor) {
        setPreviewBackground(new ColorDrawable(backgroundColor));
    }

    /**
     * Returns the format, which is used to print a textual representation of the preference's
     * color.
     *
     * @return The format, which is used to print a textual representation of the preference's
     * color, as a value of the enum {@link ColorFormat}. The format may either be <code>RGB</code>,
     * <code>ARGB</code>, <code>HEX</code> or <code>AHEX</code>
     */
    public final ColorFormat getColorFormat() {
        return colorFormat;
    }

    /**
     * Sets the format, which should be used to print a textual representation of the preference's
     * color.
     *
     * @param colorFormat
     *         The format, which should be set, as a value of the enum {@link ColorFormat}. The
     *         format may not be null
     */
    public final void setColorFormat(@NonNull final ColorFormat colorFormat) {
        ensureNotNull(colorFormat, "The color format may not be null");
        this.colorFormat = colorFormat;
    }

    @Override
    public final CharSequence getSummary() {
        if (isValueShownAsSummary()) {
            return formatColor(getColorFormat(), getColor());
        } else {
            return super.getSummary();
        }
    }

    @Override
    protected final Object onGetDefaultValue(final TypedArray typedArray, final int index) {
        return typedArray.getInt(index, 0);
    }

    @Override
    protected final void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
        setColor(restoreValue ? getPersistedInt(getColor()) : (Integer) defaultValue);
    }

    @Override
    protected final View onCreateView(final ViewGroup parent) {
        View view = super.onCreateView(parent);
        LinearLayout widgetFrame = (LinearLayout) view.findViewById(android.R.id.widget_frame);
        widgetFrame.setVisibility(View.VISIBLE);
        previewView = new ImageView(getContext());
        widgetFrame.addView(previewView, createPreviewLayoutParams());
        adaptPreviewView();
        return view;
    }

    @Override
    protected final Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        if (!isPersistent()) {
            SavedState savedState = new SavedState(superState);
            savedState.color = getColor();
            return savedState;
        }

        return superState;
    }

    @Override
    protected final void onRestoreInstanceState(final Parcelable state) {
        if (state != null && state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            setColor(savedState.color);
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}