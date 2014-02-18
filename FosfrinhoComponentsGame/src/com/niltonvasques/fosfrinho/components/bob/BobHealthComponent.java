package com.niltonvasques.fosfrinho.components.bob;

import com.badlogic.gdx.Gdx;
import com.niltonvasques.fosfrinho.components.ContainerCom;
import com.niltonvasques.fosfrinho.components.HealthComponent;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.GameObject.Type;

public class BobHealthComponent extends HealthComponent{ 
	private static final String TAG = "[BobHealthComponent]";
	private static final boolean LOG = true;
	
	private boolean notCollide = false;
	
	public BobHealthComponent(GameObject o) {
		super(o);
		o.subscribeEvent(Message.DAMAGED, this);
		setHp(5);
	}
	
	@Override
	public void update(ContainerCom o, float delta) {
		super.update(o, delta);
		
		//TODO: need fix this
		if(!isDamaged() && notCollide){
			getGameObject().getNotCollidable().removeValue(Type.ZOMBIE, true);
			notCollide = false;
		}
	}
	
	@Override
	public void receive(Message m, Object... data) {
		switch (m) {
		case DAMAGED:
			if(LOG) Gdx.app.log(TAG, "DAMAGED");
			if(super.isAlive()){
				super.setDamaged(true);
				super.decreaseHp();
				getGameObject().addNotCollidableType(Type.ZOMBIE);
				notCollide = true;
			}else{
				if(LOG) Gdx.app.log(TAG, "DEAD");
				Gdx.app.log(TAG, "YOU LOSE!");	
			}
			break;

		default:
			break;
		}
		
	}

}
