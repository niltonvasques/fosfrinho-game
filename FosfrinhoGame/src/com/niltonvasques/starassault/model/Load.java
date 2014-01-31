package com.niltonvasques.starassault.model;

import com.badlogic.gdx.math.Rectangle;

public class Load {
	
	private static final float SIZE = 0.5f;
	
	private int ammo;
	private Rectangle bounds;
	private float stateTime = 0;
	
	public Load(int ammo, float x, float y) {
		this.ammo = ammo;
		this.bounds = new Rectangle(x, y, SIZE, SIZE);
	}
	
	public Load(){
		
	}

	public int getMunition() {
		return ammo;
	}

	public void setMunition(int munition) {
		this.ammo = munition;
	}
	
	public boolean takeABullet(){
		if(ammo > 0){
			ammo--;
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
	
	public void update(float delta){
		stateTime+=delta;
	}

	public float getStateTime() {
		return stateTime;
	}

}
