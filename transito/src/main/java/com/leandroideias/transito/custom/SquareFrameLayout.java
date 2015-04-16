package com.leandroideias.transito.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Leandro on 10/04/2015.
 */
public class SquareFrameLayout extends FrameLayout {
	public SquareFrameLayout(Context context) {
		super(context);
	}

	public SquareFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	/*
	 *
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (heightMode == MeasureSpec.UNSPECIFIED && widthMode == MeasureSpec.UNSPECIFIED) {
			//The view can be as big as possible.
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			int max = Math.max(getMeasuredWidth(), getMeasuredHeight());
			int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
			super.onMeasure(measureSpec, measureSpec);
		} else if (heightMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.UNSPECIFIED) {
			if (heightMode == MeasureSpec.UNSPECIFIED) {
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(widthSize, heightSize), MeasureSpec.EXACTLY);
			} else {
				widthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(widthSize, heightSize), MeasureSpec.EXACTLY);
			}
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		} else if (heightMode == MeasureSpec.AT_MOST && widthMode == MeasureSpec.AT_MOST) {
			//The view must be as small as possible.
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			int max = Math.max(getMeasuredWidth(), getMeasuredHeight());
			int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
			super.onMeasure(measureSpec, measureSpec);
		} else {
			int min = Math.min(widthSize, heightSize);
			int measureSpec = MeasureSpec.makeMeasureSpec(min, MeasureSpec.EXACTLY);
			super.onMeasure(measureSpec, measureSpec);
		}

		setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
	}
}