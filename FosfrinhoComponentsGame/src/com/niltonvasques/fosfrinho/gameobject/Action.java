package com.niltonvasques.fosfrinho.gameobject;

public class Action {
	public enum Type{
		CREATE_SHOOT, NETWORK_SHOOT,
		DESTROY_GAME_OBJ, ADD_STAGE, END_ROUND
	}
	
	public final Type type;
	
	public final Object data[];
	
	public final GameObject owner;
	
	public Action(GameObject owner, Type t, Object... data) {
		this.type = t;
		this.data = data;
		this.owner = owner;
	}
	
	public Action(GameObject owner,Type t) {
		this(owner,t,null);
	}
}
