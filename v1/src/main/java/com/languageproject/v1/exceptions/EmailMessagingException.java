package com.languageproject.v1.exceptions;

public class EmailMessagingException extends RuntimeException{

    public EmailMessagingException() {
        super();
    }

    public EmailMessagingException(String message) {
        super(message);
    }
    
    public EmailMessagingException(String message, Throwable e) {
        super(message, e);
    }    
    
}
