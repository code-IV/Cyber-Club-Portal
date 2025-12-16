package com.cyberclub.portal.context;

public final class TenantContext {

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    private TenantContext(){}

    public static void set(String tenantId){
        currentTenant.set(tenantId);
    }

    public static boolean isSet(){
        return currentTenant.get() != null;
    }

    public static String get(){
        String tenantId = currentTenant.get();
        
        if(tenantId == null){
            throw new IllegalStateException("Tenant ID is not set in the current context.");
        }
        return tenantId ;
    }

    public static void clear(){
        currentTenant.remove();
    }
}
