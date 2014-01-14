package com.niltonvasques.fosfrinho.components.bob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.niltonvasques.fosfrinho.components.Component;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;

public class BobInputComponent implements Component{
	private static final String TAG = "[BobInputComponent]";
	
	@Override
	public void update(GameObject o, float delta) {
		
		if(Gdx.input.isKeyPressed(Keys.J)){
			o.send(Message.BTN_LEFT_PRESSED);
		}
		
		if(Gdx.input.isKeyPressed(Keys.L)){
			o.send(Message.BTN_RIGHT_PRESSED);
		}
	}

	@Override
	public void receive(Message m) {
		// TODO Auto-generated method stub
		
	}

}
