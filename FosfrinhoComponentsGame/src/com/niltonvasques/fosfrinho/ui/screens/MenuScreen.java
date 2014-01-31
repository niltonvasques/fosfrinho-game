package com.niltonvasques.fosfrinho.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.niltonvasques.fosfrinho.FosfrinhoGame;
import com.niltonvasques.fosfrinho.util.ScreenUtil;
import com.niltonvasques.fosfrinho.util.net.HostPacket;
import com.niltonvasques.fosfrinho.util.net.TransferProtocol.OnReceive;
import com.niltonvasques.fosfrinho.util.net.udp.UDPTransfer;
import com.niltonvasques.fosfrinho.util.resources.Assets;
import com.niltonvasques.fosfrinho.util.resources.Constants;

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
	    
	    TextButton newGameClient = new TextButton("New Game - Client Mode",Assets.instance.skin.skin);
	    TextButton newGameServer = new TextButton("New Game - Server Mode",Assets.instance.skin.skin);
	    TextButton helpMenu = new TextButton("Help",Assets.instance.skin.skin);
	    
	    container.row().fill(true, true).expand(true, true).pad(10, 0, 10, 0);
	    container.add(newGameClient);
	    container.row().fill(true, true).expand(true, true).pad(10, 0, 10, 0);
	    container.add(newGameServer);
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
	    
	    newGameClient.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
	    		
	    		UDPTransfer.getInstance().setOnReceive(new OnReceive() {
					@Override
					public void onReceive(HostPacket msg) {
						game.setScreen(new GameScreen());
						MenuScreen.this.dispose();
					}
				});
	    		
	    		UDPTransfer.getInstance().startClient();
	    	};
	    	
	    });
	    
	    newGameServer.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
	    		UDPTransfer.getInstance().setOnReceive(new OnReceive() {
					@Override
					public void onReceive(HostPacket msg) {
						game.setScreen(new GameScreen());
						MenuScreen.this.dispose();
					}
				});
	    		
	    		UDPTransfer.getInstance().startServer();
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
