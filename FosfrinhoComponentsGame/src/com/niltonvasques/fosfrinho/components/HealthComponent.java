package com.niltonvasques.fosfrinho.components;

import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.Property;


public abstract class HealthComponent implements Component{
	
	private final static float IMMUNITY_TIME = 2f;
	
	private int hp;
	private boolean damaged = false;
	private float damageStateTime = 0f;
	
	private GameObject object;
	
	public HealthComponent(GameObject o) {
		this.object = o;
		if(!o.getProperties().containsKey("DAMAGED")){
			o.getProperties().put("DAMAGED", new Property<Boolean>("DAMAGED", false));
		}
	}
	
	@Override
	public void update(GameObject o, float delta) {
		
		if(isDamaged()){
			damageStateTime += delta;
			if(damageStateTime > IMMUNITY_TIME){
				damageStateTime = 0f;
				damaged = false;
				o.getProperties().get("DAMAGED").value = false;
			}
		}
		
		if(!isAlive()){
			o.send(Message.DEAD);
		}
	}
	
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public boolean isDamaged() {
		return damaged;
	}
	public void setDamaged(boolean damaged) {
		this.damaged = damaged;
		object.getProperties().get("DAMAGED").value = damaged;
	}
	public float getDamageStateTime() {
		return damageStateTime;
	}
	public void setDamageStateTime(float damageStateTime) {
		this.damageStateTime = damageStateTime;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	private boolean alive = true;
	
	public void decreaseHp(){
		this.hp--;
	}

	public void increaseHp(){
		this.hp++;
	}
	
	@Override
	public GameObject getGameObject() {
		return object;
	}
	
	
}
