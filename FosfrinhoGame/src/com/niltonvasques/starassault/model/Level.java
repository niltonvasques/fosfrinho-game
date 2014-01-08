package com.niltonvasques.starassault.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Level {
	private static final String TAG = "[Level]";
	private static final boolean LOG = false;
	
	private int width;
	private int height;
	private Block[][] blocks;
	private Load[][] loads;
	private Door[][] doors;
	private Key[][] keys;
	private Vector2 spanPosition;
	private Array<Zombie> zombies;
	private Array<Zombie> drawableZombies = new Array<Zombie>();
	private long levelClearTime;

	public Level() {
		loadDemoLevel();
	}

	public long getLevelClearTime() {
		return levelClearTime;
	}

	public void setLevelClearTime(long levelClearTime) {
		this.levelClearTime = levelClearTime;
	}

	public Load[][] getLoads() {
		return loads;
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

	public Block[][] getBlocks() {
		return blocks;
	}

	public void setBlocks(Block[][] blocks) {
		this.blocks = blocks;
	}
	
	public Load getLoad(int x, int y){
		try{
			return loads[x][y];
		}catch (Exception e) {
			return null;
		}
	}

	public Block get(int x, int y) {
		try{
			return blocks[x][y];
		}catch (Exception e) {
			return null;
		}
	}

	public Vector2 getSpanPosition() {
		return spanPosition;
	}

	public void setSpanPosition(Vector2 spanPosition) {
		this.spanPosition = spanPosition;
	}

	public Array<Zombie> getZombies() {
		return zombies;
	}

	public void setZombies(Array<Zombie> zombies) {
		this.zombies = zombies;		
	}
	
	public Array<Zombie> getDrawableZombies(int xCenter, int yCenter, int width, int height){
		if(LOG) Gdx.app.debug(TAG, "Zombies size: "+zombies.size);
		
		drawableZombies.clear();
		float x1,x2,y1,y2;
    	
    	x1 = xCenter- width;
    	x2 = xCenter + width;
    	
    	y1 = yCenter - height;
    	y2 = yCenter + height;
		
		Rectangle screenDrawableArea = new Rectangle(x1, y1, x2, y2);
		
		if(LOG) Gdx.app.debug(TAG, "screenDrawableArea: "+screenDrawableArea);
    	
    	for(Zombie zombie: zombies){
    		if(LOG) Gdx.app.debug(TAG, "zombie.getBounds(): "+zombie.getBounds());
    		if(zombie.getBounds().overlaps(screenDrawableArea)){
    			drawableZombies.add(zombie);
    		}
    	}
		
		return drawableZombies;		
	}

	private void loadDemoLevel() {
		width = 10;
		height = 7;
		blocks = new Block[width][height];
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				blocks[col][row] = null;
			}
		}
	
		for (int col = 0; col < 10; col++) {
			blocks[col][0] = new Block(new Vector2(col, 0));
			blocks[col][6] = new Block(new Vector2(col, 6));
			if (col > 2) {
				blocks[col][1] = new Block(new Vector2(col, 1));
			}
		}
		blocks[9][2] = new Block(new Vector2(9, 2));
		blocks[9][3] = new Block(new Vector2(9, 3));
		blocks[9][4] = new Block(new Vector2(9, 4));
		blocks[9][5] = new Block(new Vector2(9, 5));
	
		blocks[6][3] = new Block(new Vector2(6, 3));
		blocks[6][4] = new Block(new Vector2(6, 4));
		blocks[6][5] = new Block(new Vector2(6, 5));
	}

	public void setLoads(Load[][] loads) {
		this.loads = loads;		
	}

	public void setKeys(Key[][] keys) {
		this.keys = keys;
	}

	public void setDoors(Door[][] doors) {
		this.doors = doors;
	}

	public Door[][] getDoors() {
		return doors;
	}
	
	public Door getDoor(int x, int y) {
		try{
			return doors[x][y];
		}catch (Exception e) {
			return null;
		}
	}

	public Key[][] getKeys() {
		return keys;
	}

	public Key getKey(int x, int y) {
		try{
			return keys[x][y];
		}catch (Exception e) {
			return null;
		}
	}

}
