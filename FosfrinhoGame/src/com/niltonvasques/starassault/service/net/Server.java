package com.niltonvasques.starassault.service.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.badlogic.gdx.Gdx;

public class Server extends Host{
	
	private static final String TAG = "[Provider] ";
	
	private ServerSocket providerSocket;
  
	@Override
	public Socket openSocket() throws IOException {
		Gdx.app.log(TAG, "Searching client...");
		providerSocket = new ServerSocket(2004, 10);
		return providerSocket.accept();
	}

	@Override
	public void closeSocket() throws IOException {
		providerSocket.close();
	}
	
    public static void main(String args[])
    {
    	System.out.println(">>> SERVER <<<");
        final Server server = new Server();
        
        Thread serverThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
		            server.run();
		        }
			}
		});
        
        serverThread.start();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while(true){
        	try {
				String str = in.readLine();
				server.send(new Message(str));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        
    }
}