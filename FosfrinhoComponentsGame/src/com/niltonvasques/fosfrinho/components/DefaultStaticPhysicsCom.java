package com.niltonvasques.fosfrinho.components;

import com.badlogic.gdx.physics.box2d.Body;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.physics.PhysicsManager;

public class DefaultStaticPhysicsCom extends PhysicsComponent{
	
	private Body body;
	public DefaultStaticPhysicsCom(GameObject o) {
		super(o);
		body = PhysicsManager.instance.registerStaticBody(o);
	}

	@Override
	public void update(ContainerCom o, float delta) {
		
	}

	@Override
	public Body getBody() {
		return body;
	}

}
