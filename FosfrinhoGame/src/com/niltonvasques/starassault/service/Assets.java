package com.niltonvasques.starassault.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.niltonvasques.starassault.util.Constants;

public class Assets implements Disposable, AssetErrorListener{
	
//	private void loadTextures() {
//		
//		//Load font
//		font = new BitmapFont();
//		font.setScale(2f);
//		font.setColor(Color.WHITE);
//		
//		AtlasRegion[] walkingLeftFrames = new AtlasRegion[5];
//		AtlasRegion[] walkingRightFrames = new AtlasRegion[5];
//		for(int i = 0; i < 5; i++){
//			walkingLeftFrames[i] = textureAtlas.findRegion(bobPrefix+"-0" + (i+2));
//			walkingRightFrames[i] = new AtlasRegion(walkingLeftFrames[i]);
//			walkingRightFrames[i].flip(true, false);
//		}
//		
//		walkingLeftAnimation = new Animation(RUNNING_FRAME_DURATION, walkingLeftFrames);
//		walkingRightAnimation = new Animation(RUNNING_FRAME_DURATION, walkingRightFrames);
//		
//		AtlasRegion[] walkingDamagedLeftFrames = new AtlasRegion[10];
//		AtlasRegion[] walkingDamagedRightFrames = new AtlasRegion[10];
//		for(int i = 0; i < 5; i++){
//			walkingDamagedLeftFrames[(i*2)] = textureAtlas.findRegion(bobPrefix+"-0" + (i+2));
//			walkingDamagedLeftFrames[(i*2)+1] = bobEmptyRegion;
//			
//			walkingDamagedRightFrames[(i*2)] = new AtlasRegion(walkingDamagedLeftFrames[(i*2)]);
//			walkingDamagedRightFrames[(i*2)].flip(true, false);
//			walkingDamagedRightFrames[(i*2)+1] = bobEmptyRegion;
//		}
//		
//		bobWalkingDamagedLeftAnimation = new Animation(RUNNING_FRAME_DURATION/2, walkingDamagedLeftFrames);
//		bobWalkingDamagedRightAnimation = new Animation(RUNNING_FRAME_DURATION/2, walkingDamagedRightFrames);
//		
//		AtlasRegion[] bobIdleDamagedLeftFrames = new AtlasRegion[2];
//		bobIdleDamagedLeftFrames[0] = bobIdleLeftRegion;
//		bobIdleDamagedLeftFrames[1] = bobEmptyRegion;
//		bobIdleDamagedLeftAnimation = new Animation(RUNNING_FRAME_DURATION, bobIdleDamagedLeftFrames);
//		
//		AtlasRegion[] bobIdleDamagedRightFrames = new AtlasRegion[2];
//		bobIdleDamagedRightFrames[0] = bobIdleRightRegion;
//		bobIdleDamagedRightFrames[1] = bobEmptyRegion;
//		bobIdleDamagedRightAnimation = new Animation(RUNNING_FRAME_DURATION, bobIdleDamagedRightFrames);
//		
//		AtlasRegion[] bobJumpingDamageLeftFrames = new AtlasRegion[2];
//		bobJumpingDamageLeftFrames[0] = bobJumpLeftRegion;
//		bobJumpingDamageLeftFrames[1] = bobEmptyRegion;
//		bobJumpingDamagedLeftAnimation = new Animation(RUNNING_FRAME_DURATION, bobJumpingDamageLeftFrames);
//		
//		AtlasRegion[] bobJumpingDamagedRightFrames = new AtlasRegion[2];
//		bobJumpingDamagedRightFrames[0] = bobJumpRightRegion;
//		bobJumpingDamagedRightFrames[1] = bobEmptyRegion;
//		bobJumpingDamagedRightAnimation = new Animation(RUNNING_FRAME_DURATION, bobJumpingDamagedRightFrames);
//		
//		AtlasRegion[] bobFallDamageLeftFrames = new AtlasRegion[2];
//		bobFallDamageLeftFrames[0] = bobFallLeftRegion;
//		bobFallDamageLeftFrames[1] = bobEmptyRegion;
//		bobFallDamagedLeftAnimation = new Animation(RUNNING_FRAME_DURATION, bobFallDamageLeftFrames);
//		
//		AtlasRegion[] bobFallDamagedRightFrames = new AtlasRegion[2];
//		bobFallDamagedRightFrames[0] = bobFallRightRegion;
//		bobFallDamagedRightFrames[1] = bobEmptyRegion;
//		bobFallDamagedRightAnimation = new Animation(RUNNING_FRAME_DURATION, bobFallDamagedRightFrames);
//		
//		heartRegion = textureAtlas.findRegion("heart");
//		
//		{//Loading items
//			AtlasRegion[] loadFrames = new AtlasRegion[3];
//			for(int i = 0; i < 3; i++){
//				loadFrames[i] = textureAtlas.findRegion("load-0"+(i+1));
//			}
//			loadAnimation = new Animation(LOAD_FRAME_DURATION, loadFrames);
//			
//			keyRegion = textureAtlas.findRegion("key");
//			doorRegion = textureAtlas.findRegion("door");
//			gateRegion = textureAtlas.findRegion("gate");
//		}
//		
//		
//		{//SET GAME OVER STAGE
//			gameOverRegion = textureAtlas.findRegion("game-over");
//			restartRegion = textureAtlas.findRegion("restart");
//			
//			Image gameOverImage = new Image(gameOverRegion);
//			gameOverImage.setSize(bobController.getCameraHelper().getViewportWidth(), 2f);
//			gameOverImage.setPosition(0, bobController.getCameraHelper().getViewportHeight()-gameOverImage.getHeight());
//			
//			Image restartSprite = new Image(restartRegion);
//			restartSprite.setSize(bobController.getCameraHelper().getViewportWidth(), 2f);
//			restartSprite.setPosition(0, bobController.getCameraHelper().getViewportHeight()/2-2.5f);
//			
//			gameOverStage = new Stage(bobController.getCameraHelper().getViewportWidth(), bobController.getCameraHelper().getViewportHeight());
//			gameOverStage.addActor(restartSprite);
//			gameOverStage.addActor(gameOverImage);
//			
//			bobController.registerInputProcessor(gameOverStage);
//			
//			restartSprite.addListener(new ClickListener(){
//				@Override
//				public void clicked(InputEvent event, float x, float y) {
//					if(world.isGameOver()){
//						bobController.restartGame();
//					}
//				}
//			});
//		}
//		
//		 
//	}
	
