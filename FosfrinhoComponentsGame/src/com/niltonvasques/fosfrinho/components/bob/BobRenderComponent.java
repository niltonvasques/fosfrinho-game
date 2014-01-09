package com.niltonvasques.fosfrinho.components.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.niltonvasques.fosfrinho.components.DrawComponent;
import com.niltonvasques.fosfrinho.components.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.util.Assets;

public class BobRenderComponent extends DrawComponent{
	private static final String TAG = "[BobRenderComponent]";
	
	private boolean facingLeft = true;
	
	@Override
	public void update(GameObject o, float delta) {
		
	}

	@Override
	public void receive(Message m) {
		Gdx.app.log(TAG, "Message receive: "+m);
		switch (m) {
		case BOB_FACING_LEFT:
			facingLeft = true;
			break;
			
		case BOB_FACING_RIGHT:
			facingLeft = false;
			break;

		default:
			break;
		}
		
	}

	@Override
	public TextureRegion getRenderFrame() {
		return (facingLeft ? Assets.instance.bob.bobIdleLeftRegion : Assets.instance.bob.bobIdleRightRegion);
	}	

}
