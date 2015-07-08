package com.su.vocabulary.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.web.servlet.ModelAndView;

import com.su.vocabulary.service.exceptions.UserNotFoundException;

@RunWith(Parameterized.class)
public class UserControllerExceptionHandlerTest {
    private Exception exception;
    private UserController controller;

    @Before
    public void setBefore() {
        controller = new UserController();
    }

    public UserControllerExceptionHandlerTest(Exception e) {
        exception = e;
    }

    @Parameterized.Parameters
    public static Collection credentials() {
        return Arrays.asList(new Object[][] {
                { new IllegalArgumentException() },
                { new NullPointerException() },
                { new UserNotFoundException("UserNotFound") },
                { new Exception("The Exception") } });
    }

    @Test
    public void testViewName() {
        ModelAndView mv = controller.handleAllException(exception);
        String viewName = mv.getViewName();
        assertEquals("signup_error_page", viewName);
    }

    @Test
    public void testAttribute() {
        ModelAndView mv = controller.handleAllException(exception);
        String viewName = mv.getViewName();
        String expected = exception.getClass().getSimpleName();
        if (expected == "Exception") {
            String attributeValue = (String) mv.getModel().get("errMsg");
            assertEquals(exception.getMessage(), attributeValue);
        } else {

            boolean hasAttribute = mv.getModel().containsKey(expected);
            assertTrue(hasAttribute);
        }
    }

}
