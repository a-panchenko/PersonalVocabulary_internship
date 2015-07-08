package com.su.vocabulary.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.su.vocabulary.dao.GenericDao;
import com.su.vocabulary.domain.User;
import com.su.vocabulary.service.exceptions.UserAlreadyExistsException;
import com.su.vocabulary.service.exceptions.UserNotFoundException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)  
public class UserServiceImplTest{
	
	private UserServiceImpl userService;
	private GenericDao<User> userDao;
	private User user;
	
	@Before
	public void setUp() {
		userService = new UserServiceImpl();
		userDao = mock(GenericDao.class);
		userService.setUserDao(userDao);
		user = new User(null, "email@domain.i", "password");
	}
	
	@After
	public void tearDown() {
		userDao = null;
		userService = null;
	}
	
	@Test
	public void createUserTest() throws UserAlreadyExistsException {
		userService.createUser("emai@domain.i", "password");
		verify(userDao).isInstanceExists(any(User.class));		
		verify(userDao).create(any(User.class));		
	}

	
	@Test
	public void deleteUserTest() throws UserNotFoundException {
		int userId = 1;
		when(userDao.getById(userId)).thenReturn(user);
		userService.deleteUser(userId);		
		verify(userDao).delete(userId);
	}
	
	@Test
	public void getUserByIdTest() throws UserNotFoundException {
		int userId = 1;
		when(userDao.getById(userId)).thenReturn(user);
		userService.getUser(userId);
		verify(userDao).getById(userId);
	}
	
	@Test
	public void loadUserByUserNameTest() {
		String value = "email";
		when(userDao.getByValue(value)).thenReturn(user);
		userService.loadUserByUsername(value);
		verify(userDao).getByValue(value);
	}	
	
}
