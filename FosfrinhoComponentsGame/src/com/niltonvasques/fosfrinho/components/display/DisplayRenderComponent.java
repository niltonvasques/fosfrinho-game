package com.niltonvasques.fosfrinho.components.display;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.niltonvasques.fosfrinho.components.DrawComponent;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.view.FPSRenderer;

public class DisplayRenderComponent extends DrawComponent {
	
	private FPSRenderer fpsRenderer;
	
	public DisplayRenderComponent() {
		fpsRenderer = new FPSRenderer();
	}
	
	@Override
	public void update(GameObject o, float delta) {
		fpsRenderer.act();
	}

	@Override
	public void receive(Message m) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void draw(SpriteBatch batch, GameObject o) {
		fpsRenderer.draw();
	}

	@Override
	public TextureRegion getRenderFrame() {
		// TODO Auto-generated method stub
		return null;
	}

}
