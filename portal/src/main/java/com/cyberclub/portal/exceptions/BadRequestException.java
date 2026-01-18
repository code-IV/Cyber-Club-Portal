package com.cyberclub.portal.exceptions;

public class BadRequestException extends RequestException {
    
    public BadRequestException(String message){
        super(message);
    }
}
