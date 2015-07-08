package com.su.vocabulary.domain;

/**
 * Immutable class for storing user id, email and password.
 *
 * @author julia.denysova
 * @version 1.0
 * @since 2015-05-12
 */
public class User {
    /**
     * Numeric user identifier in storage.
     */
    private final Integer id;

    /** String email name that user uses as his login. */
    private final String email;

    /** String password representation used for login by user. */
    private final String password;

    /**
     * Constructs immutable user object.
     * @param userId
     *            unique user identifier of the user in storage; can be null
     *            if user details are not added to the application storage yet
     * @param userEmail
     *            String email name used for user login
     * @param userPass
     *            String representation of encrypted password used for user
     *            login
     */
    public User(Integer userId, String userEmail, String userPass) {
        id = userId;
        email = userEmail;
        password = userPass;
    }

    /**
     * Returns user id number.
     * @return numeric identification of the user in application storage;
     *         returns null if id is not set
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns user email used as his login.
     * @return String name of user email used as application login
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns user password.
     * @return String value for the encrypted user password used to login to the
     *         system
     */
    public String getPassword() {
        return password;
    }

}
