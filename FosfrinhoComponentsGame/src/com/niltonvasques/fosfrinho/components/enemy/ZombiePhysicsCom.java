package com.niltonvasques.fosfrinho.components.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.niltonvasques.fosfrinho.components.ContainerCom;
import com.niltonvasques.fosfrinho.components.PhysicsComponent;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.Property;
import com.niltonvasques.fosfrinho.physics.PhysicsManager;
import com.niltonvasques.fosfrinho.physics.PhysicsManager.SensorCollisionListener;
import com.niltonvasques.fosfrinho.util.resources.Resources;

public class ZombiePhysicsCom extends PhysicsComponent{
	
	private static final String TAG = "[ZombiePhysicsComponent]";
	private static final boolean LOG = false;
	
	private final static float SPEED = 1f; // unit per second
	
	private Body body;
	private boolean grounded = false;
	
	private int numFootsOnGround = 0;
	
	public ZombiePhysicsCom(GameObject o) {
		super(o);
		body = PhysicsManager.instance.registerDynamicBody(o, Resources.BODY_LOADER_JSON_PATH, "zombie",100f, 0f);
		
		PhysicsManager.instance.attachWheelFixtureToBody(body, o.getBounds().width);
		
		PhysicsManager.instance.attachSensorToBody(body, o.getBounds().width, footSensorListener);
		
		if(!o.getProperties().containsKey("FACING_LEFT")){
			o.getProperties().put("FACING_LEFT",new Property("FACING_LEFT",true));
		}
		
		if(MathUtils.random(100) % 2 == 0){
			getGameObject().getProperties().get("FACING_LEFT").value = false;
		}else{
			getGameObject().getProperties().get("FACING_LEFT").value = true;
		}
	}

	@Override
	public void update(ContainerCom o, float delta) {
		if(grounded){
			
			boolean facingLeft = (Boolean) getGameObject().getProperties().get("FACING_LEFT").value;
			
			if(body.getLinearVelocity().x == 0){
				facingLeft = !facingLeft;
				getGameObject().getProperties().get("FACING_LEFT").value = facingLeft;
			}
			
			if(facingLeft && body.getLinearVelocity().x > -SPEED ){
//				body.applyLinearImpulse(-0.10f, 0, body.getPosition().x, body.getPosition().y, true);
				body.setLinearVelocity(-SPEED, body.getLinearVelocity().y);
				o.send(Message.WALKING, null);
				if(LOG) Gdx.app.log(TAG, "left run");
			}
			
			if(!facingLeft && body.getLinearVelocity().x < SPEED ){
//				body.applyLinearImpulse(0.10f, 0, body.getPosition().x, body.getPosition().y, true);
				body.setLinearVelocity(SPEED, body.getLinearVelocity().y);
				if(LOG) Gdx.app.log(TAG, "right run");
				o.send(Message.WALKING, null);
			}
			
			
			if(body.getLinearVelocity().x != 0 && body.getLinearVelocity().y == 0){
//				getGameObject().send(Message.BOB_WALKING);
			}
			
		}else{
			if(body.getLinearVelocity().y < 0){
//				o.send(Message.BOB_FALLING);
			}else if(body.getLinearVelocity().y > 0){
//				o.send(Message.BOB_JUMPING);
			}
			
		}
		
	}

	@Override
	public Body getBody() {
		return body;
	}
	
	private SensorCollisionListener footSensorListener = new SensorCollisionListener() {
		
		@Override
		public void onEndContact(GameObject o) {
			if(LOG) Gdx.app.log(TAG, "onEndContact");
			numFootsOnGround--;
			if(numFootsOnGround <= 0){
				grounded = false;
			}				
		}
		
		@Override
		public void onBeginContact(GameObject o) {
			if(LOG) Gdx.app.log(TAG, "onBeginContact ");
			numFootsOnGround++;
			if(numFootsOnGround > 0){
				grounded = true;
			}
		}
	};
	
}
