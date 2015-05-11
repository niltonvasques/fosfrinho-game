package com.niltonvasques.fosfrinho.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.fosfrinho.FosfrinhoGame;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.Action;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.GameObjectContainer;
import com.niltonvasques.fosfrinho.gameobject.GameObjectFactory;
import com.niltonvasques.fosfrinho.gameobject.Property;
import com.niltonvasques.fosfrinho.ui.screens.MenuScreen;
import com.niltonvasques.fosfrinho.util.Pools;

public class Level extends GameObjectContainer {
	
	private static final String TAG = "[Level]";
	private static final boolean LOG = false;
	
	private int width;
	private int height;
	private Vector2 spanPosition;
	private long levelClearTime;
	private GameObject bob;
	private GameObject display;
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
			if(act.data != null && act.data[0] instanceof Vector2){
				Property<Boolean> p  = act.owner.getProperties().get("FACING_LEFT");
				Vector2 pos = (Vector2) act.data[0];
				GameObject shoot = GameObjectFactory.createShootGameObject(pos.x, pos.y, p.value);
				addGameObject(shoot);
				
				send(Message.FIRE, pos.x, pos.y, p.value);
				
				Pools.vectorPool.free(pos);
			}else{
				throw new RuntimeException("Action "+act.type+" must have a Vector2 in data!");
			}
			break;
			
		case NETWORK_SHOOT:
			float x = (Float) act.data[0];
			float y = (Float) act.data[1];
			boolean left = (Boolean) act.data[2];
			GameObject shoot = GameObjectFactory.createShootGameObject(x, y, left);
			addGameObject(shoot);
			break;

		case DESTROY_GAME_OBJ:
			act.owner.detachAllComponents();
			getGameObjects().removeValue(act.owner, true);			
			break;
			
		case ADD_STAGE:
			display.send(Message.ADD_STAGE,act.data);
			break;
			
		case END_ROUND:
			Gdx.app.log(TAG, "END_ROUND");
			
			Array<GameObject> objs = getGameObjects();
			for(int i = 0; i < objs.size; i++){
				objs.get(i).send(Message.DISPOSE);
			}
			
			send(Message.DISPOSE);
			Screen screen = ((FosfrinhoGame)Gdx.app.getApplicationListener()).getScreen();
			((FosfrinhoGame)Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
			screen.dispose();
			break;
			
		default:
			break;
		}
	}
	
	public void setDisplayGameObject(GameObject display){
		this.display = display;
	}
	
	
}
