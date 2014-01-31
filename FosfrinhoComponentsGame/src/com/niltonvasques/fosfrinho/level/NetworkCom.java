package com.niltonvasques.fosfrinho.level;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Json;
import com.niltonvasques.fosfrinho.components.Component;
import com.niltonvasques.fosfrinho.components.ContainerCom;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.GameObjectFactory;
import com.niltonvasques.fosfrinho.util.net.HostPacket;
import com.niltonvasques.fosfrinho.util.net.TransferProtocol;
import com.niltonvasques.fosfrinho.util.net.TransferProtocol.OnReceive;
import com.niltonvasques.fosfrinho.util.net.udp.UDPTransfer;

public class NetworkCom implements Component{
	private static final String TAG = "[NetworkCom]";
	
	private static final float NOTIFY_DELAY = 0.02f;
	
	private TransferProtocol protocol;
	private Json json;
	
	private Level level;
	
	private float notifyStateTime = 0f;
	
	
	public NetworkCom(Level level) {
		
		protocol = UDPTransfer.getInstance();
		
//		protocol = new UDPSocket(true);
		
//		try{
//			protocol = new Client();
//			protocol.openSocket();
//		}catch (Exception e) {
//			Gdx.app.log(TAG, "How an server was not found... assumes me as a server!");
//			protocol = new Server();
//		}
		protocol.setOnReceive(receiver);
//		protocol.asyncStart();
		json = new Json();
		this.level = level;
		
		level.subscribeEvent(Message.DISPOSE, this);
	}

	@Override
	public GameObject getGameObject() {
		return null;
	}

	@Override
	public void update(ContainerCom o, float delta){
		
		if(notifyStateTime >= NOTIFY_DELAY){
			protocol.send(new HostPacket(json.toJson(level.getBob().getBounds())));
			notifyStateTime = 0f;
		}
		notifyStateTime += delta;
		
	}

	@Override
	public void receive(Message m) {
		switch(m){
		case DISPOSE:
			Gdx.app.log(TAG, "DISPOSE");
//			protocol.send(new HostPacket("bye", HostPacket.CONTROL_MASK));
			try {
				protocol.closeSocket();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			break;
		}
	}
	
	private OnReceive receiver = new OnReceive() {
		
		@Override
		public void onReceive(HostPacket msg) {
//			Gdx.app.log(TAG, msg.getContent());
			
			Rectangle rect = json.fromJson(Rectangle.class, msg.getContent());
			if( level.getNetworkBob() == null){
				level.addGameObject(GameObjectFactory.createBobNetworkGameObject(rect.x, rect.y));
			}
			level.getNetworkBob().getBounds().x = rect.x;
			level.getNetworkBob().getBounds().y = rect.y;
		}
	};

}
