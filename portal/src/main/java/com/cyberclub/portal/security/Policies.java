package com.cyberclub.portal.security;

import com.cyberclub.portal.exceptions.ForbiddenException;

public final class Policies {
    
    public static final AuthPolicy MEMBER =
        user -> {
            if(!"USER".equals(user.role())){
                throw new ForbiddenException("Unauthorized Access");
            }
        };

    public static final AuthPolicy ADMIN =
        user -> {
            if(!"ADMIN".equals(user.role())){
                throw new ForbiddenException("Unauthorized Access");
            }
        };

    private Policies(){}
}
