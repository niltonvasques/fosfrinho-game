
package com.niltonvasques.starassault.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.starassault.controller.LevelLoader;

public class World {
	/** Our player controlled hero **/
	private Bob bob;
	/** Current level where bob are **/
	private Level level;
	
	private Array<Rectangle> collisionRects = new Array<Rectangle>();
	
	private boolean gameOver = false;

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	// Getters -----------
	public Bob getBob() {
		return bob;
	}
	// --------------------

	public World() {
		createWorld();
	}
	
	private void createWorld() {
		level = LevelLoader.loadLevel(4);
        bob = new Bob(level.getSpanPosition());
        bob.setGun(new Gun38());
        bob.getGun().reload(new Load(30, 0, 0));
        bob.clear();
	}

//	private void createDemoWorld() {
//		bob = new Bob(new Vector2(7, 2));
//		level = new Level();
//	}
	
	public Array<Block> getDrawableBlocks(int width, int height){
		int x = (int)bob.getPosition().x - width;
		int y = (int)bob.getPosition().y - height;
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		
		int x2 = x + 2 * width;
		int y2 = y + 2 * height;
		if( x2 > level.getWidth()){
			x2 = level.getWidth() - 1;
		}
		if(y2 > level.getHeight())
			y2 = level.getHeight() - 1;
		Array<Block> blocks = new Array<Block>();
		
		Block block;
		for(int col = x; col <= x2; col++ ){
			for(int row = y; row <= y2; row++){
				block = level.get(col,row);
				if(block != null){
					blocks.add(block);
				}
			}
				
		}
		
		return blocks;
		
	}
	
	public Array<Rectangle> getCollisionRects() {
		return collisionRects;
	}
	
	public Level getLevel(){
		return level;
	}

	public void clear() {
		gameOver = false;
		level = LevelLoader.loadLevel(3);
		bob.setPosition(level.getSpanPosition());
		bob.clear();
	}

	public Array<Load> getDrawableLoads(int cameraWidth, int cameraHeight) {
		int x = (int)bob.getPosition().x - cameraWidth;
		int y = (int)bob.getPosition().y - cameraHeight;
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		
		int x2 = x + 2 * cameraWidth;
		int y2 = y + 2 * cameraHeight;
		if( x2 > level.getWidth()){
			x2 = level.getWidth() - 1;
		}
		if(y2 > level.getHeight())
			y2 = level.getHeight() - 1;
		Array<Load> loads = new Array<Load>();
		
		Load load;
		for(int col = x; col <= x2; col++ ){
			for(int row = y; row <= y2; row++){
				load = level.getLoad(col,row);
				if(load != null){
					loads.add(load);
				}
			}
				
		}
		
		return loads;
	}
}
