package com.niltonvasques.fosfrinho.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.niltonvasques.fosfrinho.components.Component;
import com.niltonvasques.fosfrinho.components.ContainerCom;
import com.niltonvasques.fosfrinho.components.DrawComponent;
import com.niltonvasques.fosfrinho.components.comm.CommunicationCom;
import com.niltonvasques.fosfrinho.components.comm.Message;

public class GameObject extends ContainerCom implements CommunicationCom{
	
	public enum Type{
		BOB, BLOCK, ZOMBIE, DISPLAY
	}
	
	private Rectangle bounds; 
	private Vector2 acceleration = new Vector2();
	private Vector2 velocity = new Vector2();
	
	private Type type;
	
	private DrawComponent drawComponent;
	
	public GameObject(Type type, float x, float y, float width, float height) {
		this.type = type;
		this.bounds = new Rectangle(x, y, width, height);
	}

	public DrawComponent getDrawComponent() {
		return drawComponent;
	}

	public void setDrawComponent(DrawComponent drawComponent) {
		this.drawComponent = drawComponent;
	}

	@Override
	public boolean addComponent(Component component) {
		
		if(component instanceof DrawComponent){
			drawComponent = (DrawComponent) component;
		}
		
		return super.addComponent(component);
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
	
	public void send(Message message){
		if(getComponents() != null){
			for(int i = 0; i < COMPONENTS_MAX_CAPACITY; i++){
				if(getComponents()[i] != null){
					getComponents()[i].receive(message);
				}
			}
		}		
	}
	
	public void update(float delta){
		if(getComponents() != null){
			for(int i = 0; i < COMPONENTS_MAX_CAPACITY; i++){
				if(getComponents()[i] != null){
					getComponents()[i].update(this,delta);
				}
			}
		}		
	}

	public Type getType() {
		return type;
	}
	
}
