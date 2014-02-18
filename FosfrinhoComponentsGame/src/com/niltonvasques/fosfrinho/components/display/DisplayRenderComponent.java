package com.niltonvasques.fosfrinho.components.display;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.niltonvasques.fosfrinho.components.ContainerCom;
import com.niltonvasques.fosfrinho.components.DrawComponent;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.ui.view.FPSRenderer;

public class DisplayRenderComponent extends DrawComponent {
	
	private FPSRenderer fpsRenderer;
	private List<Stage> stages = new ArrayList<Stage>();
	public DisplayRenderComponent(GameObject o) {
		super(o);
		fpsRenderer = new FPSRenderer();
		
		o.subscribeEvent(Message.ADD_STAGE,this);
	}
	
	public void addStage(Stage stage){
		stages.add(stage);
	}
	
	@Override
	public void update(ContainerCom o, float delta) {
		fpsRenderer.act();
		for (Stage s : stages) {
			s.act();
		}
	}

	@Override
	public void receive(Message m, Object... data) {
		switch (m) {
		case ADD_STAGE:
			addStage((Stage) data[0]);
			break;

		default:
			break;
		}

	}
	
	@Override
	public void draw(SpriteBatch batch, GameObject o) {
		fpsRenderer.draw();
		for (Stage s : stages) {
			s.draw();
		}
	}

	@Override
	public TextureRegion getRenderFrame() {
		// TODO Auto-generated method stub
		return null;
	}

}
