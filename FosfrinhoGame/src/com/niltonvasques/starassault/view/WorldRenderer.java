package com.niltonvasques.starassault.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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

public class WorldRenderer implements Disposable{
	
	private static final String TAG = "[WorldRenderer]";
	private static final boolean LOG = false;

	//66 ms 180 steps per minute 3 steps per second == 15 frames per second
	// 1000 ms / 15 frames == 66 ms per frame
	private static final float RUNNING_FRAME_DURATION = 0.06f;  
	private static final float LOAD_FRAME_DURATION = 0.2f;

	private World world;
	private OrthographicCamera cam;
	
	private BobController bobController;

	/** for debug rendering **/
	private ShapeRenderer debugRenderer = new ShapeRenderer();
	
	/** Disposable Resources */
	private TextureAtlas textureAtlas;
	private BitmapFont font;

	/** Textures **/
	private AtlasRegion bobEmptyRegion;
	private AtlasRegion bobIdleLeftRegion;
	private AtlasRegion bobIdleRightRegion;
	private AtlasRegion bobJumpLeftRegion;
	private AtlasRegion bobJumpRightRegion;
	private AtlasRegion bobFallLeftRegion;
	private AtlasRegion bobFallRightRegion;
	private AtlasRegion blockRegion;
	private AtlasRegion gameOverRegion;
	private AtlasRegion restartRegion;
	private AtlasRegion heartRegion;
	private AtlasRegion keyRegion;
	private AtlasRegion doorRegion;
	private AtlasRegion gateRegion;
	
	private AtlasRegion zombieRegion;
	private AtlasRegion catazombieRegion;
	private TextureRegion bobFrameRegion;
	
	private Animation walkingLeftAnimation;
	private Animation walkingRightAnimation;
	private Animation bobIdleDamagedLeftAnimation;
	private Animation bobIdleDamagedRightAnimation;
	private Animation bobWalkingDamagedLeftAnimation;
	private Animation bobWalkingDamagedRightAnimation;
	private Animation bobJumpingDamagedLeftAnimation;
	private Animation bobJumpingDamagedRightAnimation;
	private Animation bobFallDamagedLeftAnimation;
	private Animation bobFallDamagedRightAnimation;
	private Animation loadAnimation;
	
	private Stage gameOverStage;
	
	private Texture shootTexture;

	private SpriteBatch spriteBatch;
	private SpriteBatch uiSpriteBatch;
	private SpriteBatch fontBatch;
	
	
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
		
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.drawPixel(0, 0);
		
		shootTexture = new Texture(pixmap);
		
