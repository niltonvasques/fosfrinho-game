package com.niltonvasques.starassault.model;

import com.badlogic.gdx.math.Vector2;

public class NormalZombie extends Zombie{

	
	public NormalZombie(Vector2 pos) {
		super(pos);
		getVelocity().x = 0.2f;
	}
	
	@Override
	public void updateZombie(float delta) {
		getPosition().add(getVelocity().cpy().scl(delta));
	}

}
