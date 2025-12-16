package com.cyberclub.challenge.context;

public final class UserContext {  
    
    private static final ThreadLocal<String> CURRENT_USER_ID = new ThreadLocal<>();

    private UserContext(){};

    public static void setUserId(String userId) {
        CURRENT_USER_ID.set(userId);
    }

    public static String getUserId(){
        String userId = CURRENT_USER_ID.get();
        if(userId == null){
            throw new IllegalStateException("no user in context");
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
