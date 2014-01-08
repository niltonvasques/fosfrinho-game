package com.niltonvasques.starassault.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.niltonvasques.starassault.service.Assets;

public class DebugRenderer extends Stage implements View{
	
	private Table window;
	private Label fpsLabel;
	
	private boolean debug;

	public DebugRenderer() {
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
	public void drawView(SpriteBatch batch) {
		this.draw();	
		if(debug) Table.drawDebug(this);
	}

	@Override
	public float getPositionX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getPositionY() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}


}
