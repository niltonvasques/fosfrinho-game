package com.niltonvasques.fosfrinho.components.bob;

import com.niltonvasques.fosfrinho.components.HealthComponent;
import com.niltonvasques.fosfrinho.components.comm.Message;

public class BobHealthComponent extends HealthComponent{

	@Override
	public void receive(Message m) {
		switch (m) {
		case DAMAGED:
			if(super.isAlive()){
				super.setDamaged(true);
			}
			break;

		default:
			break;
		}
		
	}

}
