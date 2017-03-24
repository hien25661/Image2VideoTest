package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class ProductImageView extends ImageView {

	public ProductImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ProductImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public ProductImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		try {
			Drawable drawable = getDrawable();
			if (drawable == null) {
				setMeasuredDimension(0, 0);
			} else {

				int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
				int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
				if (measuredHeight == 0 && measuredWidth == 0) { // Height and
																	// // width
																	// set
																	// to
																	// wrap_content
					setMeasuredDimension(measuredWidth, measuredHeight);
					Log.e("custom 0", "" + measuredWidth + "va  "
							+ measuredHeight);

				} else if (measuredHeight == 0) { // Height set to wrap_content
					int width = measuredWidth;
					int height = width * drawable.getIntrinsicHeight()
							/ drawable.getIntrinsicWidth();
					setMeasuredDimension(width, height);
					Log.e("custom 1", "" + width + "va  " + height);

				} else if (measuredWidth == 0) { // Width set to wrap_content
					int height = measuredHeight;
					int width = height * drawable.getIntrinsicWidth()
							/ drawable.getIntrinsicHeight();
					setMeasuredDimension(width, height);
					Log.e("custom 2", "" + width + "va  " + height);

				} else { // Width and height are explicitly set (either to
							// match_parent or to exact value)
					setMeasuredDimension(measuredWidth, measuredHeight);
					// Log.e("custom 3", "" + measuredWidth + "va  "
					// + measuredHeight);
				}
			}
		} catch (Exception e) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

}
