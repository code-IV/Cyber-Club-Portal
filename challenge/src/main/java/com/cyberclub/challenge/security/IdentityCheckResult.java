package com.cyberclub.challenge.security;

public class IdentityCheckResult{

    public boolean allowed;
    public String role;

    public IdentityCheckResult(){}

    public IdentityCheckResult(boolean allowed, String role){
        this.allowed = allowed;
        this.role = role;
    }
}