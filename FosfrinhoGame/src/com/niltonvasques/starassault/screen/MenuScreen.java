package com.niltonvasques.starassault.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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

public class MenuScreen implements Screen{
	private static String TAG = "[MenuScreen]";
	
	private FosfrinhoGame game;

	private Stage ui;
	private Table window;
	
	private Label fpsLabel;
	
	
	public MenuScreen() {
		
		this.game = (FosfrinhoGame)Gdx.app.getApplicationListener();
		
	}
	
	@Override
	public void render(float delta) {

		ScreenUtil.setClearColor(Constants.BACKGROUND_COLOR);
		ScreenUtil.clear();
		
		fpsLabel.setText("fps: "+Gdx.graphics.getFramesPerSecond());
		
		ui.draw();
		
//		Table.drawDebug(ui);
		
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
	    
	    Label title = new Label("Main Menu",Assets.instance.skin.skin);
	    window.row().fill(false,false).expand(true,false).padTop(50).padBottom(50);
	    window.add(title);
	    
	    
	    Table container = new Table(Assets.instance.skin.skin);
	    
	    TextButton newGame = new TextButton("New Game",Assets.instance.skin.skin);
	    TextButton optionMenu = new TextButton("Options",Assets.instance.skin.skin);
	    TextButton helpMenu = new TextButton("Help",Assets.instance.skin.skin);
	    
	    container.row().fill(true, true).expand(true, true).pad(10, 0, 10, 0);
	    container.add(newGame);
	    container.row().fill(true, true).expand(true, true).pad(10, 0, 10, 0);
	    container.add(optionMenu);
	    container.row().fill(true, true).expand(true, true).pad(10, 0, 10, 0);
	    container.add(helpMenu);
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
	    
	    newGame.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
	    		game.setScreen(new GameScreen());
	    		MenuScreen.this.dispose();
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
