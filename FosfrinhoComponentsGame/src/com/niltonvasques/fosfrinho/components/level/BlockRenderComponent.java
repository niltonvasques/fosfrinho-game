package com.niltonvasques.fosfrinho.components.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.niltonvasques.fosfrinho.components.DrawComponent;
import com.niltonvasques.fosfrinho.components.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.util.Assets;

public class BlockRenderComponent extends DrawComponent{


	@Override
	public void receive(Message m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TextureRegion getRenderFrame() {
		return Assets.instance.level.blockRegion;
	}

	@Override
	public void update(GameObject o, float delta) {
		// TODO Auto-generated method stub
		
	}

}
