package com.niltonvasques.fosfrinho.util.net.tcp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.niltonvasques.fosfrinho.util.net.HostPacket;

public class Client extends Host{
	private static final String TAG = "[Requester] ";

	private Socket requestSocket;
	
	@Override
	public Socket openSocket() throws IOException {
		if(requestSocket == null){
			Gdx.app.log(TAG, "Searching server...");
			requestSocket = new Socket("localhost", 2004);
		}
		return requestSocket;
	}

	@Override
	public void closeSocket() throws IOException {
		requestSocket.close();
	}
	
    public static void main(String args[])
    {
    	System.out.println(">>> CLIENT <<<");
    	final Client client = new Client();
        
        Thread clientThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
//				while(true){
					client.run();
//		        }
			}
		});
        
        clientThread.start();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while(true){
        	try {
				String str = in.readLine();
				client.send(new HostPacket(str));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}