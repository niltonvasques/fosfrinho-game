package com.niltonvasques.fosfrinho.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.physics.PhysicsManager;

public abstract class PhysicsComponent implements Component{
	private static final String TAG = "[PhysicsComponent]";
	private GameObject gameObject;
	
	public Rectangle getBounds() {
		return gameObject.getBounds();
	}

	public PhysicsComponent(GameObject o) {
		gameObject = o;
		o.subscribeEvent(Message.DISPOSE, this);
	}
	
	@Override
	public void receive(Message m, Object... data) {
		switch(m){
		case DISPOSE:
			Gdx.app.log(TAG, "DISPOSE");
			PhysicsManager.instance.destroyBody(getBody());
			break;
		}
		
	}

//	@Override
//	public void update(ContainerCom o, float delta) {	
//		
////		o.getBounds().x = (getBody().getPosition().x-o.getBounds().width*0.5f);
////		o.getBounds().y = (getBody().getPosition().y-o.getBounds().height*0.5f);
//		
//	}
	
	public abstract Body getBody();

	public GameObject getGameObject() {
		return gameObject;
	}

}
