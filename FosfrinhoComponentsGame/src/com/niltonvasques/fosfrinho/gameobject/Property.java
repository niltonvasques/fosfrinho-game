package com.niltonvasques.fosfrinho.gameobject;

public class Property <T>{
	public String name;
	public T value;	
	
	public Property(String name, T value) {
		this.name = name;
		this.value = value;
	}
}
