package com.cyberclub.challenge.tenancy;

import jakarta.servlet.http.HttpServletRequest;

public class TenantResolver {

    private String sanitizeTenant(String tenant){
        if(tenant == null || tenant.isBlank()){
            return null;
        }
        // Example: only allow alphanumeric and hyphens
        if(!tenant.matches("^[a-zA-Z0-9-]+$")){
            throw new IllegalStateException("Invalid tenant identifier: " + tenant);
        }
        return tenant.toLowerCase();

    }
    
    public String resolveTenant(HttpServletRequest request){
        String tenant = extractDomain(request);
        if(tenant != null){
            return sanitizeTenant(tenant);
        }

        tenant = extractHeader(request);
        if(tenant != null){
            return sanitizeTenant(tenant);
        }
        
        throw new IllegalStateException("Cannot resolve tenant from request");
    }

    private String extractDomain(HttpServletRequest request){
        String host = request.getHeader("Host");
        if(host == null || host.isBlank()){
            return null;
        }
        String domain = host.split(":")[0];

        return extractSubdomain(domain);
    }

    private String extractSubdomain(String domain){
        if("localhost".equalsIgnoreCase(domain)){
            return null;
        }

        if(domain.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")){
            return null;
        }

        String[] parts = domain.split("\\.");

        if(parts.length < 3){
            throw new IllegalStateException("Domain does not contain subdomain: " + domain);
        }

        return parts[1];
    }

    private String extractHeader(HttpServletRequest request){
        String tenant = request.getHeader("X-Tenant-ID");
        if(tenant == null || tenant.isBlank()){
            return null;
        }
        return tenant;
    }
}