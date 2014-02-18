package com.niltonvasques.fosfrinho.components;

import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.Action;
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
		
		if(!o.getProperties().containsKey("HP")){
			o.getProperties().put("HP", new Property<Integer>("HP", hp));
		}
	}
	
	@Override
	public void update(ContainerCom o, float delta) {
		
		if(isDamaged()){
			damageStateTime += delta;
			if(damageStateTime > IMMUNITY_TIME){
				damageStateTime = 0f;
				damaged = false;
				((GameObject)o).getProperties().get("DAMAGED").value = false;
			}
		}
		
		if(!isAlive()){
			o.send(Message.DEAD, null);
			Action act = new Action(object, Action.Type.END_ROUND);
			object.registerPendingAction(act);
		}
	}
	
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
		object.getProperties().get("HP").value = hp;
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
		if(hp <= 0){
			alive = false;
		}else{
			this.hp--;
		}
		object.getProperties().get("HP").value = hp;
	}

	public void increaseHp(){
		this.hp++;
		object.getProperties().get("HP").value = hp;
	}
	
	@Override
	public GameObject getGameObject() {
		return object;
	}
	
	
}
