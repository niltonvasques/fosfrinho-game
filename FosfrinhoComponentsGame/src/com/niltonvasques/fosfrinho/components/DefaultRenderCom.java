package com.niltonvasques.fosfrinho.components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.util.resources.Assets;

public class DefaultRenderCom extends DrawComponent{
	private TextureRegion defaultRegion;
	
	public DefaultRenderCom(GameObject o, TextureRegion defaultRegion) {
		super(o);
		this.defaultRegion = defaultRegion;
	}

	@Override
	public void receive(Message m, Object... data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TextureRegion getRenderFrame() {
		return defaultRegion;
	}

	@Override
	public void update(ContainerCom o, float delta) {
		// TODO Auto-generated method stub
		
	}

}
