package com.niltonvasques.starassault.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.niltonvasques.starassault.FosfrinhoGame;
import com.niltonvasques.starassault.service.Assets;
import com.niltonvasques.starassault.service.Resources;

public class SplashScreen implements Screen{
	
	private static final float SPLASH_SHOW_TIME = 2f;
	
	private Game mGame;
	
	private OrthographicCamera mCamera;
	private SpriteBatch mBatch;
	
	private float stateTime = 0f;
	
	public SplashScreen() {
		mGame = (FosfrinhoGame)Gdx.app.getApplicationListener();
		
		mCamera = new OrthographicCamera();
		mCamera.setToOrtho(false, 800, 480);
		
		// instantiating a sprite batch 
		mBatch = new SpriteBatch();
		
	}
	
	@Override
	public void render(float delta) {
		
		stateTime += delta;
		
		mCamera.update();
		mBatch.setProjectionMatrix(mCamera.combined);
		
		mBatch.begin();
		
		mBatch.draw(Assets.instance.splash.splashScreen, 0, 0, 800, 480 );		
		
		mBatch.end();
		
		if(stateTime > SPLASH_SHOW_TIME){
			mGame.setScreen(new MenuScreen());
			dispose();
		}
	}
	
	@Override
	public void dispose() {
		mBatch.dispose();
		Assets.instance.dispose(Resources.SPLASH_ATLAS_PACK);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
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
