package com.awrtechnologies.valor.valorfireplace.uicomponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.awrtechnologies.valor.valorfireplace.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LoadImageFromAssets extends ImageView {

	public String TAG = "LIFA";

	public LoadImageFromAssets(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		if (this.isInEditMode()) {
			return;
		}
		init(context, attrs);
	}

	public LoadImageFromAssets(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (this.isInEditMode()) {
			return;
		}
		init(context, attrs);
	}

	public LoadImageFromAssets(Context context) {
		super(context);
	}

	void init(Context context, AttributeSet attrs) {

		TypedArray arr = context.obtainStyledAttributes(attrs,
				R.styleable.autoimage);
		boolean isMainMenuBackground = arr.getBoolean(
				R.styleable.autoimage_isMainMenuBackground, false);

		if (isMainMenuBackground) {
			ImageLoader.getInstance()
					.displayImage("assets://main_bg.png", this);
		}
		arr.recycle();
	}

}
