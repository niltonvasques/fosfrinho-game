package com.niltonvasques.starassault.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bob {

	public enum State {
		IDLE, WALKING, JUMPING, DYING
	}

	public final static float SPEED = 4f; // unit per second
	public final static float JUMP_VELOCITY = 7f;
	public final static float SIZE = 0.5f; // half a unit
	public final static float IMMUNITY_TIME = 2f;
	private final static int DEFAULT_START_LIFE = 3;

	Vector2 position = new Vector2();
	Vector2 acceleration = new Vector2();
	Vector2 velocity = new Vector2();
	Rectangle bounds = new Rectangle();
	State state = State.IDLE;
	boolean facingLeft = true;
	float stateTime = 0;
	boolean longJump = false;
	private int hp = DEFAULT_START_LIFE;
	private boolean damaged = false;
	private float damageStateTime = 0f;

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public void decreaseHp(){
		this.hp--;
	}
	
	public void increaseHp(){
		this.hp++;
	}

	public Bob(Vector2 position) {
		this.position = position;
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
	}

	public boolean isFacingLeft() {
		return facingLeft;
	}

	public void setFacingLeft(boolean facingLeft) {
		this.facingLeft = facingLeft;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public State getState() {
		return state;
	}

	public void setState(State newState) {
		this.state = newState;
	}

	public float getStateTime() {
		return stateTime;
	}

	public boolean isLongJump() {
		return longJump;
	}

	public void setLongJump(boolean longJump) {
		this.longJump = longJump;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
		this.bounds.setX(position.x);
		this.bounds.setY(position.y);
	}

	public void setAcceleration(Vector2 acceleration) {
		this.acceleration = acceleration;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public void update(float delta) {
		// position.add(velocity.tmp().mul(delta));
		// bounds.x = position.x;
		// bounds.y = position.y;
		stateTime += delta;
		if(this.damaged){
			damageStateTime += delta;
			if(damageStateTime > IMMUNITY_TIME){
				damageStateTime = 0f;
				damaged = false;
			}
		}
	}

	public void setDamaged(boolean damaged) {
		this.damaged = damaged;
	}

	public boolean isDamaged() {
		return damaged;
	}

	public void clear() {
		stateTime = 0;
		damaged = false;
		damageStateTime = 0f;
		hp = DEFAULT_START_LIFE;
		state = State.IDLE;
		
	}

}
