package com.niltonvasques.starassault.model;

import com.badlogic.gdx.math.Rectangle;

public class Key {

	private static final float SIZE = 1f;
	private Rectangle bounds;
	private String lock;
	
	public Key(String lock, float x, float y) {
		this.bounds = new Rectangle(x, y, SIZE, SIZE);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	public String getLock() {
		return lock;
	}
	
}
