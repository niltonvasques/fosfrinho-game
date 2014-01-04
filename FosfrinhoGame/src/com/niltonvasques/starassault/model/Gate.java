package com.niltonvasques.starassault.model;

public class Gate extends Door{

	public Gate(float x, float y) {
		super(null, x, y);
	}
	
	@Override
	public boolean open(Key key) {
		return true;
	}
	
	@Override
	public boolean isOpen() {
		return true;
	}

}
