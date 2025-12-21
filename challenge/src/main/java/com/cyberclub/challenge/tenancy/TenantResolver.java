package com.cyberclub.challenge.tenancy;

import jakarta.servlet.http.HttpServletRequest;

public class TenantResolver {
    
    public String resolve(HttpServletRequest request){
        String tenant = request.getHeader("X-Tenant-ID");
        if(tenant == null || tenant.isBlank()){
            throw new IllegalStateException("Missing Tenant");
        }
        return tenant;
    }

}