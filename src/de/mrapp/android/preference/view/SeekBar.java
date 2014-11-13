package de.mrapp.android.preference.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

public class SeekBar extends android.widget.SeekBar {

	private Drawable thumb;

	public SeekBar(Context context) {
		super(context);
	}

	public SeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public SeekBar(Context context, AttributeSet attrs, int defStyle,
			int defStyleRes) {
		super(context, attrs, defStyle, defStyleRes);
	}

	@Override
	public void setThumb(Drawable thumb) {
		super.setThumb(thumb);
		this.thumb = thumb;
	}

	public Drawable getThumbDrawable() {
		return thumb;
	}

}