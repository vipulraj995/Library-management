package com.vraj.library.exceptions;

public class UserAlreadyPresentException extends RuntimeException{

	private static final long serialVersionUID = 6113284845505543269L;
	private static final String excMessage = "User already registered.";
	
	public UserAlreadyPresentException() {
		super(excMessage);
	}
}
