package com.niltonvasques.fosfrinho.util.net;

public class HostPacket {
	
	public enum Type{
		BOB, SHOOT, GOODBYE
	}
	
	public static final int CONTROL_MASK = 0x01;
	
	private String content = "";
	
	/**
	 * FLAGS
	 * 
	 * 00000001 = CONTROL MSG
	 * 
	 * 00000010 = ... 
	 */
	
	private int flags;
	private Type type = Type.BOB;

	public HostPacket() {
		flags = CONTROL_MASK;
	}
	
	public HostPacket(String content, int flags){
		this.content = content;
		this.flags = flags;
	}
	
	public HostPacket(String content){
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean isControlMsg(){
		if((flags & CONTROL_MASK) == 1){
			return true;
		}
		return false;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	
}
