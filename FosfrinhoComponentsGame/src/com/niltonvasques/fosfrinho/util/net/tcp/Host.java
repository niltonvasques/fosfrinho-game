package com.niltonvasques.fosfrinho.util.net.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.niltonvasques.fosfrinho.util.net.HostPacket;
import com.niltonvasques.fosfrinho.util.net.TransferProtocol;

public abstract class Host implements TransferProtocol{
	
	private static final String TAG = "[Host] ";
	
	private List<HostPacket> pendingMessages = new ArrayList<HostPacket>(); 
	
	private Json gson = new Json();
    private Socket connection = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private HostPacket message;
    
    private OnReceive onReceive;
    
//    private Type messageType = new TypeToken<Message>(){}.getType();

    public abstract Socket openSocket() throws IOException;
    
    public void run()
    {
        try{        	
        	connection = openSocket();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            
            //3. get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            
            // starting connection
            sendMessage(new HostPacket());
            
            //4. The two parts communicate via the input and output streams
            do{
                try{                	
                	
                	String readMessage = (String)in.readObject();
                	message = gson.fromJson(HostPacket.class,readMessage);
                	
                    if(!message.isControlMsg()){
//                    	System.out.println(TAG+ "client>" + message.getContent());
                    	if(onReceive != null) onReceive.onReceive(message);
                    }
                    
                    if (message.getContent().equals("bye")){
                        sendMessage(new HostPacket("bye", HostPacket.CONTROL_MASK));
                        break;
                    }
                	
                    synchronized (pendingMessages) {
	                	if(!pendingMessages.isEmpty()){
	                		for (HostPacket msg : pendingMessages) {
	                			sendMessage(msg);
	                		}
	                		pendingMessages.clear();
	                	}else{
	                		sendMessage(new HostPacket());
	                	}
                    }
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }while(message.isControlMsg() || !message.getContent().equals("bye"));
        }
        
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
        	
            //4: Closing connection
            try{
                in.close();
                out.close();
                closeSocket();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    
    private void sendMessage(HostPacket msg)
    {
        try{
        	String json = gson.toJson(msg);
            out.writeObject(json);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    public void send(HostPacket msg){
    	synchronized (pendingMessages) {
    		pendingMessages.add(msg);
		}
    }
    
    public void asyncStart(){
    	
    	Thread clientThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Host.this.run();
			}
		});
        
        clientThread.start();
    }

	public void setOnReceive(OnReceive onReceive) {
		this.onReceive = onReceive;
	}
    
    
}
