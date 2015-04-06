package com.leandroideias.transito.model;

import java.io.Serializable;

/**
 * Created by Leandro on 12/5/2014.
 */
public class ItemLevel implements Serializable{
	private int mundo;
	private int level;
	private int stars;
	private boolean completed;
	private boolean blocked;

	public ItemLevel(int mundo, int level, int stars, boolean completed, boolean blocked){
		this.mundo = mundo;
		this.level = level;
		this.stars = stars;
		this.completed = completed;
		this.blocked = blocked;
	}

	public int getMundo() {
		return mundo;
	}

	public void setMundo(int mundo) {
		this.mundo = mundo;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public boolean getCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

}
