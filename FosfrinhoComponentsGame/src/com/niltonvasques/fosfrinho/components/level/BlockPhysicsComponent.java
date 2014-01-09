package com.niltonvasques.fosfrinho.components.level;

import com.badlogic.gdx.physics.box2d.Body;
import com.niltonvasques.fosfrinho.components.Message;
import com.niltonvasques.fosfrinho.components.PhysicsComponent;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.physics.PhysicsManager;

public class BlockPhysicsComponent extends PhysicsComponent{
	
	private Body body;
	
	public BlockPhysicsComponent(GameObject o) {
		super(o);
		body = PhysicsManager.instance.registerCollisionComponent(this,true);		
	}

	@Override
	public void receive(Message m) {
		switch (m) {
		}
		
	}

	@Override
	public Body getBody() {
		return body;
	}

}
