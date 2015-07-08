package com.su.vocabulary.controller;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.su.vocabulary.controller.UserController;
import com.su.vocabulary.domain.User;
import com.su.vocabulary.service.CustomUserDetails;
import com.su.vocabulary.service.UserService;
import com.su.vocabulary.service.UserServiceImpl;
import com.su.vocabulary.service.exceptions.UserAlreadyExistsException;
import com.su.vocabulary.service.exceptions.UserNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerMockitoTest {

    private static UserService service;
    private static UserController controller;
    private static String deleteSuccessResult;
    private static String createSuccessResult;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        service = mock(UserServiceImpl.class);
        controller = new UserController(service);
        deleteSuccessResult = "index";
        createSuccessResult = "index";
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        service = null;
        controller = null;
        deleteSuccessResult = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserNotUniqueLogin() throws Exception {
        String email = "same@email";
        String password = "validPass";
        when(service.createUser(email, password)).thenThrow(
                new UserAlreadyExistsException("UserAlreadyExistException"));
        controller.createUser(email, password);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserInvalidParametersTest() throws Exception {
        String email = "invalidemail";
        String password = "invalidpass1234567890";
        when(service.createUser(email, password)).thenThrow(
                new IllegalArgumentException());
        controller.createUser(email, password);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserNullInput() throws Exception {
        String email = null;
        String password = null;
        controller.createUser(email, password);
    }

    @Test(expected = DataAccessException.class)
    public void testCreateUserDbException() throws Exception {
        String email = "valid@email";
        String password = "123234";
        when(service.createUser(email, password)).thenThrow(
                new DataAccessResourceFailureException("DataAccessException"));
        controller.createUser(email, password);
    }

    @Test
    public void testCreateUserValidParameters() throws Exception {
        String email = "valid@email";
        String password = "validpass";
        User user = new User(1, email, password);
        when(service.createUser(email, password)).thenReturn(user);
        String result = controller.createUser(email, password).getViewName();
        assertEquals(createSuccessResult, result);
    }

}
