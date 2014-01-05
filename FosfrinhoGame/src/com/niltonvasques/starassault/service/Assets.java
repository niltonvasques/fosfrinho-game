package com.niltonvasques.starassault.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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
	
	
	public class AssetBob{
		private static final float RUNNING_FRAME_DURATION = 0.06f;  
		
		public final AtlasRegion bobEmptyRegion;
		public final AtlasRegion bobIdleLeftRegion;
		public final AtlasRegion bobIdleRightRegion;
		public final AtlasRegion bobJumpLeftRegion;
		public final AtlasRegion bobJumpRightRegion;
		public final AtlasRegion bobFallLeftRegion;
		public final AtlasRegion bobFallRightRegion;
		
		public final Animation walkingLeftAnimation;
		public final Animation walkingRightAnimation;
		public final Animation bobWalkingDamagedLeftAnimation;
		public final Animation bobWalkingDamagedRightAnimation;
		public final Animation bobIdleDamagedLeftAnimation;
		public final Animation bobIdleDamagedRightAnimation;
		public final Animation bobJumpingDamagedLeftAnimation;
		public final Animation bobJumpingDamagedRightAnimation;
		public final Animation bobFallDamagedLeftAnimation;
		public final Animation bobFallDamagedRightAnimation;
		
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
	
	public class AssetGameInfo{
		public final TextureRegion heart;
		
		public AssetGameInfo(TextureAtlas atlas) {
			heart = atlas.findRegion("heart");
		}
	}
	
	public class AssetShoots implements Disposable{
		
		public final Texture shootTexture;
		
		public AssetShoots() {
			Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
			pixmap.setColor(Color.WHITE);
			pixmap.drawPixel(0, 0);
			
			shootTexture = new Texture(pixmap);
		}
		
		@Override
		public void dispose() {
			shootTexture.dispose();
		}
	}
	
	public class AssetItems{
		
		private static final float LOAD_FRAME_DURATION = 0.2f;
		
		public final TextureRegion keyRegion;
		
		public final Animation loadAnimation;
		
		public AssetItems(TextureAtlas atlas) {
			AtlasRegion[] loadFrames = new AtlasRegion[3];
			for(int i = 0; i < 3; i++){
				loadFrames[i] = atlas.findRegion("load-0"+(i+1));
			}
			loadAnimation = new Animation(LOAD_FRAME_DURATION, loadFrames);
			
			keyRegion = atlas.findRegion("key");
		}
	}
	
	public class AssetGameOverMenu{
		
		public final AtlasRegion gameOverRegion;
		public final AtlasRegion restartRegion;
		
		public AssetGameOverMenu(TextureAtlas atlas) {
				gameOverRegion = atlas.findRegion("game-over");
				restartRegion = atlas.findRegion("restart");
		}
	}
	
	public class AssetLevelDecorator{
		public final AtlasRegion blockRegion;
		public final AtlasRegion doorRegion;
		public final AtlasRegion gateRegion;
		
		public AssetLevelDecorator(TextureAtlas atlas){
			blockRegion = atlas.findRegion("block");
			doorRegion = atlas.findRegion("door");
			gateRegion = atlas.findRegion("gate");
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
	
	public class AssetEnemys{
		public final AtlasRegion zombieRegion;
		public final AtlasRegion catazombieRegion;
		
		public AssetEnemys(TextureAtlas atlas) {
			zombieRegion = atlas.findRegion("zumbi-01");
			catazombieRegion = atlas.findRegion("catazumbi-01");
		}
	}
	
	public class AssetFont implements Disposable{
//		public final BitmapFont fontSmall;
		public final BitmapFont fontNormal;
//		private Texture fontTex;
		
		public AssetFont() {
			
//			//Load font
			fontNormal = new BitmapFont();
			fontNormal.setScale(2f);
			fontNormal.setColor(Color.WHITE);
//			fontTex = new Texture(Gdx.files.internal("data/arial.png"),true);
//			/*Setting Nearest Filter for reducing aliasing on font drawing.*/
//			fontTex.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
//			
//			/* Loading arial font */
//			fontNormal = new BitmapFont(Gdx.files.internal("data/arial.fnt"),new TextureRegion(fontTex),false);
//			fontNormal.setColor(Color.BLACK);
//			
//			fontSmall = new BitmapFont(Gdx.files.internal("data/arial.fnt"),new TextureRegion(fontTex),false);
//			fontSmall.setColor(Color.BLACK);
//			fontSmall.setScale(0.5f);
		}

		@Override
		public void dispose() {
			fontNormal.dispose();
		}
	}
	
	public class AssetSounds{
		
	}
	
	public class AssetMusic implements Disposable{
		public Music levelMusic;
		
		public AssetMusic() {
			levelMusic = Gdx.audio.newMusic(Gdx.files.internal("data/disire.mp3"));
			levelMusic.setLooping(true);
		}
		
		@Override
		public void dispose() {
			levelMusic.dispose();
		}
	}
	
	private static final String TAG = "[Assets]";
	
	public static final Assets instance = new Assets();
	
	public AssetBob bob;
	public AssetGameInfo gameInfo;
	public AssetItems items;
	public AssetGameOverMenu gameOverMenu;
	public AssetLevelDecorator level;
	public AssetEnemys enemys;
	public AssetShoots shoots;
//	public AssetJoypad joypad;
	public AssetFont fonts;
	
	public AssetMusic music;
	
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
		gameInfo = new AssetGameInfo(atlas);
		items = new AssetItems(atlas);
		gameOverMenu = new AssetGameOverMenu(atlas);
		level = new AssetLevelDecorator(atlas);
		enemys = new AssetEnemys(atlas);
		shoots = new AssetShoots();
		fonts = new AssetFont();
		
		music = new AssetMusic();
	}
	
	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.dispose();
		shoots.dispose();
		music.dispose();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.debug(TAG, "Couldn't load asset "+asset.fileName,((Exception)throwable));
	}

}
