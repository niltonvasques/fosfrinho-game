package com.niltonvasques.fosfrinho.components;

import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;

public interface Component {
	
	public void update(GameObject o, float delta);
	public void receive(Message m);
	
}
