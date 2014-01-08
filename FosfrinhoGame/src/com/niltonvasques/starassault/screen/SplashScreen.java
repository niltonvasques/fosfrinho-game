package com.niltonvasques.starassault.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.niltonvasques.starassault.service.Assets;

public class SplashScreen implements Screen{
	
	private Game mGame;
	
	private OrthographicCamera mCamera;
	private Texture mSplashTexture;
	private TextureRegion mSplashTextureRegion;
	private SpriteBatch mBatch;
	private BitmapFont mFont;
	
	private Boolean mTouched = false;
	
	public SplashScreen( Game game) {
		mGame = game;
		
		mCamera = new OrthographicCamera();
		mCamera.setToOrtho(false, 800, 480);
		
		// instantiating a sprite batch 
		mBatch = new SpriteBatch();
		
		// instantiating a font for draw texts on the screen
		mFont = new BitmapFont();
		
		// load the splash image and create the texture region
		mSplashTexture = new Texture("data/splash.png");
 
        // we set the linear texture filter to improve the stretching
		mSplashTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
 
        // in the image atlas, our splash image begins at (0,0) at the
        // upper-left corner and has a dimension of 640x480
		mSplashTextureRegion = new TextureRegion( mSplashTexture, 0, 0, 512, 307 );
		
	}
	
	@Override
	public void render(float delta) {
		
		mCamera.update();
		mBatch.setProjectionMatrix(mCamera.combined);
		
		mBatch.begin();
		
		mBatch.draw(mSplashTextureRegion, 0, 0, 800, 480 );		
		
		mFont.setScale(2);
		mFont.draw(mBatch, "Tap anywhere to begin!", 250, 50);
		
		mBatch.end();
		
		if(Gdx.input.isTouched()){
//			mTouched = true;
			mGame.setScreen(new GameScreen());
			dispose();
		}
	}
	
	@Override
	public void dispose() {
		mSplashTexture.dispose();
		mBatch.dispose();
		mFont.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean loading = false;

	@Override
	public void show() {
		
		Assets.instance.init(new AssetManager());		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