	public class AssetBob{
		private static final float RUNNING_FRAME_DURATION = 0.06f;  
		
		public AtlasRegion bobEmptyRegion;
		public AtlasRegion bobIdleLeftRegion;
		public AtlasRegion bobIdleRightRegion;
		public AtlasRegion bobJumpLeftRegion;
		public AtlasRegion bobJumpRightRegion;
		public AtlasRegion bobFallLeftRegion;
		public AtlasRegion bobFallRightRegion;
		
		public Animation walkingLeftAnimation;
		public Animation walkingRightAnimation;
		public Animation bobWalkingDamagedLeftAnimation;
		public Animation bobWalkingDamagedRightAnimation;
		public Animation bobIdleDamagedLeftAnimation;
		public Animation bobIdleDamagedRightAnimation;
		public Animation bobJumpingDamagedLeftAnimation;
		public Animation bobJumpingDamagedRightAnimation;
		public Animation bobFallDamagedLeftAnimation;
		public Animation bobFallDamagedRightAnimation;
		
		public AssetBob(TextureAtlas atlas) {
			String bobPrefix = "bob-gun";
			bobEmptyRegion = atlas.findRegion("bob-empty");
			bobIdleLeftRegion = atlas.findRegion(bobPrefix+"-01");
			bobIdleRightRegion = new AtlasRegion(bobIdleLeftRegion);
			bobIdleRightRegion.flip(true, false);
			
			bobJumpLeftRegion = atlas.findRegion(bobPrefix+"-up");
			bobJumpRightRegion = new AtlasRegion(bobJumpLeftRegion);
			bobJumpRightRegion.flip(true, false);
			
			bobFallLeftRegion = atlas.findRegion(bobPrefix+"-down");
			bobFallRightRegion = new AtlasRegion(bobFallLeftRegion);
			bobFallRightRegion.flip(true, false);
			
			AtlasRegion[] walkingLeftFrames = new AtlasRegion[5];
			AtlasRegion[] walkingRightFrames = new AtlasRegion[5];
			for(int i = 0; i < 5; i++){
				walkingLeftFrames[i] = atlas.findRegion(bobPrefix+"-0" + (i+2));
				walkingRightFrames[i] = new AtlasRegion(walkingLeftFrames[i]);
				walkingRightFrames[i].flip(true, false);
			}
			
			walkingLeftAnimation = new Animation(RUNNING_FRAME_DURATION, walkingLeftFrames);
			walkingRightAnimation = new Animation(RUNNING_FRAME_DURATION, walkingRightFrames);
			
			AtlasRegion[] walkingDamagedLeftFrames = new AtlasRegion[10];
			AtlasRegion[] walkingDamagedRightFrames = new AtlasRegion[10];
			for(int i = 0; i < 5; i++){
				walkingDamagedLeftFrames[(i*2)] = atlas.findRegion(bobPrefix+"-0" + (i+2));
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

		}
	}
	
