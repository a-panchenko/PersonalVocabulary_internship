package com.su.vocabulary.controller;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.web.servlet.ModelAndView;

public class UserControllerDbExceptionTest {
    private static UserController controller;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        controller = new UserController();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        controller = null;
    }

    @Test
    public void handleDbExceptionViewTest() {
        ModelAndView mv = controller
                .handleDbException(new DataAccessResourceFailureException(
                        "DataAccessException"));
        String viewName = mv.getViewName();
        assertEquals("signup_error_page", viewName);
    }

    @Test
    public void handleDbExceptionAttributeTest() {
        ModelAndView mv = controller
                .handleDbException(new DataAccessResourceFailureException(
                        "DataAccessException"));
        boolean hasAttr = mv.getModel().containsKey("DataAccessException");
        assertTrue(hasAttr);
    }

    @Test
    public void handleDbExceptionViewOtherExceptionTest() {
        ModelAndView mv = controller.handleDbException(new Exception(
                "The Exception"));
        String viewName = mv.getViewName();
        assertEquals("signup_error_page", viewName);
    }

    @Test
    public void handleDbExceptionAttrOtherExcpceptionTest() {
        Exception e = new Exception("The Exception");
        ModelAndView mv = controller.handleDbException(e);
        String attrValue = (String) mv.getModel().get("errMsg");
        assertEquals(e.getMessage(), attrValue);
    }

}
