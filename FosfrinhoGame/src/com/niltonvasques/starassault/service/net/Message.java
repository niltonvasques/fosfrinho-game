package com.niltonvasques.starassault.service.net;

public class Message {
	
	private boolean empty;
	private String content = "";

	public Message() {
		empty = true;
	}
	
	public Message(String content){
		this.content = content;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
