package com.niltonvasques.starassault;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.niltonvasques.starassault.screen.MenuScreen;
import com.niltonvasques.starassault.screen.SplashScreen;

public class FosfrinhoGame extends Game {
	
	private long scoreTime = 0;
	
	@Override
	public void create() {		
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		setScreen(new SplashScreen());
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

	public long getScoreTime() {
		return scoreTime;
	}

	public void setScoreTime(long scoreTime) {
		this.scoreTime = scoreTime;
	}
	
}
