package com.niltonvasques.starassault.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.niltonvasques.starassault.controller.BobController;
import com.niltonvasques.starassault.model.Block;
import com.niltonvasques.starassault.model.Bob;
import com.niltonvasques.starassault.model.Bob.State;
import com.niltonvasques.starassault.model.CataZombie;
import com.niltonvasques.starassault.model.Door;
import com.niltonvasques.starassault.model.Gate;
import com.niltonvasques.starassault.model.Key;
import com.niltonvasques.starassault.model.Load;
import com.niltonvasques.starassault.model.Shoot;
import com.niltonvasques.starassault.model.World;
import com.niltonvasques.starassault.model.Zombie;
import com.niltonvasques.starassault.service.Assets;

public class WorldRenderer implements Disposable{
	
	private static final String TAG = "[WorldRenderer]";
	private static final boolean LOG = false;

	//66 ms 180 steps per minute 3 steps per second == 15 frames per second
	// 1000 ms / 15 frames == 66 ms per frame

	private World world;
	private OrthographicCamera cam;
	
	private BobController bobController;

	/** for debug rendering **/
	private ShapeRenderer debugRenderer = new ShapeRenderer();
	
	private Stage gameOverStage;
	
	private SpriteBatch spriteBatch;
	private SpriteBatch uiSpriteBatch;
	private SpriteBatch fontBatch;
	
	private Assets assets;
	
	
	private boolean debug = false;
	private int width;
	private int height;
	private float ppuX;	// pixels per unit on the X axis
	private float ppuY;	// pixels per unit on the Y axis
	
	public void setSize (int w, int h) {
		if(LOG) Gdx.app.log(TAG, "setSize");
		this.width = w;
		this.height = h;
		ppuX = (float)width / bobController.getCameraHelper().getViewportWidth();
		ppuY = (float)height / bobController.getCameraHelper().getViewportHeight();
	}

	public WorldRenderer(BobController controller, boolean debug) {
		this.assets = Assets.instance;
		this.bobController = controller;
		this.world = bobController.getWorld();
		this.cam = new OrthographicCamera(bobController.getCameraHelper().getViewportWidth(), bobController.getCameraHelper().getViewportHeight());
		this.cam.position.set(world.getBob().getPosition().x, world.getBob().getPosition().y, 0);
		this.cam.update();
		this.debug = debug;
		
		OrthographicCamera uiCamera = new OrthographicCamera(bobController.getCameraHelper().getViewportWidth(),bobController.getCameraHelper().getViewportHeight());
		uiCamera.position.set(bobController.getCameraHelper().getViewportWidth()/2, bobController.getCameraHelper().getViewportHeight()/2, 0);
		uiCamera.update();
		
		spriteBatch = new SpriteBatch();
		uiSpriteBatch = new SpriteBatch();
		uiSpriteBatch.setProjectionMatrix(uiCamera.combined);
		fontBatch = new SpriteBatch();
		
		configureGameOverMenu();
	}

