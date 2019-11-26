package com.challenge.data.model;

/**
 * Class to represent an object with a message
 */
public class MessageObject extends JsonObject {
	
	private String message;
	
	public MessageObject(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
