package com.su.vocabulary.util.validator;

import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CredentialsValidatorInvalidTest {
    private String email;
    private String password;

    public CredentialsValidatorInvalidTest(String userEmail, String userPassword) {
        email = userEmail;
        password = userPassword;
    }

    @Parameterized.Parameters
    public static Collection credentials() {
        return Arrays.asList(new Object[][] { { "", "" },
                { "     ", "           " }, { "invalidemail", "inv" },
                { "invalidemail@", "invalidpasswordddddddddddddd" },
                { "@invalidemail", "           " },
                { "?invalidemail@gmail.com", "           " },
                { "#%$@yahoo.com", "           " },
                { "invalidemail@&&&", "           " },
                { "invalidemail@????", "           " },
                { "invalidemail@***", "           " },
                { "invalidemail@     ", "           " },
                { "      @invalidemail", "           " },
                { "^:@invalidemail", "           " }, 
                { null, null }

        });
    }

    @Test
    public void testEmail() {
        boolean validEmail = CredentialsValidator.validateUsername(email);
        assertFalse(validEmail);
    }

    @Test
    public void testPassword() {
        boolean validPassword = CredentialsValidator.validatePassword(password);
        assertFalse(validPassword);
    }

}