	private void configureGameOverMenu() {
		Image gameOverImage = new Image(assets.gameOverMenu.gameOverRegion);
		gameOverImage.setSize(bobController.getCameraHelper().getViewportWidth(), 2f);
		gameOverImage.setPosition(0, bobController.getCameraHelper().getViewportHeight()-gameOverImage.getHeight());
		
		Image restartSprite = new Image(assets.gameOverMenu.restartRegion);
		restartSprite.setSize(bobController.getCameraHelper().getViewportWidth(), 2f);
		restartSprite.setPosition(0, bobController.getCameraHelper().getViewportHeight()/2-2.5f);
		
		gameOverStage = new Stage(bobController.getCameraHelper().getViewportWidth(), bobController.getCameraHelper().getViewportHeight());
		gameOverStage.addActor(restartSprite);
		gameOverStage.addActor(gameOverImage);
		
		bobController.registerInputProcessor(gameOverStage);
		
		restartSprite.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(world.isGameOver()){
					bobController.restartGame();
				}
			}
		});
	}

	public void render() {
		cam.position.set(world.getBob().getPosition().x, world.getBob().getPosition().y, 0);
		cam.update();
		
		spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();
			drawBlocks();
			drawDoor();
			drawLoads();
			drawKeys();
			drawZombies();
			drawBob();
			drawShoots();
		spriteBatch.end();

		uiSpriteBatch.begin();
			drawHearts();
		uiSpriteBatch.end();
		
		fontBatch.begin();
			assets.fonts.fontNormal.draw(fontBatch, "bullets: "+world.getBob().getGun().getLoad().getMunition(), (bobController.getCameraHelper().getViewportWidth()-2)*ppuX, 0.5f*ppuY);
		fontBatch.end();
			
		if(world.isGameOver()){
			drawGameOver();
		}			
		
		if (debug){
			drawCollisionBlocks();
			drawDebug();
		}
	}

	private void drawGameOver() {
		gameOverStage.draw();
	}
	
	public void setDebug(boolean d){
		debug = d;
	}
	
	public boolean isDebug(){
		return debug;
	}
	

	private void drawBlocks() {
		for (Block block : world.getDrawableBlocks((int)bobController.getCameraHelper().getViewportWidth(), (int)bobController.getCameraHelper().getViewportHeight())) {
			spriteBatch.draw(assets.level.blockRegion, block.getPosition().x , block.getPosition().y , Block.SIZE , Block.SIZE );
		}
	}
	
	private void drawLoads(){
		for(Load load: world.getDrawableLoads((int)bobController.getCameraHelper().getViewportWidth(), (int)bobController.getCameraHelper().getViewportHeight())){
			TextureRegion frame = assets.items.loadAnimation.getKeyFrame(load.getStateTime(), true);
			if(LOG) Gdx.app.log(TAG, "loadFrame"+frame);
			spriteBatch.draw(frame, load.getBounds().x , load.getBounds().y , load.getBounds().width , load.getBounds().height);
		}
	}
	
	private void drawKeys(){
		int x = (int)world.getBob().getPosition().x -(int) bobController.getCameraHelper().getViewportWidth();
		int y = (int)world.getBob().getPosition().y - (int)bobController.getCameraHelper().getViewportHeight();
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		
		int x2 = x + 2 * ((int)bobController.getCameraHelper().getViewportWidth());
		int y2 = y + 2 * ((int)bobController.getCameraHelper().getViewportHeight());
		if( x2 > world.getLevel().getWidth()){
			x2 = world.getLevel().getWidth() - 1;
		}
		if(y2 > world.getLevel().getHeight())
			y2 = world.getLevel().getHeight() - 1;
		
		Key key;
		for(int col = x; col <= x2; col++ ){
			for(int row = y; row <= y2; row++){
				key = world.getLevel().getKey(col,row);
				if(key != null){
					spriteBatch.draw(assets.items.keyRegion, key.getBounds().x , key.getBounds().y , key.getBounds().width , key.getBounds().height);
				}
			}
				
		}
	}
	
	private void drawDoor(){
		int x = (int)world.getBob().getPosition().x -(int) bobController.getCameraHelper().getViewportWidth();
		int y = (int)world.getBob().getPosition().y - (int)bobController.getCameraHelper().getViewportHeight();
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		
		int x2 = x + 2 * ((int)bobController.getCameraHelper().getViewportWidth());
		int y2 = y + 2 * ((int)bobController.getCameraHelper().getViewportHeight());
		if( x2 > world.getLevel().getWidth()){
			x2 = world.getLevel().getWidth() - 1;
		}
		if(y2 > world.getLevel().getHeight())
			y2 = world.getLevel().getHeight() - 1;
		
		Door door;
		for(int col = x; col <= x2; col++ ){
			for(int row = y; row <= y2; row++){
				door = world.getLevel().getDoor(col,row);
				if(door != null){
					if(door instanceof Gate){
						spriteBatch.draw(assets.level.gateRegion, door.getBounds().x , door.getBounds().y , door.getBounds().width , door.getBounds().height);
					}else{
						spriteBatch.draw(assets.level.doorRegion, door.getBounds().x , door.getBounds().y , door.getBounds().width , door.getBounds().height);
					}
				}
			}
				
		}
	}
	
	private void drawZombies(){
		Array<Zombie> drawableZombies = world.getLevel().getDrawableZombies((int)world.getBob().getPosition().x, (int)world.getBob().getPosition().y,(int)bobController.getCameraHelper().getViewportWidth(), (int)bobController.getCameraHelper().getViewportHeight());
		if(LOG) Gdx.app.log(TAG, "DrawableZombies"+drawableZombies.size);
		
		for (Zombie zombie : drawableZombies) {
			if(zombie instanceof CataZombie)
				spriteBatch.draw(assets.enemys.catazombieRegion, zombie.getPosition().x , zombie.getPosition().y , zombie.getBounds().width , zombie.getBounds().height );
			else{
				TextureRegion frame = null;
				
				switch (zombie.getState()) {
				case WALKING:
					Animation zombieAnimation = zombie.isFacingLeft() ? assets.enemys.zombieWalkingLeftAnimation : assets.enemys.zombieWalkingRightAnimation;
					frame = zombieAnimation.getKeyFrame(zombie.getStateTime(), true);
					break;
					
				case DAMAGED:
					frame = zombie.isFacingLeft() ? assets.enemys.zombieDamagedLeft : assets.enemys.zombieDamagedRight;
					break;
					
				case DYING:
					frame = zombie.isFacingLeft() ? assets.enemys.zombieDyingLeftAnimation.getKeyFrame(zombie.getStateTime()) : assets.enemys.zombieDyingRightAnimation.getKeyFrame(zombie.getStateTime());
					break;
					
				default:
					frame = zombie.isFacingLeft() ? assets.enemys.zombieDyingLeftAnimation.getKeyFrame(zombie.getStateTime()) : assets.enemys.zombieDyingRightAnimation.getKeyFrame(zombie.getStateTime());
					break;
				}
				
				spriteBatch.draw(frame, zombie.getPosition().x , zombie.getPosition().y , zombie.getBounds().width , zombie.getBounds().height );
			}
		}
	}
	
	private void drawShoots(){
		for(Shoot shoot : bobController.getDrawableShoots(bobController.getCameraHelper().getViewportWidth(), bobController.getCameraHelper().getViewportHeight())){
			spriteBatch.draw(assets.shoots.shootTexture, shoot.getPosition().x, shoot.getPosition().y, Shoot.SIZE, Shoot.SIZE);
		}
	}

	private void drawBob() {
		TextureRegion bobFrameRegion;
		Bob bob = world.getBob();
		if(bob.getState() == State.WALKING){
			if(bob.isDamaged()){
				bobFrameRegion = (bob.isFacingLeft() ? assets.bob.bobWalkingDamagedLeftAnimation.getKeyFrame(bob.getStateTime(), true) 
						: assets.bob.bobWalkingDamagedRightAnimation.getKeyFrame(bob.getStateTime(), true) );
			}else{
				bobFrameRegion = (bob.isFacingLeft() ? assets.bob.walkingLeftAnimation.getKeyFrame(bob.getStateTime(), true) 
						: assets.bob.walkingRightAnimation.getKeyFrame(bob.getStateTime(), true) );
			}
		}else if(bob.getState() == State.JUMPING){
			if(bob.getVelocity().y > 0){
				if(bob.isDamaged()){
					bobFrameRegion = (bob.isFacingLeft() ? assets.bob.bobJumpingDamagedLeftAnimation.getKeyFrame(bob.getStateTime(),true) 
							: assets.bob.bobJumpingDamagedRightAnimation.getKeyFrame(bob.getStateTime(),true));
				}else{
					bobFrameRegion = (bob.isFacingLeft() ? assets.bob.bobJumpLeftRegion : assets.bob.bobJumpRightRegion);
				}
			}else{
				if(bob.isDamaged()){
					bobFrameRegion = (bob.isFacingLeft() ? assets.bob.bobFallDamagedLeftAnimation.getKeyFrame(bob.getStateTime(),true) 
							: assets.bob.bobFallDamagedRightAnimation.getKeyFrame(bob.getStateTime(),true));
				}else{
					bobFrameRegion = (bob.isFacingLeft() ? assets.bob.bobFallLeftRegion : assets.bob.bobFallRightRegion);			
				}
			}
		}else{
			if(bob.isDamaged()){
				bobFrameRegion = (bob.isFacingLeft() ? assets.bob.bobIdleDamagedLeftAnimation.getKeyFrame(bob.getStateTime(),true) 
						: assets.bob.bobIdleDamagedRightAnimation.getKeyFrame(bob.getStateTime(),true));
			}else{
				bobFrameRegion = (bob.isFacingLeft() ? assets.bob.bobIdleLeftRegion : assets.bob.bobIdleRightRegion);
			}
		}
		spriteBatch.draw(bobFrameRegion, bob.getPosition().x, bob.getPosition().y, Bob.SIZE , Bob.SIZE);
	}
	
	private void drawCollisionBlocks() {
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Filled);
		for (Rectangle rect : world.getCollisionRects()) {
			debugRenderer.setColor(new Color(1, 0, 0, 1));
			debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		}
		debugRenderer.end();
	}
	
	private void drawHearts(){
		for(int i = 0; i < world.getBob().getHp(); i++){
			uiSpriteBatch.draw(assets.gameInfo.heart, i, bobController.getCameraHelper().getViewportHeight()-0.5f,0.5f, 0.5f);
		}
	}

	private void drawDebug() {
		// render blocks
		// render blocks
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Line);
        for (Block block : world.getDrawableBlocks((int)bobController.getCameraHelper().getViewportWidth(), (int)bobController.getCameraHelper().getViewportHeight())) {
                Rectangle rect = block.getBounds();
                debugRenderer.setColor(new Color(1, 0, 0, 1));
                debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        // render Bob
        Bob bob = world.getBob();
        Rectangle rect = bob.getBounds();
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        debugRenderer.end();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		uiSpriteBatch.dispose();
	}
}
