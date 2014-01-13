package com.niltonvasques.fosfrinho.components.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.niltonvasques.fosfrinho.components.Message;
import com.niltonvasques.fosfrinho.components.PhysicsComponent;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.physics.PhysicsManager;
import com.niltonvasques.fosfrinho.physics.PhysicsManager.SensorCollisionListener;

public class BobPhysicsComponent extends PhysicsComponent{
	
	private static final String TAG = "[BobPhysicsComponent]";
	
	private final static float SPEED = 4f; // unit per second
	private final static float JUMP_FORCE = 10f;
	private final static long MAX_TIME_PRESS_JUMP 	= 150l;
	
	private Body body;
	private boolean grounded = false;
	
	private int numFootsOnGround = 0;
	
	public BobPhysicsComponent(GameObject o) {
		super(o);
		body = PhysicsManager.instance.registerDynamicBody(o, "data/fosfrinho.json", "bob");
		PhysicsManager.instance.attachSensorToBody(body, o.getBounds().width, new SensorCollisionListener() {
			
			@Override
			public void onEndContact() {
				Gdx.app.log(TAG, "onEndContact");
				numFootsOnGround--;
				if(numFootsOnGround <= 0){
					grounded = false;
				}				
			}
			
			@Override
			public void onBeginContact() {
				Gdx.app.log(TAG, "onBeginContact");
				numFootsOnGround++;
				if(numFootsOnGround > 0){
					grounded = true;
				}
			}
		});
	}
	
	@Override
	public void update(GameObject o, float delta) {
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
						
		case BTN_LEFT_PRESSED:
			if(body.getLinearVelocity().x > -SPEED ){
				body.applyLinearImpulse(-0.10f, 0, body.getPosition().x, body.getPosition().y, true);
			}
			break;
			
		case BTN_RIGHT_PRESSED:
			if(body.getLinearVelocity().x < SPEED ){
				body.applyLinearImpulse(0.10f, 0, body.getPosition().x, body.getPosition().y, true);
			}
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
