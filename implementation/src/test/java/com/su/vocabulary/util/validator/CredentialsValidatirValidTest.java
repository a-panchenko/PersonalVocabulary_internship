package com.su.vocabulary.util.validator;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CredentialsValidatirValidTest {
    private String email;
    private String password;

    public CredentialsValidatirValidTest(String userEmail, String userPassword) {
        email = userEmail;
        password = userPassword;
    }

    @Parameterized.Parameters
    public static Collection credentials() {
        return Arrays.asList(new Object[][] {
                { "valid@emailcom", "123456" },
                { "valid.email@gmail.com", "wqrergtgdghhjjjkuh" },
                { "valid_email@yahoo.com", "QDFRGKLYPONLK" },
                { "valid-email@gmail.com", "15434TTkjhib" },
                { "_valid-email.123@host.net", "!@#$%^&*()" },
                { "VALID-EMAIL.2000@host.afdafgfg", "';:qdrkvo!@#RT" },
                });
    }

    @Test
    public void testEmail() {
        boolean validEmail = CredentialsValidator.validateUsername(email);
        assertTrue(validEmail);

    }

    @Test
    public void testPassword() {
        boolean validPassword = CredentialsValidator.validatePassword(password);
        assertTrue(validPassword);
    }

}
