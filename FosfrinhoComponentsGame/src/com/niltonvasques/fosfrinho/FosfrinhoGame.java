package com.niltonvasques.fosfrinho;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.niltonvasques.fosfrinho.screens.GameScreen;

public class FosfrinhoGame extends Game {
	
	@Override
	public void create() {		
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		setScreen(new GameScreen());
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
