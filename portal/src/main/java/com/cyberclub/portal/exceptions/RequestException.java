package com.cyberclub.portal.exceptions;

public abstract class RequestException extends RuntimeException {
    protected RequestException(String message){
        super(message);
    }
}
