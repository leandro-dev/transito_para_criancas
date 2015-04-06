package com.leandroideias.transito.custom;

import java.io.Serializable;

/**
 * Created by leandro.castro on 16/05/2014.
 */
public class MyImageViewTag implements Serializable{
	public MyImageViewTag(int imageResId, boolean positive, float fullImageWidth, float fullImageHeight, float regionWidth, float regionHeight, float startX, float startY){
		setImageResId(imageResId);
		setPositive(positive);
		setFullImageWidth(fullImageWidth);
		setFullImageHeight(fullImageHeight);
		setRegionWidth(regionWidth);
		setRegionHeight(regionHeight);
		setStartX(startX);
		setStartY(startY);
	}

	private int imageResId;
	private boolean positive;

	private float fullImageWidth;
	private float fullImageHeight;

	private float regionWidth;
	private float regionHeight;

	private float startX;
	private float startY;

	public int getImageResId() {
		return imageResId;
	}

	public void setImageResId(int imageResId) {
		this.imageResId = imageResId;
	}

	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}

	public float getFullImageWidth() {
		return fullImageWidth;
	}

	public void setFullImageWidth(float fullImageWidth) {
		this.fullImageWidth = fullImageWidth;
	}

	public float getFullImageHeight() {
		return fullImageHeight;
	}

	public void setFullImageHeight(float fullImageHeight) {
		this.fullImageHeight = fullImageHeight;
	}

	public float getRegionWidth() {
		return regionWidth;
	}

	public void setRegionWidth(float regionWidth) {
		this.regionWidth = regionWidth;
	}

	public float getRegionHeight() {
		return regionHeight;
	}

	public void setRegionHeight(float regionHeight) {
		this.regionHeight = regionHeight;
	}

	public float getStartX() {
		return startX;
	}

	public void setStartX(float startX) {
		this.startX = startX;
	}

	public float getStartY() {
		return startY;
	}

	public void setStartY(float startY) {
		this.startY = startY;
	}
}
