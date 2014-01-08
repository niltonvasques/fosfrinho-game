package com.niltonvasques.starassault.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.niltonvasques.starassault.controller.BobController;
import com.niltonvasques.starassault.service.Assets;
import com.niltonvasques.starassault.util.ColorUtil;
import com.niltonvasques.starassault.view.WorldRenderer;

public class GameScreen implements Screen, InputProcessor {
	private static final String TAG = "[GameScreen]";
	private static final boolean LOG = false;
	
	private static final int BACKGROUND_COLOR = 0x480e79;
	
	private int[] backgroundColorArr = ColorUtil.rgbToArray(BACKGROUND_COLOR);
	
	private WorldRenderer worldRenderer;
	private BobController controller;
	
	public GameScreen() {
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(backgroundColorArr[0]/255f, backgroundColorArr[1]/255f, backgroundColorArr[2]/255f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		controller.update(delta);
		worldRenderer.render();
		
		if(LOG) Gdx.app.log(TAG, " FPS "+Gdx.graphics.getFramesPerSecond());
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.setSize(width, height);
	}

	@Override
	public void show() {
		controller = new BobController();
		worldRenderer = new WorldRenderer(controller,false);
		controller.registerInputProcessor(this);
		
		Assets.instance.music.levelMusic.play();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
		worldRenderer.dispose();
		Assets.instance.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.J)
			controller.leftPressed();
		if(keycode == Keys.L)
			controller.rightPressed();
		if(keycode == Keys.Z)
			controller.jumpPressed();
		if(keycode == Keys.X)
			controller.firePressed();
		if(keycode == Keys.D){
			worldRenderer.setDebug(!worldRenderer.isDebug());
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.J)
			controller.leftReleased();
		if(keycode == Keys.L)
			controller.rightReleased();
		if(keycode == Keys.Z)
			controller.jumpReleased();
		if(keycode == Keys.X)
			controller.fireReleased();
		if(keycode == Keys.ESCAPE)
			Gdx.app.exit();
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(screenY < Gdx.graphics.getHeight()/2){
			if(screenX < Gdx.graphics.getWidth()/2){
				controller.firePressed();
			}else{
				controller.jumpPressed();
			}			
		}else if(screenX < Gdx.graphics.getWidth()/2){
			controller.leftPressed();
		}else{
			controller.rightPressed();
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(screenY < Gdx.graphics.getHeight()/2){
			if(screenX < Gdx.graphics.getWidth()/2){
				controller.fireReleased();
			}else{
				controller.jumpReleased();
			}			
		}else if(screenX < Gdx.graphics.getWidth()/2){
			controller.leftReleased();
		}else{
			controller.rightReleased();
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