		loadTextures();
	}

	private void loadTextures() {
		
		//Load font
		font = new BitmapFont();
		font.setScale(2f);
		font.setColor(Color.WHITE);
		
		String bobPrefix = "bob-gun";
		textureAtlas = new TextureAtlas("data/textures.pack");
		bobEmptyRegion = textureAtlas.findRegion("bob-empty");
		bobIdleLeftRegion = textureAtlas.findRegion(bobPrefix+"-01");
		bobIdleRightRegion = new AtlasRegion(bobIdleLeftRegion);
		bobIdleRightRegion.flip(true, false);
		
		bobJumpLeftRegion = textureAtlas.findRegion(bobPrefix+"-up");
		bobJumpRightRegion = new AtlasRegion(bobJumpLeftRegion);
		bobJumpRightRegion.flip(true, false);
		
		bobFallLeftRegion = textureAtlas.findRegion(bobPrefix+"-down");
		bobFallRightRegion = new AtlasRegion(bobFallLeftRegion);
		bobFallRightRegion.flip(true, false);
		
		zombieRegion = textureAtlas.findRegion("zumbi-01");
		catazombieRegion = textureAtlas.findRegion("catazumbi-01");
		
		blockRegion = textureAtlas.findRegion("block");
		AtlasRegion[] walkingLeftFrames = new AtlasRegion[5];
		AtlasRegion[] walkingRightFrames = new AtlasRegion[5];
		for(int i = 0; i < 5; i++){
			walkingLeftFrames[i] = textureAtlas.findRegion(bobPrefix+"-0" + (i+2));
			walkingRightFrames[i] = new AtlasRegion(walkingLeftFrames[i]);
			walkingRightFrames[i].flip(true, false);
		}
		
		walkingLeftAnimation = new Animation(RUNNING_FRAME_DURATION, walkingLeftFrames);
		walkingRightAnimation = new Animation(RUNNING_FRAME_DURATION, walkingRightFrames);
		
		AtlasRegion[] walkingDamagedLeftFrames = new AtlasRegion[10];
		AtlasRegion[] walkingDamagedRightFrames = new AtlasRegion[10];
		for(int i = 0; i < 5; i++){
			walkingDamagedLeftFrames[(i*2)] = textureAtlas.findRegion(bobPrefix+"-0" + (i+2));
			walkingDamagedLeftFrames[(i*2)+1] = bobEmptyRegion;
			
			walkingDamagedRightFrames[(i*2)] = new AtlasRegion(walkingDamagedLeftFrames[(i*2)]);
			walkingDamagedRightFrames[(i*2)].flip(true, false);
			walkingDamagedRightFrames[(i*2)+1] = bobEmptyRegion;
		}
		
		bobWalkingDamagedLeftAnimation = new Animation(RUNNING_FRAME_DURATION/2, walkingDamagedLeftFrames);
		bobWalkingDamagedRightAnimation = new Animation(RUNNING_FRAME_DURATION/2, walkingDamagedRightFrames);
		
		AtlasRegion[] bobIdleDamagedLeftFrames = new AtlasRegion[2];
		bobIdleDamagedLeftFrames[0] = bobIdleLeftRegion;
		bobIdleDamagedLeftFrames[1] = bobEmptyRegion;
		bobIdleDamagedLeftAnimation = new Animation(RUNNING_FRAME_DURATION, bobIdleDamagedLeftFrames);
		
		AtlasRegion[] bobIdleDamagedRightFrames = new AtlasRegion[2];
		bobIdleDamagedRightFrames[0] = bobIdleRightRegion;
		bobIdleDamagedRightFrames[1] = bobEmptyRegion;
		bobIdleDamagedRightAnimation = new Animation(RUNNING_FRAME_DURATION, bobIdleDamagedRightFrames);
		
		AtlasRegion[] bobJumpingDamageLeftFrames = new AtlasRegion[2];
		bobJumpingDamageLeftFrames[0] = bobJumpLeftRegion;
		bobJumpingDamageLeftFrames[1] = bobEmptyRegion;
		bobJumpingDamagedLeftAnimation = new Animation(RUNNING_FRAME_DURATION, bobJumpingDamageLeftFrames);
		
		AtlasRegion[] bobJumpingDamagedRightFrames = new AtlasRegion[2];
		bobJumpingDamagedRightFrames[0] = bobJumpRightRegion;
		bobJumpingDamagedRightFrames[1] = bobEmptyRegion;
		bobJumpingDamagedRightAnimation = new Animation(RUNNING_FRAME_DURATION, bobJumpingDamagedRightFrames);
		
		AtlasRegion[] bobFallDamageLeftFrames = new AtlasRegion[2];
		bobFallDamageLeftFrames[0] = bobFallLeftRegion;
		bobFallDamageLeftFrames[1] = bobEmptyRegion;
		bobFallDamagedLeftAnimation = new Animation(RUNNING_FRAME_DURATION, bobFallDamageLeftFrames);
		
		AtlasRegion[] bobFallDamagedRightFrames = new AtlasRegion[2];
		bobFallDamagedRightFrames[0] = bobFallRightRegion;
		bobFallDamagedRightFrames[1] = bobEmptyRegion;
		bobFallDamagedRightAnimation = new Animation(RUNNING_FRAME_DURATION, bobFallDamagedRightFrames);
		
		heartRegion = textureAtlas.findRegion("heart");
		
		{//Loading items
			AtlasRegion[] loadFrames = new AtlasRegion[3];
			for(int i = 0; i < 3; i++){
				loadFrames[i] = textureAtlas.findRegion("load-0"+(i+1));
			}
			loadAnimation = new Animation(LOAD_FRAME_DURATION, loadFrames);
			
			keyRegion = textureAtlas.findRegion("key");
			doorRegion = textureAtlas.findRegion("door");
			gateRegion = textureAtlas.findRegion("gate");
		}
		
		
		{//SET GAME OVER STAGE
			gameOverRegion = textureAtlas.findRegion("game-over");
			restartRegion = textureAtlas.findRegion("restart");
			
			Image gameOverImage = new Image(gameOverRegion);
			gameOverImage.setSize(bobController.getCameraHelper().getViewportWidth(), 2f);
			gameOverImage.setPosition(0, bobController.getCameraHelper().getViewportHeight()-gameOverImage.getHeight());
			
			Image restartSprite = new Image(restartRegion);
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
			font.draw(fontBatch, "bullets: "+world.getBob().getGun().getLoad().getMunition(), (bobController.getCameraHelper().getViewportWidth()-2)*ppuX, 0.5f*ppuY);
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
			spriteBatch.draw(blockRegion, block.getPosition().x , block.getPosition().y , Block.SIZE , Block.SIZE );
		}
	}
	
	private void drawLoads(){
		for(Load load: world.getDrawableLoads((int)bobController.getCameraHelper().getViewportWidth(), (int)bobController.getCameraHelper().getViewportHeight())){
			TextureRegion frame = loadAnimation.getKeyFrame(load.getStateTime(), true);
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
		
		Key load;
		for(int col = x; col <= x2; col++ ){
			for(int row = y; row <= y2; row++){
				load = world.getLevel().getKey(col,row);
				if(load != null){
					spriteBatch.draw(keyRegion, load.getBounds().x , load.getBounds().y , load.getBounds().width , load.getBounds().height);
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
						spriteBatch.draw(gateRegion, door.getBounds().x , door.getBounds().y , door.getBounds().width , door.getBounds().height);
					}else{
						spriteBatch.draw(doorRegion, door.getBounds().x , door.getBounds().y , door.getBounds().width , door.getBounds().height);
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
				spriteBatch.draw(catazombieRegion, zombie.getPosition().x , zombie.getPosition().y , zombie.getBounds().width , zombie.getBounds().height );
			else			
				spriteBatch.draw(zombieRegion, zombie.getPosition().x , zombie.getPosition().y , zombie.getBounds().width , zombie.getBounds().height );
		}
	}
	
	private void drawShoots(){
		for(Shoot shoot : bobController.getDrawableShoots(bobController.getCameraHelper().getViewportWidth(), bobController.getCameraHelper().getViewportHeight())){
			spriteBatch.draw(shootTexture, shoot.getPosition().x, shoot.getPosition().y, Shoot.SIZE, Shoot.SIZE);
		}
	}

	private void drawBob() {
		Bob bob = world.getBob();
		if(bob.getState() == State.WALKING){
			if(bob.isDamaged()){
				bobFrameRegion = (bob.isFacingLeft() ? bobWalkingDamagedLeftAnimation.getKeyFrame(bob.getStateTime(), true) 
						: bobWalkingDamagedRightAnimation.getKeyFrame(bob.getStateTime(), true) );
			}else{
				bobFrameRegion = (bob.isFacingLeft() ? walkingLeftAnimation.getKeyFrame(bob.getStateTime(), true) 
						: walkingRightAnimation.getKeyFrame(bob.getStateTime(), true) );
			}
		}else if(bob.getState() == State.JUMPING){
			if(bob.getVelocity().y > 0){
				if(bob.isDamaged()){
					bobFrameRegion = (bob.isFacingLeft() ? bobJumpingDamagedLeftAnimation.getKeyFrame(bob.getStateTime(),true) 
							: bobJumpingDamagedRightAnimation.getKeyFrame(bob.getStateTime(),true));
				}else{
					bobFrameRegion = (bob.isFacingLeft() ? bobJumpLeftRegion : bobJumpRightRegion);
				}
			}else{
				if(bob.isDamaged()){
					bobFrameRegion = (bob.isFacingLeft() ? bobFallDamagedLeftAnimation.getKeyFrame(bob.getStateTime(),true) 
							: bobFallDamagedRightAnimation.getKeyFrame(bob.getStateTime(),true));
				}else{
					bobFrameRegion = (bob.isFacingLeft() ? bobFallLeftRegion : bobFallRightRegion);			
				}
			}
		}else{
			if(bob.isDamaged()){
				bobFrameRegion = (bob.isFacingLeft() ? bobIdleDamagedLeftAnimation.getKeyFrame(bob.getStateTime(),true) 
						: bobIdleDamagedRightAnimation.getKeyFrame(bob.getStateTime(),true));
			}else{
				bobFrameRegion = (bob.isFacingLeft() ? bobIdleLeftRegion : bobIdleRightRegion);
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
			uiSpriteBatch.draw(heartRegion, i, bobController.getCameraHelper().getViewportHeight()-0.5f,0.5f, 0.5f);
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
		textureAtlas.dispose();
		shootTexture.dispose();
		font.dispose();
	}
}
