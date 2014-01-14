package com.niltonvasques.fosfrinho.components;

public abstract class ContainerCom {
	
	public static final int COMPONENTS_MAX_CAPACITY = 10;
	
	private Component[] components;
	
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
}
