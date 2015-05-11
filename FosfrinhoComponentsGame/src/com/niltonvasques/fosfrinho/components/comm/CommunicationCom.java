package com.niltonvasques.fosfrinho.components.comm;

public interface CommunicationCom {
	
	public void send(Message message, Object... data);
	
	public void update(float delta);
}
