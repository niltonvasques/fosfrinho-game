package com.niltonvasques.starassault.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class CataZombie extends Zombie{
	private static final String TAG = "[CataZombie]";
	private static boolean LOG = false;

	private static final float SIZE = 1f;
	
	public CataZombie(Vector2 pos) {
		super(pos);
		getBounds().height = SIZE;
		getBounds().width = SIZE;
		getVelocity().x = 0;
		getVelocity().y = 0.1f;
	}
	
	@Override
	public void updateZombie(float delta) {
		if(LOG) Gdx.app.log(TAG, "updateZombie");
		setStateTime(getStateTime()+delta);
	}

}
