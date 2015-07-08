package com.su.vocabulary.service.exceptions;

/**
 * Exception is thrown when somebody tries to add User which already exists
 * @author alexandr.panchenko
 *
 */
public class UserAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 9109625421451136270L;

	public UserAlreadyExistsException(String message) {
		super(message);
	}
	
}
