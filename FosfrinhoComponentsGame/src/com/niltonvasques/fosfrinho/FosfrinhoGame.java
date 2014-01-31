package com.niltonvasques.fosfrinho;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.niltonvasques.fosfrinho.ui.screens.MenuScreen;
import com.niltonvasques.fosfrinho.util.net.udp.UDPTransfer;
import com.niltonvasques.fosfrinho.util.resources.Assets;

public class FosfrinhoGame extends Game {
	
	@Override
	public void create() {
		Assets.instance.init(new AssetManager());
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		setScreen(new MenuScreen());
	}

	@Override
	public void dispose() {
		getScreen().dispose();
		Assets.instance.dispose();
	}

	@Override
	public void render() {		
		super.render();
		UDPTransfer.getInstance().update(Gdx.graphics.getDeltaTime());
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
