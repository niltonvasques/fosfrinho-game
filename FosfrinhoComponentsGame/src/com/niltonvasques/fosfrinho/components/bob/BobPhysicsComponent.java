package com.niltonvasques.fosfrinho.components.bob;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.niltonvasques.fosfrinho.components.Message;
import com.niltonvasques.fosfrinho.components.PhysicsComponent;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.physics.PhysicsManager;

public class BobPhysicsComponent extends PhysicsComponent{
	
	public final static float SPEED = 4f; // unit per second
	public final static float JUMP_FORCE = 10f;
	private final static long MAX_TIME_PRESS_JUMP 	= 150l;
	
	private Body body;
	private boolean grounded;
	
	public BobPhysicsComponent(GameObject o) {
		super(o);
		body = PhysicsManager.instance.registerCollisionComponent(this,false,"Sensor");		
	}

	@Override
	public void receive(Message m) {
		switch (m) {
		case BOB_JUMP_PRESSED:
//			body.applyLinearImpulse(0, 2, body.getPosition().x, body.getPosition().y, true);
			if(grounded)
//				body.applyForceToCenter(new Vector2(0, 110), true);
			//ok to jump
				body.applyLinearImpulse( new Vector2(0, body.getMass() * JUMP_FORCE),body.getWorldCenter(), true);
			break;
			
		case GROUNDED:
			grounded = true;
			break;
			
		case FLYING:
			grounded = false;
			break;
			
		case BTN_LEFT_PRESSED:
			body.setLinearVelocity(-4f, body.getLinearVelocity().y);
//			body.applyLinearImpulse( new Vector2(-body.getMass() * 4,0),body.getWorldCenter(), true);
//			body.applyLinearImpulse(-1, 0, body.getPosition().x, body.getPosition().y, true);
			break;
			
		case BTN_RIGHT_PRESSED:
//			body.applyLinearImpulse( new Vector2(body.getMass() * 4,0),body.getWorldCenter(), true);
			body.setLinearVelocity(4f, body.getLinearVelocity().y);
//			body.applyLinearImpulse(1, 0, body.getPosition().x, body.getPosition().y, true);
			break;
			
		case BTN_RIGHT_RELEASED:
		case BTN_LEFT_RELEASED:
			body.setLinearVelocity(0f, body.getLinearVelocity().y);
			break;

		default:
			break;
		}
		
	}

	@Override
	public Body getBody() {
		return body;
	}

}
