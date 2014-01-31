package com.niltonvasques.fosfrinho.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.niltonvasques.fosfrinho.gameobject.GameObject;

public abstract class DrawComponent implements Component{

	private GameObject object;
	private float stateTime = 0f; 
	
	public DrawComponent(GameObject o) {
		this.object = o;
	}
	
	@Override
	public void update(ContainerCom o, float delta) {
		stateTime += delta;
	}
	
	public void draw(SpriteBatch batch, GameObject o){
		batch.draw(getRenderFrame(), o.getBounds().x, o.getBounds().y, o.getBounds().width , o.getBounds().height);
	}

	public abstract TextureRegion getRenderFrame();

	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}
	
	@Override
	public GameObject getGameObject() {
		return object;
	}

}
