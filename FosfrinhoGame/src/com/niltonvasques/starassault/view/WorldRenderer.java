package com.niltonvasques.starassault.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.niltonvasques.starassault.model.Shoot;
import com.niltonvasques.starassault.model.World;
import com.niltonvasques.starassault.model.Zombie;

public class WorldRenderer implements Disposable{
	
	private static final String TAG = "[WorldRenderer]";
	private static final boolean LOG = false;

	private static final float CAMERA_WIDTH = 10f;
	private static final float CAMERA_HEIGHT = 7f;
	
	//66 ms 180 steps per minute 3 steps per second == 15 frames per second
	// 1000 ms / 15 frames == 66 ms per frame
	private static final float RUNNING_FRAME_DURATION = 0.06f;  

	private World world;
	private OrthographicCamera cam;
	
	private BobController bobController;

	/** for debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();
	

	/** Textures **/
	private TextureAtlas textureAtlas;
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
	
	private Stage gameOverStage;
	
	
	private Texture shootTexture;

	private SpriteBatch spriteBatch;
	private SpriteBatch uiSpriteBatch;
	
	
	private boolean debug = false;
	private int width;
	private int height;
	private float ppuX;	// pixels per unit on the X axis
	private float ppuY;	// pixels per unit on the Y axis
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;
		ppuX = (float)width / CAMERA_WIDTH;
		ppuY = (float)height / CAMERA_HEIGHT;
	}

	public WorldRenderer(BobController controller, World world, boolean debug) {
		this.bobController = controller;
		this.world = world;
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(world.getBob().getPosition().x, world.getBob().getPosition().y, 0);
		this.cam.update();
		this.debug = debug;
		
		OrthographicCamera uiCamera = new OrthographicCamera(CAMERA_WIDTH,CAMERA_HEIGHT);
		uiCamera.position.set(CAMERA_WIDTH/2, CAMERA_HEIGHT/2, 0);
		uiCamera.update();
		
		spriteBatch = new SpriteBatch();
		uiSpriteBatch = new SpriteBatch();
		uiSpriteBatch.setProjectionMatrix(uiCamera.combined);
		
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.drawPixel(0, 0);
		
		shootTexture = new Texture(pixmap);
		
		loadTextures();
	}

	private void loadTextures() {
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
		
		{//SET GAME OVER STAGE
			gameOverRegion = textureAtlas.findRegion("game-over");
			restartRegion = textureAtlas.findRegion("restart");
			
			Image gameOverImage = new Image(gameOverRegion);
			gameOverImage.setSize(CAMERA_WIDTH, 2f);
			gameOverImage.setPosition(0, CAMERA_HEIGHT-gameOverImage.getHeight());
			
			Image restartSprite = new Image(restartRegion);
			restartSprite.setSize(CAMERA_WIDTH, 2f);
			restartSprite.setPosition(0, CAMERA_HEIGHT/2-2.5f);
			
			gameOverStage = new Stage(CAMERA_WIDTH, CAMERA_HEIGHT);
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
			drawZombies();
			drawBob();
			drawShoots();
		spriteBatch.end();

		uiSpriteBatch.begin();
			drawHearts();
		uiSpriteBatch.end();
			
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
		for (Block block : world.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
			spriteBatch.draw(blockRegion, block.getPosition().x , block.getPosition().y , Block.SIZE , Block.SIZE );
		}
	}
	
	private void drawZombies(){
		Array<Zombie> drawableZombies = world.getLevel().getDrawableZombies((int)world.getBob().getPosition().x, (int)world.getBob().getPosition().y,(int)CAMERA_WIDTH, (int)CAMERA_HEIGHT);
		if(LOG) Gdx.app.log(TAG, "DrawableZombies"+drawableZombies.size);
		
		for (Zombie zombie : drawableZombies) {
			if(zombie instanceof CataZombie)
				spriteBatch.draw(catazombieRegion, zombie.getPosition().x , zombie.getPosition().y , zombie.getBounds().width , zombie.getBounds().height );
			else			
				spriteBatch.draw(zombieRegion, zombie.getPosition().x , zombie.getPosition().y , zombie.getBounds().width , zombie.getBounds().height );
		}
	}
	
	private void drawShoots(){
		for(Shoot shoot : bobController.getDrawableShoots(CAMERA_WIDTH, CAMERA_HEIGHT)){
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
			uiSpriteBatch.draw(heartRegion, i, CAMERA_HEIGHT-0.5f,0.5f, 0.5f);
		}
	}

	private void drawDebug() {
		// render blocks
		// render blocks
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Line);
        for (Block block : world.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
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
		textureAtlas.dispose();
		shootTexture.dispose();
	}
}
