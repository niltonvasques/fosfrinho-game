package com.niltonvasques.fosfrinho.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.niltonvasques.fosfrinho.gameobject.GameObject;

public abstract class DrawComponent implements Component{

	
	public void draw(SpriteBatch batch, GameObject o){
		batch.draw(getRenderFrame(), o.getBounds().x, o.getBounds().y, o.getBounds().width , o.getBounds().height);
	}

	public abstract TextureRegion getRenderFrame();

}
