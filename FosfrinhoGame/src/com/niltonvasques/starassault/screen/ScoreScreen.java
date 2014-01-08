package com.niltonvasques.starassault.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.niltonvasques.starassault.FosfrinhoGame;
import com.niltonvasques.starassault.service.Assets;
import com.niltonvasques.starassault.util.Constants;
import com.niltonvasques.starassault.util.ScreenUtil;

public class ScoreScreen implements Screen{
	private static String TAG = "[MenuScreen]";
	
	private FosfrinhoGame game;

	private Stage ui;
	private Table window;
	
	private Label fpsLabel;
	
	
	public ScoreScreen() {
		
		this.game = (FosfrinhoGame)Gdx.app.getApplicationListener();
		
	}
	
	@Override
	public void render(float delta) {

		ScreenUtil.setClearColor(Color.BLACK);
		ScreenUtil.clear();
		
		fpsLabel.setText("fps: "+Gdx.graphics.getFramesPerSecond());
		
		ui.draw();
		
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log(TAG, "resize");
		
	    ui = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), true);
	    Gdx.input.setInputProcessor(ui);
	    window = new Table(Assets.instance.skin.skin);
	    window.setWidth(ui.getWidth());
	    window.setHeight(ui.getHeight());
	    window.setPosition(0, 0);
	    window.debug();
	    
	    Label title = new Label("Score ",Assets.instance.skin.skin);
	    window.row().fill(false,false).expand(true,false).padTop(50).padBottom(50);
	    window.add(title);
	    
	    
	    Table container = new Table(Assets.instance.skin.skin);
	    
	    Label score = new Label("Clear Time: "+game.getScoreTime()/1000+" seconds",Assets.instance.skin.skin);
	    Label optionMenu = new Label("",Assets.instance.skin.skin);
	    TextButton continueButton = new TextButton("Continue",Assets.instance.skin.skin);
	    
	    container.row().fill(true, true).expand(true, true).pad(10, 0, 10, 0);
	    container.add(score);
	    container.row().fill(true, true).expand(true, true).pad(10, 0, 10, 0);
	    container.add(optionMenu);
	    container.row().fill(true, true).expand(true, true).pad(10, 0, 10, 0);
	    container.add(continueButton);
	    window.row().fill(0.5f,1f).expand(true,true);
	    window.add(container);
	    
	    
	    Table extras = new Table(Assets.instance.skin.skin);
	    
	    fpsLabel = new Label("fps: ", Assets.instance.skin.skin);
	    Image libgdx = new Image(Assets.instance.skin.badlogicSmallRegion);
	    extras.row().fill(false,false).expand(true,true);
	    extras.add(fpsLabel).left().center().pad(0,25,25,0); 
	    extras.add(libgdx).right().center().pad(0,0,25,25);
	    window.row().fill(true,false).expand(true,true);
	    window.add(extras).bottom();
	    
	    ui.addActor(window);
	    
	    continueButton.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
	    		game.setScreen(new GameScreen());
	    		ScoreScreen.this.dispose();
	    	};
	    	
	    });
	}
	

	@Override
	public void show() {
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
