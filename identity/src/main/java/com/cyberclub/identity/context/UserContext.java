package com.cyberclub.identity.context;

public class UserContext {

    private static final ThreadLocal<String> CURRENT_USER_ID = new ThreadLocal<>();

    private UserContext(){}

    public static void set(String userId){
        CURRENT_USER_ID.set(userId);
    }

    public static String get(){
        String userId = CURRENT_USER_ID.get();
        if(userId == null){
            throw new IllegalStateException("No User in Context");
        }
        return userId;
    }

    public static boolean isSet(){
        return CURRENT_USER_ID.get() != null;
    }
    
    public static void clear(){
        CURRENT_USER_ID.remove();
    }
}
