package com.niltonvasques.fosfrinho.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.fosfrinho.components.comm.CommunicationCom;

public abstract class GameObjectContainer {
	
	private static final String TAG = "[GameObjectContainer]";
	private static final boolean LOG = false;
	
	private Array<GameObject> gameObjects;
	private Array<GameObject> drawableObjects;
	
	private boolean drawPrepared = false;
	
	public GameObjectContainer() {
		gameObjects = new Array<GameObject>();
		drawableObjects = new Array<GameObject>();
	}

	public Array<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void addGameObject(GameObject o){
		gameObjects.add(o);
	}
	
	public Array<GameObject> selectDrawableObjects(int xCenter, int yCenter, int width, int height){
		if(LOG) Gdx.app.debug(TAG, "Zombies size: "+gameObjects.size);
		
		drawableObjects.clear();
		float x1,x2,y1,y2;
    	
    	x1 = xCenter- width;
    	x2 = xCenter + width;
    	
    	y1 = yCenter - height;
    	y2 = yCenter + height;
		
		Rectangle screenDrawableArea = new Rectangle(x1, y1, x2, y2);
		
		if(LOG) Gdx.app.debug(TAG, "screenDrawableArea: "+screenDrawableArea);
    	
		if(gameObjects != null){
	    	for(GameObject zombie: gameObjects){
	    		if(LOG) Gdx.app.debug(TAG, "zombie.getBounds(): "+zombie.getBounds());
	    		if(zombie.getBounds().overlaps(screenDrawableArea)){
	    			drawableObjects.add(zombie);
	    		}
	    	}
		}
		
		return drawableObjects;		
	}
	

	public void update(float delta) {
		for(GameObject o : gameObjects){
			for(Action a : o.getPendingActions()){
				resolvePendingAction(a);
				o.getPendingActions().removeValue(a, true);
			}
		}
		for(GameObject o : gameObjects){
			o.update(delta);
		}			
	}

	public void draw(SpriteBatch batch) {
		if(!drawPrepared){
			throw new RuntimeException("No objects to be rendered! You need call prepareDraw before!!");
		}
		
		for(GameObject o : drawableObjects){
			o.draw(batch);
		}		
		
		drawPrepared = false;
	}

	public void prepareDraw(int xCenter, int yCenter, int width, int height) {
		selectDrawableObjects(xCenter, yCenter, width, height);
		drawPrepared = true;
	}
	
	public abstract void resolvePendingAction(Action act);
	
}
