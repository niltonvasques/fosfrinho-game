package com.niltonvasques.fosfrinho.gameobject;

public class Action {
	public enum Type{
		CREATE_SHOOT, 
		DESTROY_GAME_OBJ
	}
	
	public final Type type;
	
	public final Object data;
	
	public final GameObject owner;
	
	public Action(GameObject owner, Type t, Object data) {
		this.type = t;
		this.data = data;
		this.owner = owner;
	}
	
	public Action(GameObject owner,Type t) {
		this(owner,t,null);
	}
}
