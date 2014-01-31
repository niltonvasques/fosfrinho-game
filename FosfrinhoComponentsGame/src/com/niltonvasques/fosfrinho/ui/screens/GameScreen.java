package com.niltonvasques.fosfrinho.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.niltonvasques.fosfrinho.components.comm.CommunicationCom;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.GameObjectFactory;
import com.niltonvasques.fosfrinho.input.InputManager;
import com.niltonvasques.fosfrinho.level.Level;
import com.niltonvasques.fosfrinho.level.LevelLoader;
import com.niltonvasques.fosfrinho.physics.PhysicsManager;
import com.niltonvasques.fosfrinho.util.CameraHelper;
import com.niltonvasques.fosfrinho.util.ScreenUtil;
import com.niltonvasques.fosfrinho.util.resources.Assets;

public class GameScreen implements Screen{
	
	private static final String TAG = "[GameScreen]";
	
	private GameObject display;
	
	private CameraHelper cameraHelper;
	private OrthographicCamera cam;
	private SpriteBatch batch;
	private Level level;
	private InputManager input;
	
	@Override
	public void render(float delta) {
		ScreenUtil.setClearColor(Color.BLACK);
		ScreenUtil.clear();
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			Gdx.app.exit();
			return;
		}
		
		PhysicsManager.instance.Update(delta);
		
		display.update(delta);
		level.update(delta);
		
		//Drawing
		cameraHelper.update(delta);
		cameraHelper.applyTo(cam);
		
		level.prepareDraw((int)cameraHelper.getPosition().x, (int)cameraHelper.getPosition().y,
				(int)cameraHelper.getViewportWidth(),(int)cameraHelper.getViewportHeight());
		
		if(!PhysicsManager.instance.isDebug()){
			batch.setProjectionMatrix(cam.combined);
			
			batch.begin();
				
				level.draw(batch);
				
			batch.end();
		}
		
		display.draw(batch);
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		
		input = new InputManager();
		input.addListener(comm);
		
		Gdx.input.setInputProcessor(input);
		
		batch = new SpriteBatch();
		
		level = LevelLoader.loadLevel(3);
		
		input.addListener(level.getBob());
		
		this.cameraHelper = new CameraHelper();
		this.cameraHelper.setTarget(level.getBob());
		
		this.cam = new OrthographicCamera(cameraHelper.getViewportWidth(), cameraHelper.getViewportHeight());
		this.cameraHelper.applyTo(cam);
		
		display = GameObjectFactory.createFpsDisplayGameObject(0, 0);
		
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

	@Override
	public void dispose() {
		level.send(Message.DISPOSE);
	}

	private CommunicationCom comm = new CommunicationCom() {
		
		@Override
		public void update(float delta) { }
		
		@Override
		public void send(Message message) {
			switch(message){
			case DEBUG:
				if(PhysicsManager.instance.isDebug()){
					PhysicsManager.instance.disableDebug();
				}else{
					PhysicsManager.instance.enableDebug(cam);
				}
				break;
				
			case BTN_PLUS:
				cameraHelper.addZoom(-0.1f);
				break;
				
			case BTN_MINUS:
				cameraHelper.addZoom(0.1f);
				break;
			}
		}
	};

}
