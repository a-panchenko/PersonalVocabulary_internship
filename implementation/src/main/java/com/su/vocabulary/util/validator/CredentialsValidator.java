package com.su.vocabulary.util.validator;

import java.util.regex.Pattern;

/**
 * Util class for user name and password validation. Can be used for user
 * registration or user password update.
 *
 * @author julia.denysova
 * @version 1.0
 * @since 2015-05-16
 */
public class CredentialsValidator {
    
    private static final Pattern SPACES_PATTERN = Pattern.compile("()+");
    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+$");
    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 18;

    private CredentialsValidator() {
    }

    /**
     * Validates username which should be a valid email name (no whitespaces,
     * not empty line, not a null value allowed)
     *
     * @param name
     *            user login
     * @return true when username satisfies requirements, false if it is not
     */
    public static boolean validateUsername(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        if (SPACES_PATTERN.matcher(name).matches()
                || !EMAIL_PATTERN.matcher(name).matches()) {
            return false;
        }
        return true;
    }

    /**
     * Validates user password which should be from 6 to 18 symbols in length
     * (no whitespaces, not a null value or empty line allowed)
     *
     * @param password
     *            user password
     * @return true if password satisfies requirements,false if it does not
     */
    public static boolean validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            return false;
        }

        if (password.matches("( )+")) {
            return false;
        }
        return true;
    }

}
