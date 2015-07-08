package com.su.vocabulary.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.su.vocabulary.domain.User;
import com.su.vocabulary.service.exceptions.UserAlreadyExistsException;
import com.su.vocabulary.service.exceptions.UserNotFoundException;
/**
 * Interface which describes methods to manipulate with User's data 
 * @author alexandr.panchenko
 * @version 1.0
 * @since 2015-05-13
 */
public interface UserService extends UserDetailsService {
	/**
	 * Insert user into storage.
	 * @param user
	 *           User instance to insert into storage	
	 * @return user which was created with it's id, email and encoded password 
	 * @throws UserAlreadyExistException
	 *           if user with such email already exist
	 */
	User createUser(String email, String password) throws UserAlreadyExistsException;
	
	/**
	 * Returns user with specified id
	 * @param userId
	 *            unique user identificator of the user in storage;
	 *            cannot be less than 1
	 * @return User instance with specified id
	 * @throws UserNotFoundException
	 *            if user with such id doesn't exist
	 */
	User getUser(int userId) throws UserNotFoundException;
	
	/**
	 * Deletes user with specified id
	 * @param userId
	 *            unique user identificator of the user in storage;
	 *            cannot be less than 1
	 * @throws UserNotFoundException
	 *            if user with such id doesn't exist
	 */
	void deleteUser(int userId) throws UserNotFoundException;
}
