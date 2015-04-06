package com.leandroideias.transito.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by leandro.castro on 16/05/2014.
 */
public class MyImageView extends ImageView {
	public MyImageView(Context context) {
		super(context);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	float scaleFactor = 0;



	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if(!(getTag() instanceof MyImageViewTag)) throw new RuntimeException("Faltou setar a tag do MyImageView com o sendo uma instância de MyImageViewTag");
		MyImageViewTag tag = (MyImageViewTag) getTag();

//		View parent = ((View) getParent());
		float parentWidth = /*parent.getWidth()*/ 0;
		float parentHeight = /*parent.getHeight()*/ 0;

		if (parentWidth == 0 || parentHeight == 0){
			parentWidth = MeasureSpec.getSize(widthMeasureSpec);
			parentHeight = MeasureSpec.getSize(heightMeasureSpec);
		}

		float scaleXFactor = parentWidth / tag.getFullImageWidth();
		float scaleYFactor = parentHeight / tag.getFullImageHeight();

		scaleFactor = Math.min(scaleXFactor, scaleYFactor);
		float width = tag.getFullImageWidth();
		float height = tag.getFullImageHeight();

		int paddings[] = new int[]{
				(int) (tag.getStartX() * scaleFactor),
				(int) (tag.getStartY() * scaleFactor),
				(int) ((tag.getFullImageWidth() - tag.getStartX() - tag.getRegionWidth()) * scaleFactor),
				(int) ((tag.getFullImageHeight() - tag.getStartY() - tag.getRegionHeight()) * scaleFactor)};
		setPadding(paddings[0], paddings[1], paddings[2], paddings[3]);
		setMeasuredDimension((int) (width * scaleFactor), (int) (height * scaleFactor));
	}
}
