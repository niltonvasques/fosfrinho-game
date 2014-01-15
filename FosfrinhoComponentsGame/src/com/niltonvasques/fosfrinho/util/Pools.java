package com.niltonvasques.fosfrinho.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Pool;
import com.niltonvasques.fosfrinho.physics.PhysicsManager;

public class Pools {
	
	public static Pool<Vector2> vectorPool = new Pool<Vector2>(){
		@Override
		protected Vector2 newObject() {
			return new Vector2();
		}
	};
	
	
	
//	private static final Array<Vector2> vectorPool = new Array<Vector2>();
//	
//	public static Vector2 obtain() {
//        return vectorPool.size == 0 ? new Vector2() : vectorPool.removeIndex(0);
//	}
//
//	public static void free(Vector2 v) {
//        vectorPool.add(v);
//	}
}
