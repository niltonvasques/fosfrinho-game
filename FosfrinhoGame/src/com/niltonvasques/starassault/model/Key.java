package com.niltonvasques.starassault.model;

import com.badlogic.gdx.math.Rectangle;

public class Key implements Item{

	private static final float SIZE = 0.5f;
	private Rectangle bounds;
	private String lock;
	
	public Key(String lock, float x, float y) {
		this.lock = lock;
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
