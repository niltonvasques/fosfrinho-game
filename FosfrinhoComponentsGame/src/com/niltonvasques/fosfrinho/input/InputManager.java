package com.niltonvasques.fosfrinho.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.niltonvasques.fosfrinho.components.comm.CommunicationCom;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;

public class InputManager implements InputProcessor{
	private Array<CommunicationCom> listeners = new Array<CommunicationCom>();
	
	public void addListener(CommunicationCom o){
		listeners.add(o);
	}
	
	public void removeListener(CommunicationCom c){
		listeners.removeValue(c, true);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.Z){
			for(CommunicationCom o : listeners){
				o.send(Message.BOB_JUMP_PRESSED);
			}
		}
		
		if(keycode == Keys.J){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_LEFT_PRESSED);
			}
		}
		
		if(keycode == Keys.L){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_RIGHT_PRESSED);
			}
		}
		
		if(keycode == Keys.EQUALS){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_PLUS);
			}
		}
		
		if(keycode == Keys.MINUS){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_MINUS);
			}
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.Z){
			for(CommunicationCom o : listeners){
				o.send(Message.BOB_JUMP_RELEASED);
			}
		}
		
		if(keycode == Keys.J){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_LEFT_RELEASED);
			}
		}
		
		if(keycode == Keys.L){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_RIGHT_RELEASED);
			}
		}
		
		if(keycode == Keys.D){
			for(CommunicationCom o : listeners){
				o.send(Message.DEBUG);
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
