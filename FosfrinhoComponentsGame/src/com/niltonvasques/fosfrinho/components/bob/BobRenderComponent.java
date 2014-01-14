package com.niltonvasques.fosfrinho.components.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.niltonvasques.fosfrinho.components.DrawComponent;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.util.Assets;

public class BobRenderComponent extends DrawComponent{
	
	public enum State {
		IDLE, WALKING, JUMPING, FALLING, DYING
	}
	
	private static final String TAG = "[BobRenderComponent]";
	private static final boolean LOG = false;
	
	private boolean facingLeft = true;
	private State state = State.IDLE;
	
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
		
		switch (state) {
		case IDLE:
			frame = (facingLeft ? Assets.instance.bob.bobIdleLeftRegion : Assets.instance.bob.bobIdleRightRegion);
			break;
			
		case WALKING:
			frame = (facingLeft ? Assets.instance.bob.bobWalkingLeftAnimation.getKeyFrame(getStateTime(),true) : 
				Assets.instance.bob.bobWalkingRightAnimation.getKeyFrame(getStateTime(),true) );
			break;
			
		case JUMPING:
			frame = (facingLeft ? Assets.instance.bob.bobJumpLeftRegion : Assets.instance.bob.bobJumpRightRegion);
			break;
			
		case FALLING:
			frame = (facingLeft ? Assets.instance.bob.bobFallLeftRegion : Assets.instance.bob.bobFallRightRegion);
			break;

		default:
			frame = null;
			break;
		}
		
		return frame;
	}	

}
