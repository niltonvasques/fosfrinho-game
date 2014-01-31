package com.niltonvasques.fosfrinho.ui.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.niltonvasques.fosfrinho.util.resources.Assets;

public class FPSRenderer extends Stage {
	
	private Table window;
	private Label fpsLabel;
	
	private boolean debug;

	public FPSRenderer() {
		super(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), true);
		
	    window = new Table(Assets.instance.skin.skin);
	    window.setWidth(getWidth());
	    window.setHeight(getHeight());
	    window.setPosition(0, 0);
	    window.debug();
	    
	    Table extras = new Table(Assets.instance.skin.skin);
	    extras.row().fill(false,false).expand(true,true);
	    
	    fpsLabel = new Label("fps: ", Assets.instance.skin.skin);
	    extras.add(fpsLabel).right().pad(25,25,25,25);
	    
	    window.row().fill(true,false).expand(true,true);
	    window.add(extras).top();
	    
	    addActor(window);
	}
	
	@Override
	public void act() {
		super.act();
		
		fpsLabel.setText("fps: "+Gdx.graphics.getFramesPerSecond());
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		fpsLabel.setText("fps: "+Gdx.graphics.getFramesPerSecond());
	}
	
	@Override
	public void draw() {
		super.draw();
		if(debug) Table.drawDebug(this);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}


}
