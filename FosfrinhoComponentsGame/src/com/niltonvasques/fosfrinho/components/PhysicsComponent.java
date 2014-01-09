package com.niltonvasques.fosfrinho.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.niltonvasques.fosfrinho.gameobject.GameObject;

public abstract class PhysicsComponent implements Component{
	
	private Rectangle bounds;
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public PhysicsComponent(GameObject o) {
		bounds = o.getBounds();
	}

	@Override
	public void update(GameObject o, float delta) {	
		
		o.getBounds().x = (getBody().getPosition().x-o.getBounds().width*0.5f);
		o.getBounds().y = (getBody().getPosition().y-o.getBounds().height*0.5f);
		
	}
	
	public abstract Body getBody();


}
