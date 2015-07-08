package com.su.vocabulary.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.su.vocabulary.domain.User;
import com.su.vocabulary.service.CustomUserDetails;
import com.su.vocabulary.service.UserService;
import com.su.vocabulary.service.exceptions.UserAlreadyExistsException;
import com.su.vocabulary.util.validator.CredentialsValidator;

@Controller
public class ExternalUserController {
	/** Bean instance of UserService */
    @Autowired
    private UserService userService;
    /**
	 * Object for encoding password
	 */
	@Autowired
	private PasswordEncoder enocoder;

    private final static Logger logger = Logger.getLogger(ExternalUserController.class);

    public ExternalUserController() {
    	
    }
    
    /*package*/ ExternalUserController(UserService service, PasswordEncoder encoder) {
		this.userService = service;
		this.enocoder = encoder;
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
     * @return String message with result of method invocation - 'Ok' or error message
     */
	@RequestMapping(value = "/client/signup", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody String createUser(@RequestParam("email") String email,
            @RequestParam("password") String password) throws Exception {
        logger.debug("New request to create user: " + email);
        User user = null;
        String invalidCredentialsMessage = "Invalid email or password.";
        String databaseErrorMessage = "Registration error";
        if (!checkCredentials(email, password)) {
            return invalidCredentialsMessage;
        }
        try {
            user = userService.createUser(email, password);
        } catch (UserAlreadyExistsException e) {
            logger.error("User creation error.", e);
            return "User with email=" + email + " already exists.";
        } catch(DataAccessException e) {
            logger.error("User creation error.", e);
            return databaseErrorMessage;
        } catch (Exception e) {
            logger.error("User creation error.", e);
            return e.getMessage();
        }

        if (user == null) {
            logger.error("User creation error - null User object.");
            return databaseErrorMessage;
        }
        logger.debug("User " + email + " was created successfully.");
        return "Ok";
    }

    /**
     * Deletes user on his HTTP request.
     *
     * @return String message with result of method invocation - 'Ok' or error message
     */
    @RequestMapping(value = "/client/delete_user", method = RequestMethod.POST,  produces = "text/plain;charset=UTF-8")
    public @ResponseBody String deleteUser() throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        int id = userDetails.getId();
        SecurityContextHolder.clearContext();
        try {
            userService.deleteUser(id);
        } catch(DataAccessException e) {
            logger.error("User deletion error. ", e);
            return "Deletion procedure failed";
        }
        logger.debug("user (id=" + id + ") was deleted successfully");
        return "Ok";
    }

    /**
     * Allows user to login in system on his HTTP request.
     *
     * @param email
     *            String value representing user email which is further
     *            used as user login
     * @param password
     *            String value representing user password
     * @return String message with result of method invocation - 'Ok' or error message
     */
    @RequestMapping(value = "/client/login", method = RequestMethod.POST,  produces = "text/plain;charset=UTF-8")
    public @ResponseBody String login(@RequestParam("email") String email,
            @RequestParam("password") String password) throws Exception {
        logger.debug("New request to login: " + email);
        UserDetails user = null;
        String InvalidCredentialsMessage = "Invalid email or password.";
        if (!checkCredentials(email, password)) {
            return InvalidCredentialsMessage;
        }
        try {
        	user = userService.loadUserByUsername(email);
        	if (enocoder.matches(password, user.getPassword()) == false) {
        		return InvalidCredentialsMessage;
        	}
        	
        } catch(UsernameNotFoundException e) {
            logger.error("Login error. ", e);
        	return InvalidCredentialsMessage;
        } catch (DataAccessException e) {
            logger.error("Login error. ", e);
            return "Login error"; 
        } catch (Exception e) {
            logger.error("Login error. ", e);
            return e.getMessage();
        }
        authorize(user, password);
        return "Ok";
    }

    /**
     * Allows user to logout from system on his HTTP request.
     *
     * @return String message with result of method invocation - 'Ok' or error message
     */
    @RequestMapping(value = "/client/logout", method = RequestMethod.POST,  produces = "text/plain;charset=UTF-8")
    public @ResponseBody String logout() throws Exception {
        SecurityContextHolder.clearContext();
        return "Ok";
    }
    
    /*package*/ CustomUserDetails authorize(UserDetails user, String password) {
    	UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, password);        
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
    }

    private boolean checkCredentials(String email, String password) {
        boolean validEmail = false;
        boolean validPass = false;
        validEmail = CredentialsValidator.validateUsername(email);
        validPass = CredentialsValidator.validatePassword(password);
        if (!validEmail || !validPass) {
            return false;
        }
        return true;
    }
   
}
