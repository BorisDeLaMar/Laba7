package ru.itmo.server.src.Exceptions;

public class NullException extends Exception{
	private String message;

	public NullException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
