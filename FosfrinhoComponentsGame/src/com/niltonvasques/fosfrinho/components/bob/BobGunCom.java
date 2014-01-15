package com.niltonvasques.fosfrinho.components.bob;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.niltonvasques.fosfrinho.components.Component;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.Action;
import com.niltonvasques.fosfrinho.gameobject.Action.Type;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.Property;
import com.niltonvasques.fosfrinho.util.Pools;

public class BobGunCom implements Component{

	private GameObject object;
	
	private long lastShoot = 0;
	
	private int shootsPerSecond = 3;
	
	public BobGunCom(GameObject o) {
		this.object = o;
		
		o.subscribeEvent(Message.FIRE, this);
	}
	
	@Override
	public void update(GameObject o, float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receive(Message m) {
		switch (m) {
		
		case FIRE:
			long time = TimeUtils.millis();
			if((time - lastShoot) >= (1000/shootsPerSecond)){
				
				lastShoot = time;
				Property<Boolean> p = object.getProperties().get("FACING_LEFT");
				Vector2 vec = Pools.vectorPool.obtain();
				vec.set(object.getBounds().x, object.getBounds().y+object.getBounds().width*0.35f);
				if(!p.value){
					vec.x += object.getBounds().width;
				}
				Action act = new Action(object,Type.CREATE_SHOOT, vec);
				object.registerPendingAction(act);
			}
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public GameObject getGameObject() {
		return object;
	}

}
