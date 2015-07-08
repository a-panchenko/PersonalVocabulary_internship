package com.su.vocabulary.service.exceptions;

/**
 * Exception is thrown when somebody tries to get User, which doesn't exist in
 *           storage
 * @author alexandr.panchenko
 *
 */
public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 3990453210178142949L;

	public UserNotFoundException(String message) {
		super(message);
	}

}
