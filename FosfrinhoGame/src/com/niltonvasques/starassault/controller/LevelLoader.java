package com.niltonvasques.starassault.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.starassault.model.Block;
import com.niltonvasques.starassault.model.CataZombie;
import com.niltonvasques.starassault.model.Level;
import com.niltonvasques.starassault.model.NormalZombie;
import com.niltonvasques.starassault.model.Zombie;

/**
* Created with IntelliJ IDEA.
* User: tamas
* Date: 26/03/2013
* Time: 15:30
* To change this template use File | Settings | File Templates.
*/
public class LevelLoader {
	private static final String TAG = "[LevelLoader]";
	private static final boolean LOG = false;
    private static final String LEVEL_PREFIX = "levels/level-";

    private static final int BLOCK = 0x000000; // black
    private static final int EMPTY = 0xffffff; // white
    private static final int START_POS = 0x0000ff; // blue
    private static final int ZOMBIE = 0x800080; // purple
    private static final int CATAZOMBIE = 0x00ff00; // green

    public static Level loadLevel(int number) {
    	
    	Array<Zombie> zombies = new Array<Zombie>();
    	
        Level level = new Level();

        // Loading the png into a Pixmap
        Pixmap pixmap = new Pixmap(Gdx.files.internal(LEVEL_PREFIX + number + ".png"));
        
        if(LOG) Gdx.app.log(TAG, "loadLevel: "+LEVEL_PREFIX + number + ".png" );

        // setting the size of the level based on the size of the pixmap
        level.setWidth(pixmap.getWidth());
        level.setHeight(pixmap.getHeight());

        // creating the backing blocks array
        Block[][] blocks = new Block[level.getWidth()][level.getHeight()];
        for (int col = 0; col < level.getWidth(); col++) {
            for (int row = 0; row < level.getHeight(); row++) {
                blocks[col][row] = null;
            }
        }
        
        

        for (int row = 0; row < level.getHeight(); row++) {
            for (int col = 0; col < level.getWidth(); col++) {
                int pixel = (pixmap.getPixel(col, row) >>> 8) & 0xffffff;
                int iRow = level.getHeight() - 1 - row;
                
                switch(pixel){
                	case BLOCK:
                        blocks[col][iRow] = new Block(new Vector2(col, iRow));
                		break;
                	
                	case START_POS:
                		if(LOG) Gdx.app.log(TAG, "pixel["+row+"]"+"["+col+"]: "+Integer.toHexString(pixel));
                        level.setSpanPosition(new Vector2(col, iRow));
                		break;
                		
                	case ZOMBIE:
                		if(LOG) Gdx.app.log(TAG, "FIND_ZUMBI");
                		Zombie zombie = new NormalZombie(new Vector2(col, iRow));
                		zombies.add(zombie);
                		break;
                		
                	case CATAZOMBIE:
                		CataZombie cataZombie = new CataZombie(new Vector2(col, iRow));
                		zombies.add(cataZombie);
                		break;
                		
                	case EMPTY:
                		break;
                		
            		default:
            			if(LOG) Gdx.app.log(TAG, "pixel["+row+"]"+"["+col+"]: "+Integer.toHexString(pixel));
            			break;
                }
                
            }
        }

        // setting the blocks
        level.setBlocks(blocks);
        level.setZombies(zombies);
        return level;
    }

}