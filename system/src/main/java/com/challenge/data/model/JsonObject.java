package com.challenge.data.model;

/**
 * Class to represent an object to be put into HTTP Response
 */
public class JsonObject extends IdentityObject {
	
	private String message;
	
	public JsonObject(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
