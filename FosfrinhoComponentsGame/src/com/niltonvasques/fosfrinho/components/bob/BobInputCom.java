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
	
	public BobInputCom(GameObject o) {
		this.object = o;
	}
	
	@Override
	public void update(ContainerCom o, float delta) {
		
		if(Gdx.input.isKeyPressed(Keys.J)){
			o.send(Message.BTN_LEFT_PRESSED);
		}
		
		if(Gdx.input.isKeyPressed(Keys.L)){
			o.send(Message.BTN_RIGHT_PRESSED);
		}
		
		if(Gdx.input.isKeyPressed(Keys.X)){
			o.send(Message.FIRE);
		}
	}

	@Override
	public void receive(Message m) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public GameObject getGameObject() {
		return object;
	}

}
