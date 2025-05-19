package com.example.englishfun.ui.login;

public class UserInfo {
    private Long userId;
    private static UserInfo instance;
    private UserInfo(Long userId) {
        this.userId = userId;
    }
    public static UserInfo getInstance(Long userId){
        if(instance == null){
            return new UserInfo(userId);
        }
        return instance;
    }

    public Long getUserId() {
        return userId;
    }
}
