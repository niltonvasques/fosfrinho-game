package com.niltonvasques.starassault.model;

import com.badlogic.gdx.math.Rectangle;

public class Door {
	
	private static final float SIZE = 1f;

	private Rectangle bounds;
	private boolean open;
	private String lock;
	
	public Door(String lock, float x, float y) {
		this.lock = lock;
		this.bounds = new Rectangle(x, y, SIZE, SIZE);
		this.open = false;
	}

	public boolean isOpen() {
		return open;
	}
	
	public boolean open(Key key){
		
		if(lock.equals(key.getLock())){
			return true;
		}
		
		return false;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	
}
