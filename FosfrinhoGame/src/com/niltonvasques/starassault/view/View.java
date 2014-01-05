package com.niltonvasques.starassault.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface View {

	public void drawView(SpriteBatch batch);
	
	public float getPositionX();
	public float getPositionY();
	
}
