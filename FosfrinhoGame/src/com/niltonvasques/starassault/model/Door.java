package com.niltonvasques.starassault.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class Door {
	
	private static final String TAG = "[Door]";
	private static final boolean LOG = true;
	
	private static final float SIZE = 0.5f;

	private Rectangle bounds;
	private boolean open;
	private String lock;
	
	public Door(String lock, float x, float y) {
		this.lock = lock;
		this.bounds = new Rectangle(x, y, SIZE, SIZE*2f);
		this.open = false;
	}

	public boolean isOpen() {
		return open;
	}
	
	public boolean open(Key key){
		
		if(lock.equals(key.getLock())){
			if(LOG)Gdx.app.log(TAG, "Door is opened!");
			open = true;
			return true;
		}
		if(LOG)Gdx.app.log(TAG, "The door is not have opened, because key is not valid!!");
		return false;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	
}
