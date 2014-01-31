package com.niltonvasques.fosfrinho.components.bob.gun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.fosfrinho.components.ContainerCom;
import com.niltonvasques.fosfrinho.components.PhysicsComponent;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.Action;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.GameObject.Type;
import com.niltonvasques.fosfrinho.physics.PhysicsManager;
import com.niltonvasques.fosfrinho.physics.PhysicsManager.SensorCollisionListener;

public class ShootPhysicsCom extends PhysicsComponent{
	
	private static final Array<Body> shootPool = new Array<Body>();
	
    private static Body newVec(GameObject o, SensorCollisionListener listener) {
    	if(shootPool.size == 0){
    		Body body = PhysicsManager.instance.registerDynamicBody(o);
    		body.setGravityScale(0);
    		PolygonShape polygon = new PolygonShape();
    		polygon.setAsBox(o.getBounds().width*0.5f, o.getBounds().height*0.5f, new Vector2(o.getBounds().width*0.5f, o.getBounds().height*0.5f),0);
    		
    		PhysicsManager.instance.attachSensorToBody(body, polygon, listener);
    		
    		polygon.dispose();
    		
		    return body;
    	}else{
    		Body body = shootPool.removeIndex(0);
	        body.setActive(true);
	        body.setTransform(o.getBounds().x, o.getBounds().y, 0);
	        for(Fixture f : body.getFixtureList()){
	        	if( f.isSensor() ){
	        		f.setUserData(listener);
	        	}
	        }
	        body.setUserData(o);
	        return body;
    	}
	}
	
	public static void free(Body v) {
		shootPool.add(v);
	}
	
	private static final String TAG = "[ShootPhysicsCom]";
	private static final boolean LOG = false;
	
	private final static float SPEED = 10f; // unit per second
	private Body body;
	private boolean leftDirection = false;
	
	public ShootPhysicsCom(GameObject o, boolean leftDirection) {
		super(o);
		
		this.leftDirection = leftDirection;
		
		body = PhysicsManager.instance.registerDynamicBody(o);
		body.setGravityScale(0);
		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(o.getBounds().width*0.5f, o.getBounds().height*0.5f, new Vector2(o.getBounds().width*0.5f, o.getBounds().height*0.5f),0);
		
		PhysicsManager.instance.attachSensorToBody(body, polygon, bodySensorListener);
		
		polygon.dispose();
	}
	
	@Override
	public void update(ContainerCom o, float delta) {
		body.setLinearVelocity(leftDirection ? -SPEED : SPEED, 0);
	}

	@Override
	public void receive(Message m) {
		Gdx.app.log(TAG, ""+m);
		switch (m) {

		default:
			break;
		}
		
	}

	@Override
	public Body getBody() {
		return body;
	}
	
	private SensorCollisionListener bodySensorListener = new SensorCollisionListener() {
		
		@Override
		public void onEndContact(GameObject o) {
			if(LOG) Gdx.app.log(TAG, "bodySensorListener onEndContact");
		}
		
		@Override
		public void onBeginContact(GameObject o) {
			if(LOG) Gdx.app.log(TAG, "bodySensorListener onBeginContact");
			if(o.getType() == Type.BLOCK){
				getGameObject().detachComponent(ShootPhysicsCom.this);
				getGameObject().registerPendingAction(new Action(getGameObject(),Action.Type.DESTROY_GAME_OBJ));
				PhysicsManager.instance.destroyBody(body);
			}
		}
	};
	
}
