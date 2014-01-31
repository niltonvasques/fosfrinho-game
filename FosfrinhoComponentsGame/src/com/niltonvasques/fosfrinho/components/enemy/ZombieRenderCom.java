package com.niltonvasques.fosfrinho.components.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.niltonvasques.fosfrinho.components.DrawComponent;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.util.resources.Assets;

public class ZombieRenderCom extends DrawComponent{
	
	public enum State {
		IDLE, WALKING, DYING
	}
	
	private static final String TAG = "[BobRenderComponent]";
	private static final boolean LOG = false;
	
	private boolean facingLeft = true;
	private State state = State.IDLE;
	
	public ZombieRenderCom(GameObject o) {
		super(o);
		
		o.subscribeEvent(Message.FACING_LEFT, this);
		o.subscribeEvent(Message.FACING_RIGHT, this);
		o.subscribeEvent(Message.WALKING, this);
	}
	
	@Override
	public void receive(Message m) {
		if(LOG) Gdx.app.log(TAG, "Message receive: "+m);
		switch (m) {		
		
		case FACING_LEFT:
			facingLeft = true;
			break;
			
		case FACING_RIGHT:
			facingLeft = false;
			break;
		
		case WALKING:
			state = State.WALKING;
			break;

		default:
			break;
		}
		
	}

	@Override
	public TextureRegion getRenderFrame() {
		TextureRegion frame;
		
		switch (state) {
		case IDLE:
			frame = (facingLeft ? Assets.instance.enemys.zombieRegion : Assets.instance.enemys.zombieRegion);
			break;
			
		case WALKING:
			frame = (facingLeft ? Assets.instance.enemys.zombieWalkingLeftAnimation.getKeyFrame(getStateTime(),true) : 
				Assets.instance.enemys.zombieWalkingRightAnimation.getKeyFrame(getStateTime(),true) );
			break;
			
		default:
			frame = null;
			break;
		}
		
		return frame;
	}	
	
}
