package com.niltonvasques.fosfrinho.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.GameObjectFactory;
import com.niltonvasques.fosfrinho.input.InputManager;
import com.niltonvasques.fosfrinho.level.Level;
import com.niltonvasques.fosfrinho.level.LevelLoader;
import com.niltonvasques.fosfrinho.physics.PhysicsManager;
import com.niltonvasques.fosfrinho.util.Assets;
import com.niltonvasques.fosfrinho.util.CameraHelper;
import com.niltonvasques.fosfrinho.util.ScreenUtil;

public class GameScreen implements Screen{
	private static final String TAG = "[GameScreen]";
	
	private GameObject bob;
	
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
		
		//Updating
		Array<GameObject> blocks = level.getDrawableBlocks((int)cameraHelper.getPosition().x, (int)cameraHelper.getPosition().y,
				(int)cameraHelper.getViewportWidth(),(int)cameraHelper.getViewportHeight());
		for(GameObject o : blocks){
			o.update(delta);
		}
		bob.update(delta);
		
		//Drawing
		cameraHelper.update(delta);
		cameraHelper.applyTo(cam);
		batch.setProjectionMatrix(cam.combined);
		
		batch.begin();
//		
			for(GameObject o : blocks){
				o.draw(batch);
			}
////			
			bob.draw(batch);
//			
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Assets.instance.init(new AssetManager());
		
		input = new InputManager();
		
		Gdx.input.setInputProcessor(input);
		
		batch = new SpriteBatch();
		
		level = LevelLoader.loadLevel(batch, 3);
		
		bob = GameObjectFactory.createBobGameObject(batch,level.getSpanPosition().x,level.getSpanPosition().y);
		
		input.addListener(bob);
		
		this.cameraHelper = new CameraHelper();
		this.cameraHelper.setTarget(bob);
		
		this.cam = new OrthographicCamera(cameraHelper.getViewportWidth(), cameraHelper.getViewportHeight());
		this.cameraHelper.applyTo(cam);
		
//		PhysicsManager.instance.enableDebug(cam);
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
		// TODO Auto-generated method stub
		
	}

}
