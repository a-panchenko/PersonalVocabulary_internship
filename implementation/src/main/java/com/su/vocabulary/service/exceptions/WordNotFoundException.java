package com.su.vocabulary.service.exceptions;

public class WordNotFoundException extends Exception {

    private static final long serialVersionUID = -276344275308267108L;

    public WordNotFoundException(String message) {
        super(message);
    }

}
