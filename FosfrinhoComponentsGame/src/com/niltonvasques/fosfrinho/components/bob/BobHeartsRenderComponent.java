package com.niltonvasques.fosfrinho.components.bob;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.niltonvasques.fosfrinho.components.Component;
import com.niltonvasques.fosfrinho.components.ContainerCom;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.Action;
import com.niltonvasques.fosfrinho.gameobject.Action.Type;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.Property;
import com.niltonvasques.fosfrinho.util.resources.Assets;

public class BobHeartsRenderComponent implements Component{
	
	private Stage heartStage;
	private Table window;
	private Table extras;
	private List<Image> hearts = new ArrayList<Image>();
	
	private GameObject object;
	
	private static final String TAG = "[BobHeartsRenderCom]";

	public BobHeartsRenderComponent(GameObject o) {
		object = o;
		heartStage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), true);
		
		if(!o.getProperties().containsKey("HP")){
			o.getProperties().put("HP", new Property<Integer>("HP", 10));
		}
		
				
	    window = new Table(Assets.instance.skin.skin);
	    window.setWidth(Gdx.graphics.getWidth());
	    window.setHeight(Gdx.graphics.getHeight());
	    window.setPosition(0, 0);
	    
	    extras = new Table(Assets.instance.skin.skin);
	    extras.row().fill(true,false).expand(false,true);
	    

	    int hp = (Integer) object.getProperties().get("HP").value;
	    for(int i = 0; i < hp; i++ ){
	    	Image heart = new Image(Assets.instance.gameInfo.heart);
	    	hearts.add(heart);
	    	extras.add(heart).left().pad(25,25,25,25);
	    }
	    
	    window.row().fill(true,false).expand(true,true);
	    window.add(extras).top().left();
	    
	    heartStage.addActor(window);
	    
	    Action act = new Action(object, Type.ADD_STAGE, heartStage);
	    object.registerPendingAction(act);
	    
	    object.subscribeEvent(Message.DAMAGED, this);
	}

	@Override
	public GameObject getGameObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(ContainerCom o, float delta) {
		
	}

	@Override
	public void receive(Message m, Object... data) {
		switch (m) {
		case DAMAGED:
			if(hearts.size() > 0){
				Image heart = hearts.remove(hearts.size()-1);
				extras.removeActor(heart);
			}
			break;

		default:
			break;
		}
		
	}
	


}
