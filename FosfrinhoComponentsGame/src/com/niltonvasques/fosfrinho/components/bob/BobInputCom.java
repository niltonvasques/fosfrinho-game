package com.niltonvasques.fosfrinho.components.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.niltonvasques.fosfrinho.components.Component;
import com.niltonvasques.fosfrinho.components.ContainerCom;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;

public class BobInputCom implements Component{
	private static final String TAG = "[BobInputComponent]";
	private GameObject object;
	private boolean dead = false;
	
	public BobInputCom(GameObject o) {
		this.object = o;
		o.subscribeEvent(Message.DEAD, this);
	}
	
	@Override
	public void update(ContainerCom o, float delta) {
		
		if(dead) return;
		
		if(Gdx.input.isKeyPressed(Keys.J)){
			o.send(Message.BTN_LEFT_PRESSED, null);
		}
				
		if(Gdx.input.isKeyPressed(Keys.L)){
			o.send(Message.BTN_RIGHT_PRESSED, null);
		}
		
		if(Gdx.input.isKeyPressed(Keys.X)){
			o.send(Message.FIRE, null);
		}
	}

	@Override
	public void receive(Message m, Object... data) {
		switch(m){
		case DEAD:
			dead = true;
			break;
		}
		
	}
	
	@Override
	public GameObject getGameObject() {
		return object;
	}

}
