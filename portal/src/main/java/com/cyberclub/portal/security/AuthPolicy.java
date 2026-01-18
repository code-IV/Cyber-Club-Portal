package com.cyberclub.portal.security;

import com.cyberclub.portal.dtos.AuthResult;
import com.cyberclub.portal.exceptions.ForbiddenException;

public interface AuthPolicy {

    void check(AuthResult user);

    default AuthPolicy and(AuthPolicy other) {
        return user -> {
            this.check(user);
            other.check(user);
        };
    }

    default AuthPolicy or(AuthPolicy other) {
        return user -> {
            try {
                this.check(user);
            } catch (ForbiddenException e) {
                other.check(user);
            }
        };
    }

}