package ru.itmo.server.src.Exceptions;

public class LimitException extends Exception{
	private String message;
	
	public LimitException(String message) {
		this.message = message;
	}
	
	public String getMessage() { 
		return message;
	}
}
