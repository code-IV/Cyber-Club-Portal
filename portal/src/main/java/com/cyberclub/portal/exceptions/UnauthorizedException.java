package com.cyberclub.portal.exceptions;

public class UnauthorizedException extends RequestException {

    public UnauthorizedException(String message){
        super(message);
    }
}
