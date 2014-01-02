
package com.niltonvasques.starassault.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block {

	public static final float SIZE = 1f;

	private Vector2 position;
	private Rectangle bounds = new Rectangle();

	public Block(Vector2 pos) {
		this.setPosition(pos);
		bounds.setX(position.x);
		bounds.setY(position.y);
		this.bounds.width = SIZE;
		this.bounds.height = SIZE;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Rectangle getBounds() {
		return bounds;
	}
}
