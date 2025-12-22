package com.cyberclub.learn.context;

public class UserContext {
    private static final ThreadLocal<String> CURRENT_USER_ID = new ThreadLocal<>();

    private UserContext(){}

    public static void setUserId(String userId){
        CURRENT_USER_ID.set(userId);
    }

    public static String getUserId(){
        String userId = CURRENT_USER_ID.get();
        if(userId == null){
            throw new IllegalStateException("No User in context");
        }
        return userId;
    }

    public static boolean isSetUserId(){
        return CURRENT_USER_ID.get() != null;
    }

    public static void clear(){
        CURRENT_USER_ID.remove();
    }
    
}
