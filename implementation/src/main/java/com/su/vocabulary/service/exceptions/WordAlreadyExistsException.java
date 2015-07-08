package com.su.vocabulary.service.exceptions;

public class WordAlreadyExistsException extends Exception {
    
    private static final long serialVersionUID = -8271630403802704104L;

    public WordAlreadyExistsException(String message) {
        super(message);
    }
}
