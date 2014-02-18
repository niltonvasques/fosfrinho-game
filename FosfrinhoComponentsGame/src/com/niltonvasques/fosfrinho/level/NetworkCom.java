package com.niltonvasques.fosfrinho.level;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Json;
import com.niltonvasques.fosfrinho.components.Component;
import com.niltonvasques.fosfrinho.components.ContainerCom;
import com.niltonvasques.fosfrinho.components.comm.Message;
import com.niltonvasques.fosfrinho.gameobject.Action;
import com.niltonvasques.fosfrinho.gameobject.GameObject;
import com.niltonvasques.fosfrinho.gameobject.GameObjectFactory;
import com.niltonvasques.fosfrinho.gameobject.Property;
import com.niltonvasques.fosfrinho.util.net.HostPacket;
import com.niltonvasques.fosfrinho.util.net.HostPacket.Type;
import com.niltonvasques.fosfrinho.util.net.TransferProtocol;
import com.niltonvasques.fosfrinho.util.net.TransferProtocol.OnReceive;
import com.niltonvasques.fosfrinho.util.net.udp.UDPTransfer;

public class NetworkCom implements Component{
	private static final String TAG = "[NetworkCom]";

	private static float NOTIFY_DELAY_ANDROID = 0.1f;
	private static float NOTIFY_DELAY_DESKTOP = 0.1f;

	private TransferProtocol protocol;
	private Json json;

	private Level level;

	private float notifyStateTime = 0f;


	public NetworkCom(Level level) {

		protocol = UDPTransfer.getInstance();
		protocol.setOnReceive(receiver);

		json = new Json();
		this.level = level;

		level.subscribeEvent(Message.DISPOSE, this);
		level.subscribeEvent(Message.FIRE, this);
	}

	@Override
	public GameObject getGameObject() {
		return null;
	}

	@Override
	public void update(ContainerCom o, float delta){

		float delay = Gdx.app.getType() == ApplicationType.Desktop ? NOTIFY_DELAY_DESKTOP : NOTIFY_DELAY_ANDROID;
		
		if(notifyStateTime >= delay ){
			String jsonStr = json.toJson(level.getBob().getProperties());
			protocol.send(new HostPacket(jsonStr));
			notifyStateTime = 0f;
		}
		notifyStateTime += delta;

	}

	@Override
	public void receive(Message m, Object... data) {
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

		case FIRE:
			if(data != null){
				String jsonStr = json.toJson(data);
				UDPTransfer p = (UDPTransfer)protocol;
				HostPacket packet = new HostPacket(jsonStr);
				packet.setType(Type.SHOOT);
				p.sendReliable(packet);
				Gdx.app.debug(TAG, "FIRE_NETWORK: "+jsonStr);
			}
			break;
		}
	}

	private OnReceive receiver = new OnReceive() {

		@Override
		public void onReceive(HostPacket msg) {
			//			Gdx.app.log(TAG, msg.getContent());

			switch(msg.getType()){
			case BOB:
				Map<String, Property> properties = json.fromJson(HashMap.class, msg.getContent());

				if(properties != null && properties.containsKey("BOUNDS")){
					Rectangle rect = (Rectangle) properties.get("BOUNDS").value;
					if( level.getNetworkBob() == null ){
						level.addGameObject(GameObjectFactory.createBobNetworkGameObject(rect.x, rect.y));
					}
					level.getNetworkBob().getBounds().x = rect.x;
					level.getNetworkBob().getBounds().y = rect.y;
					level.getNetworkBob().getProperties().get("FACING_LEFT").value = properties.get("FACING_LEFT").value;
				}
				break;
				
			case SHOOT:
				Object[] data = json.fromJson(Object[].class, msg.getContent());
				Action act = new Action(level.getNetworkBob(), Action.Type.NETWORK_SHOOT, data);
				level.getNetworkBob().registerPendingAction(act);
				break;
				
			case GOODBYE:
				Action act2 = new Action(level.getNetworkBob(), Action.Type.END_ROUND);
				level.getNetworkBob().registerPendingAction(act2);
				break;
			}
		}
	};
}
