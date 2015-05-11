package com.niltonvasques.fosfrinho.components.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.niltonvasques.fosfrinho.components.ContainerCom;
import com.niltonvasques.fosfrinho.components.PhysicsComponent;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.GameObject.Type;
import com.niltonvasques.fosfrinho.physics.PhysicsManager;
import com.niltonvasques.fosfrinho.physics.PhysicsManager.SensorCollisionListener;
import com.niltonvasques.fosfrinho.util.resources.Resources;

public class BobPhysicsCom extends PhysicsComponent{
	
	private static final String TAG = "[BobPhysicsComponent]";
	private static final boolean LOG = false;
	
	private final static float SPEED = 4f; // unit per second
	private final static float JUMP_FORCE = 10f;
	private final static long MAX_TIME_PRESS_JUMP 	= 150l;
	
	private Body body;
	private boolean grounded = false;
	
	private boolean btnLeftPressed = false;
	private boolean btnRightPressed = false;
	
	private boolean dead = false;
	
	private int numFootsOnGround = 0;
	
	public BobPhysicsCom(GameObject o) {
		super(o);
		
		body = PhysicsManager.instance.registerDynamicBody(o, Resources.BODY_LOADER_JSON_PATH, "bob");
		PhysicsManager.instance.attachSensorToBody(body, o.getBounds().width, footSensorListener);
		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(o.getBounds().width*0.5f, o.getBounds().height*0.5f,
				new Vector2(o.getBounds().width*0.5f, o.getBounds().height*0.5f),0);
		PhysicsManager.instance.attachSensorToBody(body, polygon, bodySensorListener);
		
		getGameObject().subscribeEvent(Message.BOB_JUMP_PRESSED, this);
		getGameObject().subscribeEvent(Message.BTN_LEFT_PRESSED, this);
		getGameObject().subscribeEvent(Message.BTN_RIGHT_PRESSED, this);
		getGameObject().subscribeEvent(Message.BTN_LEFT_RELEASED, this);
		getGameObject().subscribeEvent(Message.BTN_RIGHT_RELEASED, this);
	}
	
	@Override
	public void update(ContainerCom o, float delta) {
		
		if(dead) return;
		
		if(btnLeftPressed && !btnRightPressed){
			if(body.getLinearVelocity().x > -SPEED ){
				body.applyLinearImpulse(-0.80f, 0, body.getPosition().x, body.getPosition().y, true);
			}
		}
		
		if(btnRightPressed && !btnLeftPressed){
			if(body.getLinearVelocity().x < SPEED ){
				body.applyLinearImpulse(0.80f, 0, body.getPosition().x, body.getPosition().y, true);
			}
		}
		
		if(grounded){
			
			if(body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0){
				getGameObject().send(Message.IDLE, null);
			}
			
			if(body.getLinearVelocity().x != 0 && body.getLinearVelocity().y == 0){
				getGameObject().send(Message.WALKING, null);
			}
			
		}else{
			if(body.getLinearVelocity().y < 0){
				o.send(Message.BOB_FALLING, null);
			}else if(body.getLinearVelocity().y > 0){
				o.send(Message.BOB_JUMPING, null);
			}
		}
		
	}

	@Override
	public void receive(Message m, Object... data) {
//		if(LOG) Gdx.app.log(TAG, ""+m);
		
		switch (m) {
		case BOB_JUMP_PRESSED:
			if(grounded)
				body.applyLinearImpulse( new Vector2(0, body.getMass() * JUMP_FORCE),body.getWorldCenter(), true);
			break;
						
		case BTN_LEFT_PRESSED:
			btnLeftPressed = true;
			break;
			
		case BTN_RIGHT_PRESSED:
			btnRightPressed = true;
			break;
			
		case BTN_RIGHT_RELEASED:
			btnRightPressed = false;
			body.setLinearVelocity(0f, body.getLinearVelocity().y);
			break;
			
		case BTN_LEFT_RELEASED:
			btnLeftPressed = false;
			body.setLinearVelocity(0f, body.getLinearVelocity().y);
			break;

		default:
			break;
		}
		
		super.receive(m, data);
		
	}

	@Override
	public Body getBody() {
		return body;
	}
	
	private SensorCollisionListener footSensorListener = new SensorCollisionListener() {
		
		@Override
		public void onEndContact(GameObject o) {
			if(LOG) Gdx.app.log(TAG, "onEndContact");
			if(o.getType() == Type.BLOCK){
				numFootsOnGround--;
				if(numFootsOnGround <= 0){
					grounded = false;
				}				
			}
		}
		
		@Override
		public void onBeginContact(GameObject o) {
			if(LOG) Gdx.app.log(TAG, "onBeginContact "+o.getType());
			if(o.getType() == Type.BLOCK){
				numFootsOnGround++;
				if(numFootsOnGround > 0){
					grounded = true;
				}
			}
			
			if(o.getType() == Type.ZOMBIE){
				getGameObject().send(Message.DAMAGED, null);
			}
		}
	};
	
	private SensorCollisionListener bodySensorListener = new SensorCollisionListener() {
		
		@Override
		public void onEndContact(GameObject o) {
			if(LOG) Gdx.app.log(TAG, "bodySensorListener onEndContact");
		}
		
		@Override
		public void onBeginContact(GameObject o) {
			if(LOG) Gdx.app.log(TAG, "bodySensorListener onBeginContact"+o.getType());
			if(o.getType() == Type.ZOMBIE){
				getGameObject().send(Message.DAMAGED, null);
			}
		}
	};
	
}
