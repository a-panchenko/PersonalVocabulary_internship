package com.su.vocabulary.controller;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.su.vocabulary.domain.User;
import com.su.vocabulary.service.CustomUserDetails;
import com.su.vocabulary.service.UserRole;
import com.su.vocabulary.service.UserService;
import com.su.vocabulary.service.exceptions.SignoutException;
import com.su.vocabulary.service.exceptions.UserAlreadyExistsException;
import com.su.vocabulary.util.validator.CredentialsValidator;

/**
 * Controller class to process requests for user sign up and user sign out
 * (removing account).
 *
 * @author julia.denysova
 * @version 1.0
 * @since 2015-05-13
 *
 */
@Controller
public class UserController {
    /** Bean instance of UserService */
    @Autowired
    private UserService userService;

    private final static Logger logger = Logger.getLogger(UserController.class);

    public UserController() {
    }

    // used for UserControllerMockitoTest.java
    /* package */UserController(UserService service) {
        this.userService = service;
    }

    /**
     * Creates user from user details received from HTTP
     * 
     * @param email
     *            String value representing user email which will be further
     *            used as user login
     * @param password
     *            String value representing user password that user will use for log in
     *            procedure in the future
     * @return ModelAndView object with the name of returned page and user id as
     *         a model attribute;
     * @throws IllegalArgumentException
     *             in case when email or password parameters are invalid
     * @throws NullPointerException
     *             if user object received from database is null
     * @throws org.springframework.dao.DataAccessException
     *             or exceptions derived from it in case of database issue
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView createUser(@RequestParam("email") String email,
            @RequestParam("password") String password) throws Exception {
        logger.debug("New request to create user: " + email);
        boolean validCredentials = validateCredentials(email, password);
        if (!validCredentials) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        User user = null;            
        try {
            user = userService.createUser(email, password);
        } catch (UserAlreadyExistsException e) {
            logger.error("User creation error.", e);
            throw new IllegalArgumentException("Invalid username or password.",
                    e);
        }

        if (user == null) {
            logger.error("User creation error - null User object.");
            throw new NullPointerException("Registration error: null user object");
        }
        ModelAndView modelView = new ModelAndView("index");
        logger.debug("User " + email + " was created successfully.");
        return modelView;
    }
    
    /**
     * Check whether user authorized or not and redirect to necessary page
     * @return String name of necessary view 
     */
    @RequestMapping(value = "/")
    public String helloMethod() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains(UserRole.ROLE_USER.name())) {
        	return "redirect:/words";
        }
        return "index";
    }

    /**
     * Deletes user on his HTTP request.
     *
     * @param id
     *            user id
     * @return ModelAndView object with the name of returned page;
     * @throws UserNotFoundException
     *             if user with such id doesn't exist
     * @throws org.springframework.dao.DataAccessException
     *             and exceptions derived from it in case of database issue
     */
    @RequestMapping(value = "/delete_user", method = RequestMethod.GET)
    public ModelAndView deleteUser()
            throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        int id = userDetails.getId();
        userService.deleteUser(id);
        User currentUser = null;
        
        try{ 
            currentUser = userService.getUser(id);
        } catch(EmptyResultDataAccessException e) {
            SecurityContextHolder.clearContext();            
        } 
        
        if(currentUser != null) {
            throw new SignoutException("Sign out error!");
        }
        logger.debug("user (id=" + id + ") was deleted successfully");
        return new ModelAndView("index");
    }

    /**
     * Handles all exceptions inherited from DataAccessException class.
     *
     * @param ex
     *            exception thrown in UserController createUser or deleteUser
     *            method
     * @return ModelAndView object with view name and attributes identifying
     *         displayed exception message
     */
    @ExceptionHandler(DataAccessException.class)
    public ModelAndView handleDbException(Exception ex) {
        logger.error("registration or account deletion failed", ex);
        ModelAndView model = new ModelAndView("signup_error_page");
        model.addObject("DataAccessException", "true");
        model.addObject("errMsg", ex.getMessage());
        return model;
    }

    /**
     * Handles all exceptions other than DataAccessException.
     *
     * @param ex
     *            exception thrown in UserController createUser or deleteUser
     *            method
     * @return ModelAndView object with view name and attributes identifying
     *         displayed exception message
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllException(Exception ex) {
        logger.error("registration or account deletion procedure failed", ex);
        ModelAndView model = new ModelAndView("signup_error_page");
        String exceptionName = ex.getClass().getSimpleName();
        model.addObject(exceptionName, "true");
        model.addObject("errMsg", ex.getMessage());
        return model;
    }
    
    private boolean validateCredentials(String email, String password) {
        boolean validEmail = CredentialsValidator.validateUsername(email);
        boolean validPass = CredentialsValidator.validatePassword(password);

        if (!validEmail || !validPass) {
            return false;
        }
        return true;
    }

}