	public class AssetLevelDecorator{
		public AtlasRegion blockRegion;
		
		public AssetLevelDecorator(TextureAtlas atlas){
			blockRegion = atlas.findRegion("block");
		}
	}
	
	public class AssetJoypad{
		public final AtlasRegion buttonRun;
		public final AtlasRegion buttonThrow;
		public final AtlasRegion buttonRestart;
		public final AtlasRegion areaRun;
		public final AtlasRegion powerIndicator;
		
		public AssetJoypad(TextureAtlas atlas) {
			buttonRun = atlas.findRegion("button-run");
			buttonThrow = atlas.findRegion("button-throw");
			buttonRestart 	= atlas.findRegion("button-restart");
			areaRun = atlas.findRegion("power-area");
			powerIndicator = atlas.findRegion("power-indicator");
		}
	}
	
	public class AssetPanel{
		public final AtlasRegion distanceBGRegion;
		
		public AssetPanel(TextureAtlas atlas) {
			distanceBGRegion = atlas.findRegion("distance-bg");
		}
	}
	
	public class AssetFont implements Disposable{
		public final BitmapFont fontSmall;
		public final BitmapFont fontNormal;
		private Texture fontTex;
		
		public AssetFont() {
			fontTex = new Texture(Gdx.files.internal("data/arial.png"),true);
			/*Setting Nearest Filter for reducing aliasing on font drawing.*/
			fontTex.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
			
			/* Loading arial font */
			fontNormal = new BitmapFont(Gdx.files.internal("data/arial.fnt"),new TextureRegion(fontTex),false);
			fontNormal.setColor(Color.BLACK);
			
			fontSmall = new BitmapFont(Gdx.files.internal("data/arial.fnt"),new TextureRegion(fontTex),false);
			fontSmall.setColor(Color.BLACK);
			fontSmall.setScale(0.5f);
		}

		@Override
		public void dispose() {
			fontSmall.dispose();
			fontNormal.dispose();
			fontTex.dispose();
		}
		
	}
	
	private static final String TAG = "[Assets]";
	
	public static final Assets instance = new Assets();
	
	public AssetBob bob;
	public AssetJoypad joypad;
	public AssetPanel panel;
	public AssetFont fonts;
	
	private AssetManager assetManager;
	
	private Assets(){
	}
	
	public void init(AssetManager manager){
		assetManager = manager;
		
		assetManager.setErrorListener(this);
		
		assetManager.load(Constants.TEXTURE_ATLAS_PACK, TextureAtlas.class);
		
		/*Wait all textures to be loaded */
		assetManager.finishLoading();
		
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_PACK);
		
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		bob = new AssetBob(atlas);
//		joypad = new AssetJoypad(atlas);
//		panel = new AssetPanel(atlas);
//		fonts = new AssetFont();
	}
	
	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.dispose();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.debug(TAG, "Couldn't load asset "+asset.fileName,((Exception)throwable));
	}

}
