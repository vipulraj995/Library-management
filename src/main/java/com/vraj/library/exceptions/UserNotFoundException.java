package com.vraj.library.exceptions;


public class UserNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -429088221455050276L;
	private static final String excMessage = "Invalid Username/password";
	public UserNotFoundException() {
		super(excMessage);
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}
}
