package com.niltonvasques.fosfrinho.components.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.niltonvasques.fosfrinho.components.DrawComponent;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.Property;
import com.niltonvasques.fosfrinho.util.resources.Assets;

public class BobRenderCom extends DrawComponent{
	
	public enum State {
		IDLE, WALKING, JUMPING, FALLING, DYING
	}
	
	private static final String TAG = "[BobRenderComponent]";
	private static final boolean LOG = false;
	
	private State state = State.IDLE;
	
	public BobRenderCom(GameObject o) {
		super(o);
		
		if(!o.getProperties().containsKey("FACING_LEFT")){
			o.getProperties().put("FACING_LEFT",new Property("FACING_LEFT",true));
		}
		
		if(!o.getProperties().containsKey("FACING_LEFT")){
			o.getProperties().put("FACING_LEFT",new Property("FACING_LEFT",true));
		}
		
		o.subscribeEvent(Message.WALKING, this);
		o.subscribeEvent(Message.IDLE, this);
		o.subscribeEvent(Message.BOB_FALLING, this);
		o.subscribeEvent(Message.BOB_JUMPING, this);
		o.subscribeEvent(Message.DAMAGED, this);
	}
	
	@Override
	public void receive(Message m, Object... data) {
		if(LOG) Gdx.app.log(TAG, "Message receive: "+m);
		switch (m) {
			
		case WALKING:
			state = State.WALKING;
			break;
			
		case IDLE:
			state = State.IDLE;
			break;
			
		case BOB_FALLING:
			state = State.FALLING;
			break;
			
		case BOB_JUMPING:
			state = State.JUMPING;
			break;

		default:
			break;
		}
		
	}

	@Override
	public TextureRegion getRenderFrame() {
		TextureRegion frame;
		Property<Boolean> damaged = getGameObject().getProperties().get("DAMAGED");
		boolean facingLeft = (Boolean) getGameObject().getProperties().get("FACING_LEFT").value;
		
		switch (state) {
		case IDLE:
			if(damaged.value){
				frame = (facingLeft ? Assets.instance.bob.bobIdleDamagedLeftAnimation.getKeyFrame(getStateTime(), true) 
						: Assets.instance.bob.bobIdleDamagedRightAnimation.getKeyFrame(getStateTime(), true) );
			}else{
				frame = (facingLeft ? Assets.instance.bob.bobIdleLeftRegion : Assets.instance.bob.bobIdleRightRegion);
			}
			break;
			
		case WALKING:
			if(damaged.value){
				frame = (facingLeft ? Assets.instance.bob.bobWalkingDamagedLeftAnimation.getKeyFrame(getStateTime(), true) 
						: Assets.instance.bob.bobWalkingDamagedRightAnimation.getKeyFrame(getStateTime(), true) );
			}else{
				frame = (facingLeft ? Assets.instance.bob.bobWalkingLeftAnimation.getKeyFrame(getStateTime(),true) : 
					Assets.instance.bob.bobWalkingRightAnimation.getKeyFrame(getStateTime(),true) );
			}
			break;
			
		case JUMPING:
			if(damaged.value){
				frame = (facingLeft ? Assets.instance.bob.bobJumpingDamagedLeftAnimation.getKeyFrame(getStateTime(),true) 
						: Assets.instance.bob.bobJumpingDamagedRightAnimation.getKeyFrame(getStateTime(),true));
			}else{
				frame = (facingLeft ? Assets.instance.bob.bobJumpLeftRegion : Assets.instance.bob.bobJumpRightRegion);
			}			
			break;
			
		case FALLING:
			if(damaged.value){
				frame = (facingLeft ? Assets.instance.bob.bobFallDamagedLeftAnimation.getKeyFrame(getStateTime(),true) 
						: Assets.instance.bob.bobFallDamagedRightAnimation.getKeyFrame(getStateTime(),true));
			}else{
				frame = (facingLeft ? Assets.instance.bob.bobFallLeftRegion : Assets.instance.bob.bobFallRightRegion);
			}
			break;

		default:
			frame = null;
			break;
		}
		
		return frame;
	}	

}
