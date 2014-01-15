package com.niltonvasques.fosfrinho.components.bob;

import com.badlogic.gdx.Gdx;
import com.niltonvasques.fosfrinho.components.Component;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.Property;

public class BobMoveCom implements Component{
	
	private static final String TAG = "[BobMoveComponent]";
	private static final boolean LOG = false;
	
	public final static float SPEED = 4f; // unit per second
	public final static float JUMP_VELOCITY = 7f;
	private final static long MAX_TIME_PRESS_JUMP 	= 150l;
	
	private GameObject object;
	
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean jumpPressed = false;
	private long jumpPressedTime = 0;
	
	public BobMoveCom(GameObject o) {
		this.object = o;
		
		if(!o.getProperties().containsKey("FACING_LEFT")){
			o.getProperties().put("FACING_LEFT",new Property("FACING_LEFT",true));
		}
		
		o.subscribeEvent(Message.BTN_LEFT_PRESSED, this);
		o.subscribeEvent(Message.BTN_RIGHT_PRESSED, this);
	}
	
	@Override
	public void update(GameObject o, float delta) {
		if(leftPressed && !rightPressed){
			o.send(Message.FACING_LEFT);
			o.getProperties().get("FACING_LEFT").value = true;
		}
		
		if(!leftPressed && rightPressed){
			o.send(Message.FACING_RIGHT);
			o.getProperties().get("FACING_LEFT").value = false;
		}
		
		if(!leftPressed && !rightPressed){
		}
		
		if(jumpPressed){
		}
		
//		o.getVelocity().y += -10*delta;
		
		if(jumpPressed && ((System.currentTimeMillis() - jumpPressedTime) >= MAX_TIME_PRESS_JUMP)){
			jumpPressed = false;
		}
		
		leftPressed = false;
		rightPressed = false;
		jumpPressed = false;
	}

	@Override
	public void receive(Message m) {
		if(LOG) Gdx.app.log(TAG, "Message receive: "+m);
		
		switch (m) {
		case BTN_LEFT_PRESSED:
			leftPressed = true;
			break;
			
		case BTN_RIGHT_PRESSED:
			rightPressed = true;
			break;
			
//		case BOB_JUMP_PRESSED:
//			jumpPressed = true;
//		    jumpPressedTime = System.currentTimeMillis();
//			break;
//			
//		case BOB_JUMP_RELEASED:
//			jumpPressed = false;			
//			break;
		default:
			break;
		}
		
	}
	
	@Override
	public GameObject getGameObject() {
		return object;
	}

}
