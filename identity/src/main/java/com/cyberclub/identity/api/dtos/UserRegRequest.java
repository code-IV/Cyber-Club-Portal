package com.cyberclub.identity.api.dtos;

public record UserRegRequest(
    String username,
    String email,
    String password
) {
    @Override
    public String toString(){
        return "UserRegRequest[username=" + username + ", email=" + email + ", password=****]";
    }
}
