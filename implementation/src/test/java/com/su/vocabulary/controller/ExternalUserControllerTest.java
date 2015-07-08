package com.su.vocabulary.controller;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.su.vocabulary.domain.User;
import com.su.vocabulary.service.CustomUserDetails;
import com.su.vocabulary.service.UserService;
import com.su.vocabulary.service.exceptions.SessionRecognizingException;
import com.su.vocabulary.service.exceptions.UserAlreadyExistsException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ExternalUserControllerTest {
	private ExternalUserController controller;
	private UserService service;
	private PasswordEncoder encoder;
	
	
	@Before
	public void setUP() throws SessionRecognizingException {
		service = Mockito.mock(UserService.class);
		encoder = mock(PasswordEncoder.class);
		controller = Mockito.spy(new ExternalUserController(service, encoder));
	}
	
	@After
	public void tearDown() {
		service = null;
		controller = null;
	}
	
	@Test
	public void signUpTestWithInvalidArgs() throws Exception {
		String result = controller.createUser("invalidMail", "0");
		assertEquals("Invalid email or password.", result);
	}
	
	@Test
	public void signUpTestUserAlreadyExists() throws Exception {
		when(service.createUser("user1@mail.com", "password")).thenThrow(new UserAlreadyExistsException(""));
		String result = controller.createUser("user1@mail.com", "password");
		assertEquals("User with email=user1@mail.com already exists.", result);
	}

	@Test
    public void signUpDbException() throws Exception {
        when(service.createUser("user1@mail.com", "password")).thenThrow(new CannotGetJdbcConnectionException("", new SQLException()));
        String result = controller.createUser("user1@mail.com", "password");
        assertEquals("Registration error", result);
    }
	
	@Test
	public void signUpValidArgs() throws Exception {
		when(service.createUser("user1@mail.com", "password")).thenReturn(new User(10, "user1@mail.com", "password"));
		String result = controller.createUser("user1@mail.com", "password");
		assertEquals("Ok", result);
	}
	
	@Test
	public void logInTestWithInvalidArgs() throws Exception {
		String result = controller.login("invalidMail", "0");
		assertEquals("Invalid email or password.", result);
	}
	
	@Test
	public void logInTestUserDoesnotExist() throws Exception {
		when(service.loadUserByUsername("user1@mail.com")).thenThrow(new UsernameNotFoundException(""));
		String result = controller.login("user1@mail.com", "password");
		assertEquals("Invalid email or password.", result);
	}
	
	@Test
    public void logInDbException() throws Exception {
        when(service.loadUserByUsername("user1@mail.com")).thenThrow(new CannotGetJdbcConnectionException("", new SQLException()));
        String result = controller.login("user1@mail.com", "password");
        assertEquals("Login error", result);
    }
	
	@Test
	public void logInValidArgs() throws Exception {
		when(encoder.matches(anyString(), anyString())).thenReturn(true);
		when(service.loadUserByUsername("user1@mail.com")).thenReturn(mock(CustomUserDetails.class));
		doReturn(null).when(controller).authorize(any(UserDetails.class), anyString());
		String result = controller.login("user1@mail.com", "password");
		assertEquals("Ok", result);
	}
	
	
}
