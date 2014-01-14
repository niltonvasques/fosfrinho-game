package com.niltonvasques.fosfrinho.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.niltonvasques.fosfrinho.gameobject.GameObject;

public abstract class PhysicsComponent implements Component{
	
	private GameObject gameObject;
	
	public Rectangle getBounds() {
		return gameObject.getBounds();
	}

	public PhysicsComponent(GameObject o) {
		gameObject = o;
	}

//	@Override
//	public void update(GameObject o, float delta) {	
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
