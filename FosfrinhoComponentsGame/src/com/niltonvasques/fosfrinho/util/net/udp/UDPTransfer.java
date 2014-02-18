package com.niltonvasques.fosfrinho.util.net.udp;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.niltonvasques.fosfrinho.util.net.HostPacket;
import com.niltonvasques.fosfrinho.util.net.TransferProtocol;

public class UDPTransfer implements TransferProtocol{
	private static final String TAG = "[UDPSocket]";

	/**
	 * 
	 * TRANSFER PACKET FORMAT
	 * 
	 * | SEQUENCE_NUMBER 16 bits  | ACKNOWLEDGE_NUMBER 16 bits |  LENGHT 32 bits                      |
	 * |  FLAGS 8 bits |      DATA         ..............                                             |
	 * 
	 */
	private static final int INDEX_SEQUENCE_NUMBER 						= 0;
	private static final int INDEX_ACKNOWLEDGE_NUMBER					= 16/8;
	private static final int INDEX_LENGHT								= 32/8;
	private static final int INDEX_FLAGS								= 64/8;
	private static final int INDEX_DATA									= 72/8;

	private static final int HEADER_LENGHT 								= 72;


	public static final int FLAG_EMPTY_MASK								= 0; //00000000
	public static final int FLAG_ACK_MASK								= 1; //00000001
	public static final int FLAG_RELIABLE_MASK							= 2; //00000010
	public static final int FLAG_HANDSHAKE_MASK							= 4; //00000100
	public static final int FLAG_SERVER_BCAST_MASK						= 8; //00001000

	public static final int DEFAULT_CLIENT_PORT = 5555;
	public static final int DEFAULT_SERVER_PORT = 5557;

	public static final float TIMEOUT_DEFAULT = 5f;


	private InetAddress clientAddress;
	private Object clientLock = new Object();
	private DatagramSocket socket;
	private OnReceive receiver;


	private Boolean closeSocket = false;
	private Gson json = new Gson();
	private Thread thread;

	private float timeout = 0;
	private boolean server = false;

	private int nextSequenceNumber = 0;

	private Map<Integer, byte[]> reliablePackets = new TreeMap<Integer, byte[]>();

	private static UDPTransfer instance;

	private UDPTransfer() {
	}

	public static UDPTransfer getInstance(){
		if(instance ==  null){
			instance = new UDPTransfer();
		}
		return instance;
	}

	public void startClient() {
		server = false;
		try {
			socket = new DatagramSocket(DEFAULT_CLIENT_PORT);

			timeout = TIMEOUT_DEFAULT;
			asyncRun();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void startServer() {
		server = true;
		notifyServerInBroadcast();
	}

	public void notifyServerInBroadcast(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

					while(true){
						try {
							if(socket == null){
								socket = new DatagramSocket(DEFAULT_SERVER_PORT);
								socket.setBroadcast(true);
								asyncRun();
							}
							
							synchronized (clientLock) {
								if(clientAddress != null) break;
							}
							
							InetAddress address = InetAddress.getByName("192.168.0.255");

							byte[] data = new byte[HEADER_LENGHT/8];

							mountRawPacket(data, FLAG_SERVER_BCAST_MASK);

							send(address,socket,data);

							Thread.sleep(4000);
						} catch (SocketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		});

		thread.start();
	}

	private void asyncRun() {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				UDPTransfer.this.run();
			}
		});

