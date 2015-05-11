package com.niltonvasques.fosfrinho.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
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
				o.send(Message.BOB_JUMP_PRESSED, null);
			}
		}
		
		if(keycode == Keys.J){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_LEFT_PRESSED, null);
			}
		}
		
		if(keycode == Keys.L){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_RIGHT_PRESSED, null);
			}
		}
		
		if(keycode == Keys.EQUALS){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_PLUS, null);
			}
		}
		
		if(keycode == Keys.MINUS){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_MINUS, null);
			}
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.Z){
			for(CommunicationCom o : listeners){
				o.send(Message.BOB_JUMP_RELEASED, null);
			}
		}
		
		if(keycode == Keys.J){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_LEFT_RELEASED, null);
			}
		}
		
		if(keycode == Keys.L){
			for(CommunicationCom o : listeners){
				o.send(Message.BTN_RIGHT_RELEASED, null);
			}
		}
		
		if(keycode == Keys.D || keycode == Keys.VOLUME_DOWN){
			for(CommunicationCom o : listeners){
				o.send(Message.DEBUG, null);
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
		for(CommunicationCom o : listeners){
			if(screenY < Gdx.graphics.getHeight()/2){
				if(screenX < Gdx.graphics.getWidth()/2){
						o.send(Message.FIRE, null);
				}else{
					o.send(Message.BOB_JUMP_PRESSED, null);
				}			
			}else if(screenX < Gdx.graphics.getWidth()/2){
				o.send(Message.BTN_LEFT_PRESSED, null);
			}else{
				o.send(Message.BTN_RIGHT_PRESSED, null);
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for(CommunicationCom o : listeners){
			if(screenY < Gdx.graphics.getHeight()/2){
				if(screenX < Gdx.graphics.getWidth()/2){
				}else{
					o.send(Message.BOB_JUMP_RELEASED, null);
				}			
			}else if(screenX < Gdx.graphics.getWidth()/2){
				o.send(Message.BTN_LEFT_RELEASED, null);
			}else{
				o.send(Message.BTN_RIGHT_RELEASED, null);
			}
		}
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
