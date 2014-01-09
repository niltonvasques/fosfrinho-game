package com.niltonvasques.fosfrinho.components;

import com.niltonvasques.fosfrinho.gameobject.GameObject;


public abstract class HealthComponent implements Component{
	
	private int hp;
	private boolean damaged = false;
	private float damageStateTime = 0f;
	
	@Override
	public void update(GameObject o, float delta) {
		
		if(isDamaged()){
			decreaseHp();
		}
		
		damaged = false;
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
	
	
}
