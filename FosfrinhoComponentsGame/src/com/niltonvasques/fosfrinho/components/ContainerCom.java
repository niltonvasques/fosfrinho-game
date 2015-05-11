package com.niltonvasques.fosfrinho.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.niltonvasques.fosfrinho.components.comm.CommunicationCom;
import com.niltonvasques.fosfrinho.components.comm.Message;

public abstract class ContainerCom implements CommunicationCom{
	
	public static final int COMPONENTS_MAX_CAPACITY = 10;
	
	private Component[] components;
	
	private Map<Message, Array<Component>> eventsSubscribes = new HashMap<Message, Array<Component>>();
	
	public ContainerCom() {
		this.components = new Component[COMPONENTS_MAX_CAPACITY];
		for(int i = 0; i < COMPONENTS_MAX_CAPACITY; i++){
			this.components[i] = null;
		}
	}
	
	public Component[] getComponents() {
		return components;
	}
	
	public boolean addComponent(Component component) {
		
		for(int i = 0; i < COMPONENTS_MAX_CAPACITY; i++){
			if(this.components[i] == null){
				this.components[i] = component;
				return true;
			}
		}
		
		return false;
	}
	
	public void subscribeEvent(Message event, Component c){
		if(eventsSubscribes.containsKey(event)){
			eventsSubscribes.get(event).add(c);
		}else{
			eventsSubscribes.put(event,new Array<Component>());
			eventsSubscribes.get(event).add(c);
		}
	}
	
	@Override
	public void send(Message message, Object... data){
		if(!eventsSubscribes.containsKey(message)) return;
		
		for(Component c : eventsSubscribes.get(message)){
			c.receive(message,data);
		}
	}

}
