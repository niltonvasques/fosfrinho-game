package com.niltonvasques.fosfrinho.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.fosfrinho.components.Component;
import com.niltonvasques.fosfrinho.components.DrawComponent;
import com.niltonvasques.fosfrinho.components.Message;


public class GameObject {
	
	public static final int COMPONENTS_MAX_CAPACITY = 10;
	
	private Rectangle bounds; 
	private Vector2 acceleration = new Vector2();
	private Vector2 velocity = new Vector2();
	
	private DrawComponent drawComponent;

	private Component[] components;
	
	public GameObject( float x, float y, float width, float height) {
		this.components = new Component[COMPONENTS_MAX_CAPACITY];
		for(int i = 0; i < COMPONENTS_MAX_CAPACITY; i++){
			this.components[i] = null;
		}
		this.bounds = new Rectangle(x, y, width, height);
	}
	
	public Component[] getComponents() {
		return components;
	}

	public DrawComponent getDrawComponent() {
		return drawComponent;
	}

	public void setDrawComponent(DrawComponent drawComponent) {
		this.drawComponent = drawComponent;
	}

	public boolean addComponent(Component component) {
		
		if(component instanceof DrawComponent){
			drawComponent = (DrawComponent) component;
		}
		
		for(int i = 0; i < COMPONENTS_MAX_CAPACITY; i++){
			if(this.components[i] == null){
				this.components[i] = component;
				return true;
			}
		}
		
		return false;
	}

	public void send(Message message){
		
		if(components != null){
			for(int i = 0; i < COMPONENTS_MAX_CAPACITY; i++){
				if(this.components[i] != null){
					components[i].receive(message);
				}
			}
		}		
	}
	
	public void update(float delta){
		if(components != null){
			for(int i = 0; i < COMPONENTS_MAX_CAPACITY; i++){
				if(this.components[i] != null){
					components[i].update(this,delta);
				}
			}
		}		
	}
	
	public void draw(SpriteBatch batch){
		if(drawComponent != null){
			drawComponent.draw(batch, this);
		}
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public Vector2 getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2 acceleration) {
		this.acceleration = acceleration;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	
}
