package com.niltonvasques.starassault.model;

import com.badlogic.gdx.math.Vector2;

public class NormalZombie extends Zombie{
	
	private static final float DAMAGED_TIME = 0.2f;
	private static final float DYING_TIME = 0.8f;
	
	private float damagedTime = 0f;
	private float dyingTime = 0f;
	
	public NormalZombie(Vector2 pos) {
		super(pos);
		getVelocity().x = 0.2f;
		getBounds().width = 0.5f;
		getBounds().height = 0.9f;
		setState(State.WALKING);
	}
	
	@Override
	public void updateZombie(float delta) {		
		setStateTime(getStateTime()+delta);
		
		switch (getState()) {
		case DAMAGED:
			damagedTime+= delta;
			if(damagedTime > DAMAGED_TIME){
				setState(State.WALKING);
				damagedTime = 0f;
			}
			break;
			
		case WALKING:
			getPosition().add(getVelocity().scl(delta));
			getVelocity().scl(1/delta);
			break;
			
		case DYING:
			dyingTime += delta;
			if(dyingTime > DYING_TIME){
				setState(State.DIED);
				dyingTime = 0f;
			}
			break;

		default:
			break;
		}
		
		
	}
	
	@Override
	public void decreaseHp() {
		super.decreaseHp();
		if(getHp() <= 0){
			setState(State.DYING);
			dyingTime = 0f;
			getBounds().width = 1f;
			getBounds().height = 0.68f;
			setStateTime(0f);
		}else{
			setState(State.DAMAGED);
			damagedTime = 0f;
		}
	}

}
