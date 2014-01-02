package com.niltonvasques.starassault.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Zombie {

	public enum State {
		IDLE, WALKING, JUMPING, WALL,DIED
	}

	public final static float SPEED = 1f; // unit per second
	public final static float SIZE = 1f; // half a unit

	private Vector2 position = new Vector2();
	private Vector2 acceleration = new Vector2();
	private Vector2 velocity = new Vector2();
	private Rectangle bounds = new Rectangle();
	private State state = State.IDLE;
	private int hp;
	boolean facingLeft = true;
	private float stateTime = 0;
	
	public Zombie(Vector2 pos) {
		this.position = pos;
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
		this.velocity.x = SPEED;
		this.hp = 10;
	}
	
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public void decreaseHp(){
		this.hp--;
	}

	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
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
	public Rectangle getBounds() {
		bounds.x = position.x;
		bounds.y = position.y;
		return bounds;
	}
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public boolean isFacingLeft() {
		return facingLeft;
	}
	public void setFacingLeft(boolean facingLeft) {
		this.facingLeft = facingLeft;
	}
	public float getStateTime() {
		return stateTime;
	}
	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}
	
	public abstract void updateZombie(float delta);
	
	public void reveserXDirection(){
		velocity.x *= -1;
	}

	public void reverseYDirection() {
		velocity.y *= -1;
	}
}
