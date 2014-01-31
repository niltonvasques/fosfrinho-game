package com.niltonvasques.starassault.service.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Json;

public abstract class Host {
	
	private static final String TAG = "[Host] ";
	
	public interface OnReceive{
    	public void onReceive(Message msg);
    }
	
	private List<Message> pendingMessages = new ArrayList<Message>(); 
	private Json gson = new Json();
    private Socket connection = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Message message;
    
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
            
            sendMessage(new Message("Connection successful"));
            
            //4. The two parts communicate via the input and output streams
            do{
                try{                	
                	
                	String readMessage = (String)in.readObject();
                	message = gson.fromJson(Message.class,readMessage);
                	
                    if(!message.isEmpty()){
//                    	System.out.println(TAG+ "client>" + message.getContent());
                    	if(onReceive != null) onReceive.onReceive(message);
                    }
                    
                    if (message.equals("bye")){
                        sendMessage(new Message("bye"));
                    }
                	
                    synchronized (pendingMessages) {
	                	if(!pendingMessages.isEmpty()){
	                		for (Message msg : pendingMessages) {
	                			sendMessage(msg);
	                		}
	                		pendingMessages.clear();
	                	}else{
	                		sendMessage(new Message());
	                	}
                    }
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }while(message.isEmpty() || !message.getContent().equals("bye"));
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
    
    public abstract void closeSocket() throws IOException;
    
    private void sendMessage(Message msg)
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
    
    public void send(Message msg){
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
