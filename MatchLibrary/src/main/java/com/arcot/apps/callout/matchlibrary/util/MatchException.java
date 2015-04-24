package com.arcot.apps.callout.matchlibrary.util;

public class MatchException extends Exception {
	
	private static final long serialVersionUID = -1721559814837472626L;
	
	String message = null;
	String description = null;
	
	public MatchException(String message, String description) {
		this.message = message;
		this.description = description;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
