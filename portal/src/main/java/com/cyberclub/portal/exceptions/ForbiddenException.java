package com.cyberclub.portal.exceptions;

public class ForbiddenException extends RequestException{

    public ForbiddenException(String message) {
        super(message);
    }
}