package com.cyberclub.portal.services;

import org.springframework.stereotype.Service;

import com.cyberclub.portal.dtos.SettingData;
import com.cyberclub.portal.context.*;
import com.cyberclub.portal.repositories.UserSettingRepo;
import com.cyberclub.portal.security.Policies;

@Service
public class SettingService {
    
    private final AuthService auth;
    private final UserSettingRepo settingRepo;

    public SettingService(AuthService auth, UserSettingRepo settingRepo){
        this.auth = auth;
        this.settingRepo = settingRepo;
    }

    public void setSetting(SettingData req ){
        auth.require(Policies.ADMIN);
        
        settingRepo.setSetting(
            UserContext.get(),
            req.theme(),
            req.notifications_enabled(),
            req.language_code()
        );
    }
}
