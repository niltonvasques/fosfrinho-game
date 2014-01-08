package com.niltonvasques.starassault;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.niltonvasques.starassault.screen.GameScreen;
import com.niltonvasques.starassault.screen.SplashScreen;

public class StarAssaultGame extends Game {
	
	@Override
	public void create() {		
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
