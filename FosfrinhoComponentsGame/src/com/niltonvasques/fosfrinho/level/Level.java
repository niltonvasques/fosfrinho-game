package com.niltonvasques.fosfrinho.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.fosfrinho.gameobject.Action;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.GameObject.Type;
import com.niltonvasques.fosfrinho.gameobject.GameObjectContainer;
import com.niltonvasques.fosfrinho.gameobject.GameObjectFactory;
import com.niltonvasques.fosfrinho.gameobject.Property;
import com.niltonvasques.fosfrinho.util.Pools;

public class Level extends GameObjectContainer {
	
	private static final String TAG = "[Level]";
	private static final boolean LOG = false;
	
	private int width;
	private int height;
	private Vector2 spanPosition;
	private long levelClearTime;
	private GameObject bob;
	
	private GameObject networkBob; 

	public Level() {
		
	}

	public long getLevelClearTime() {
		return levelClearTime;
	}

	public void setLevelClearTime(long levelClearTime) {
		this.levelClearTime = levelClearTime;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Vector2 getSpanPosition() {
		return spanPosition;
	}

	public void setSpanPosition(Vector2 spanPosition) {
		this.spanPosition = spanPosition;
	}

	public GameObject getBob() {
		return bob;
	}
	
	public GameObject getNetworkBob() {
		return networkBob;
	}
	
	@Override
	public void addGameObject(GameObject o) {
		super.addGameObject(o);
		
		switch (o.getType()) {
		case BOB:
			bob = o;
			break;
			
		case NETWORK_BOB:
			networkBob = o;
			break;

		default:
			break;
		}
	}
	
	@Override
	public void resolvePendingAction(Action act) {
		Gdx.app.log(TAG,""+act.type);
		switch (act.type) {
		case CREATE_SHOOT:
			if(act.data != null && act.data instanceof Vector2){
				Property<Boolean> p  = act.owner.getProperties().get("FACING_LEFT");
				Vector2 pos = (Vector2) act.data;
				GameObject shoot = GameObjectFactory.createShootGameObject(pos.x, pos.y, p.value);
				addGameObject(shoot);
				
				Pools.vectorPool.free(pos);
			}else{
				throw new RuntimeException("Action "+act.type+" must have a Vector2 in data!");
			}
			break;

		case DESTROY_GAME_OBJ:
			act.owner.detachAllComponents();
			getGameObjects().removeValue(act.owner, true);			
			break;
		default:
			break;
		}
	}
	
	
}
