package com.niltonvasques.fosfrinho.components.bob.gun;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.niltonvasques.fosfrinho.components.DrawComponent;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.util.resources.Assets;

public class ShootRenderCom extends DrawComponent{

	public ShootRenderCom(GameObject o) {
		super(o);
	}
	@Override
	public void receive(Message m, Object... data) {
		
	}

	@Override
	public TextureRegion getRenderFrame() {
		return Assets.instance.shoots.shootRegion;
	}

}