		thread.start();
	}

	public void run(){
		try {
			byte[] receiveData = new byte[1024];
			//			byte[] sendData = new byte[1024];

			while(!closeSocket)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

				Gdx.app.log(TAG, (server ? "Server: ":"Client: ")+"WAITING FOR A PACKET!!!");

				socket.receive(receivePacket);

				Gdx.app.log(TAG,"RECEIVED: PACKET = |"+extractSequenceNumber(receiveData)+" | "+extractAcknowledgeNumber(receiveData)+
						" | "+extractLenght(receiveData)+" | "+extractFlags(receiveData)+" | to addr="+receivePacket.getAddress());

				int flags = extractFlags(receiveData);

				synchronized (clientLock) {
					
					if(((flags & FLAG_SERVER_BCAST_MASK) == FLAG_SERVER_BCAST_MASK) &&
							clientAddress == null){
						
						byte[] data = new byte[HEADER_LENGHT/8];

						mountRawPacket(data, FLAG_RELIABLE_MASK | FLAG_HANDSHAKE_MASK);

						send(receivePacket.getAddress(),socket,data);
						
						continue;
						
					}
					
					/*
					 * If the packet to be contains an ack, we need remove it from the reliable packets sended.
					 * This way we know that this packet was sucess delivered.
					 */
					if((flags & FLAG_ACK_MASK) == FLAG_ACK_MASK){ 

						/*
						 * If the ack packet contains an handshake, we know that this handshake is an
						 * handshake accepted to the server, and we need store the address, for start a connection.
						 */
						if(((flags & FLAG_HANDSHAKE_MASK) == FLAG_HANDSHAKE_MASK) && clientAddress == null){
							clientAddress = receivePacket.getAddress();
							if(receiver != null) receiver.onReceive(new HostPacket());
						}
						reliablePackets.remove(extractAcknowledgeNumber(receiveData));
					}

					/* Checking for a reliable packet, if this packet has a reliable mask,
					 * we need send a ack answer for the client. 
					 */
					if((flags & FLAG_RELIABLE_MASK) == FLAG_RELIABLE_MASK){
						int responseFlags = FLAG_EMPTY_MASK;

						/* If received an handshake packet, and do not have a client connected
						 * response with an ack handshake to accept the connect. Otherwise,
						 * just response the ack packet without accept the request for a connection. 
						 */
						if(((flags & FLAG_HANDSHAKE_MASK) == FLAG_HANDSHAKE_MASK) && clientAddress == null){
							clientAddress = receivePacket.getAddress();
							responseFlags |= FLAG_HANDSHAKE_MASK;
						}
						//Sending ACK...
						byte[] sendData = new byte[HEADER_LENGHT/8];
						mountAckPacket(sendData, extractSequenceNumber(receiveData), responseFlags);
						send(receivePacket.getAddress(), socket, sendData);
					}

				}

				if(extractLenght(receiveData) > 0){
					String data = extractData(receiveData);

					try{
						Type type = new TypeToken<HostPacket>(){}.getType();
						HostPacket message = json.fromJson(data, type);
						if(!message.isControlMsg()){
							if(receiver != null) receiver.onReceive(message);
						}
					}catch (JsonSyntaxException e) {
						e.printStackTrace();
					}

				}


			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(HostPacket msg, InetAddress addr, DatagramSocket socket, int flags) {
		try {
			String data = json.toJson(msg);

			byte[] sendData = new byte[HEADER_LENGHT + data.getBytes().length];

			mountPacket(sendData, data, data.getBytes().length, flags);

			Gdx.app.log(TAG,"sending: PACKET = | "+extractLenght(sendData)+" | "+extractData(sendData)+" | to addr="+addr);

			send(addr, socket, sendData);

		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	private void send(InetAddress addr, DatagramSocket socket, byte[] sendData)
			throws IOException {
		if(addr.getAddress() == null) Gdx.app.log(TAG,"UNRESOLVED ADDRESS");

		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, server ? DEFAULT_CLIENT_PORT : DEFAULT_SERVER_PORT);

		socket.send(sendPacket);

		Gdx.app.log(TAG,"SENDED: PACKET = |"+extractSequenceNumber(sendData)+" | "+extractAcknowledgeNumber(sendData)+
				" | "+extractLenght(sendData)+" | "+extractFlags(sendData)+" | to addr="+addr);

		if((extractFlags(sendData) & FLAG_RELIABLE_MASK) == FLAG_RELIABLE_MASK){
			reliablePackets.put(extractSequenceNumber(sendData), sendData);
		}
	}

	@Override
	public void send(HostPacket msg) {
		if(clientAddress != null){
			send(msg, clientAddress, socket, 0);
		}
	}

	public void sendReliable(HostPacket msg) {
		if(clientAddress != null){
			send(msg, clientAddress, socket, FLAG_RELIABLE_MASK);
		}
	}

	private void mountPacket(byte[] sendData, String data, int lenght, int flags) {
		setSequenceNumber(sendData);
		setLenght(sendData, lenght);
		setData(sendData, data);
		setFlags(sendData, flags);
	}

	private void mountRawPacket(byte[] sendData, int flags) {
		setSequenceNumber(sendData);
		setFlags(sendData, flags);
	}

	private void mountAckPacket(byte[] sendData, int ack, int flags){
		setSequenceNumber(sendData);
		setLenght(sendData, 0);
		setAck(sendData, ack);
		setFlags(sendData, flags | FLAG_ACK_MASK);
	}

	private void setSequenceNumber(byte[] arr){

		arr[INDEX_SEQUENCE_NUMBER+0] = (byte) ((nextSequenceNumber >> 8) & 0xFF);
		arr[INDEX_SEQUENCE_NUMBER+1] = (byte) ((nextSequenceNumber) & 0xFF);

		nextSequenceNumber++;
		if(nextSequenceNumber > 0xFFFF) nextSequenceNumber = 0;

	}

	private void setAck(byte[] arr, int ack){

		arr[INDEX_ACKNOWLEDGE_NUMBER+0] = (byte) ((ack >> 8) & 0xFF);
		arr[INDEX_ACKNOWLEDGE_NUMBER+1] = (byte) ((ack) & 0xFF);

	}

	private void setFlags(byte[] arr, int flags){

		arr[INDEX_FLAGS] = (byte) ((flags) & 0xFF);

	}

	private void setLenght(byte[] arr, int data){
		arr[INDEX_LENGHT+0] = (byte) ((data >> 24) & 0xFF);
		arr[INDEX_LENGHT+1] = (byte) ((data >> 16) & 0xFF);
		arr[INDEX_LENGHT+2] = (byte) ((data >> 8) & 0xFF);
		arr[INDEX_LENGHT+3] = (byte) (data & 0xFF);
	}

	private void setData(byte[] arr, String data){
		for(int i = 0; i < data.getBytes().length; i++){
			arr[i+INDEX_DATA] = data.getBytes()[i];
		}
	}

	private int extractLenght(byte[] arr){
		int lenght = 0;
		lenght |= (( ((int)arr[INDEX_LENGHT+0]) << 24) & 0XFF000000 );
		lenght |= (( ((int)arr[INDEX_LENGHT+1]) << 16) & 0xFF0000 );
		lenght |= (( ((int)arr[INDEX_LENGHT+2]) << 8) & 0xFF00 );
		lenght |= ( ((int)arr[INDEX_LENGHT+3]) & 0xFF );

		return lenght;
	}

	private int extractSequenceNumber(byte[] arr){
		int sequence = 0;
		sequence |= (((int)arr[INDEX_SEQUENCE_NUMBER+0]) << 8);
		sequence |= (((int)arr[INDEX_SEQUENCE_NUMBER+1]));

		return sequence;
	}

	private int extractAcknowledgeNumber(byte[] arr){
		int ackNum = 0;

		ackNum |= (((int)arr[INDEX_ACKNOWLEDGE_NUMBER+0]) << 8);
		ackNum |= (((int)arr[INDEX_ACKNOWLEDGE_NUMBER+1]));

		return ackNum;
	}

	private int extractFlags(byte[] arr){
		int flags = 0;

		flags |= (((int)arr[INDEX_FLAGS]));

		return flags;
	}

	private String extractData(byte[] arr){
		int lenght = extractLenght(arr);

		byte[] bData = new byte[lenght];

		for(int i = 0; i < lenght; i++){
			bData[i] = arr[INDEX_DATA+i];
		}

		String data = new String(bData);

		return data;
	}

	public void update(float delta){
		timeout -= delta;

		if(timeout < 0){
			for (byte[] packet : reliablePackets.values()) {
				try {
					send(clientAddress, socket, packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			timeout = TIMEOUT_DEFAULT;
		}
	}

	@Override
	public void setOnReceive(OnReceive onReceive) {
		receiver = onReceive;
	}

	@Override
	public void closeSocket() throws IOException {
		synchronized (closeSocket) {
			closeSocket = true;
			thread.interrupt();
			socket.close();
		}
	}
}
