package com.niltonvasques.fosfrinho.util.net;

import java.io.IOException;


public interface TransferProtocol {
	
	public interface OnReceive{
    	public void onReceive(HostPacket msg);
    }
	
	public void send(HostPacket msg);
	
	public void setOnReceive(OnReceive onReceive);
	
	public void closeSocket() throws IOException;

}
