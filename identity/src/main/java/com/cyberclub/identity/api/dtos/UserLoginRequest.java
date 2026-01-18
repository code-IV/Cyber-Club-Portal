package com.cyberclub.identity.api.dtos;

public record UserLoginRequest(
    String email,
    String password
) {
    @Override
    public String toString(){
        return "UserLoginRequest[email=" + email + ", password=*****]";
    }
}
