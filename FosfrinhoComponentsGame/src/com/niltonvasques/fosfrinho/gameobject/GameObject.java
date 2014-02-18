package com.niltonvasques.fosfrinho.gameobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.fosfrinho.components.Component;
import com.niltonvasques.fosfrinho.components.ContainerCom;
import com.niltonvasques.fosfrinho.components.DrawComponent;
import com.niltonvasques.fosfrinho.components.comm.CommunicationCom;
import com.niltonvasques.fosfrinho.components.comm.Message;

public class GameObject extends ContainerCom {
	
	public enum Type{
		BOB, NETWORK_BOB, BLOCK, ZOMBIE, DISPLAY, SHOOT
	}
	
	private Rectangle bounds; 
	private Type type;
	private List<DrawComponent> drawComponents = new ArrayList<DrawComponent>();

	private Map<String, Property> properties = new HashMap<String, Property>();
	
	private Array<Type> notCollidable = new Array<Type>();
	private Array<Action> pendingActions = new Array<Action>();
	
	
	public GameObject(Type type, float x, float y, float width, float height) {
		this.type = type;
		this.bounds = new Rectangle(x, y, width, height);
		this.properties.put("BOUNDS", new Property<Rectangle>("BOUNDS", bounds));
	}

//	public DrawComponent getDrawComponent() {
//		return drawComponent;
//	}
//
//	public void setDrawComponent(DrawComponent drawComponent) {
//		this.drawComponent = drawComponent;
//	}

	@Override
	public boolean addComponent(Component component) {
		
		if(component instanceof DrawComponent){
			drawComponents.add((DrawComponent)component);
		}
		
		return super.addComponent(component);
	}
	
	public void draw(SpriteBatch batch){
		for (DrawComponent comm : drawComponents) {
			comm.draw(batch, this);
		}
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
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

	public Array<Type> getNotCollidable() {
		return notCollidable;
	}
	
	public void addNotCollidableType(Type t){
		notCollidable.add(t);
	}

	public Array<Action> getPendingActions() {
		return pendingActions;
	}

	public void registerPendingAction(Action a){
		pendingActions.add(a);
	}

	public void detachComponent(Component com) {
		for(int i = 0; i < COMPONENTS_MAX_CAPACITY; i++){
			if(getComponents()[i] != null && com == getComponents()[i]){
				getComponents()[i] = null;
			}
		}		
	}
	
	public void detachAllComponents() {
		for(int i = 0; i < COMPONENTS_MAX_CAPACITY; i++){
			if(getComponents()[i] != null){
				getComponents()[i] = null;
			}
		}		
	}

	public Map<String, Property> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Property> properties) {
		this.properties = properties;
	}

}
