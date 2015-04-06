package com.leandroideias.baseutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Leandro on 18/4/2014.
 */
public class GridSquareLayout extends FrameLayout {


	public GridSquareLayout(Context context) {
		super(context);
		init(context);
	}

	public GridSquareLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@SuppressLint("NewApi")
	public GridSquareLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context){
		FrameLayout.LayoutParams paramsView = new FrameLayout.LayoutParams(600, 600);
		View view = new View(context);
		view.setBackgroundColor(Color.TRANSPARENT);
		view.setLayoutParams(paramsView);
		addView(view);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int min = (width < height ? width : height);

		setMeasuredDimension(min, min);
	}
}
