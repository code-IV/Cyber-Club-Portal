package com.cyberclub.portal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyberclub.portal.dtos.AuthResult;
import com.cyberclub.portal.exceptions.ForbiddenException;
import com.cyberclub.portal.security.ClientAuth;
import com.cyberclub.portal.config.ContextConfigTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@AutoConfigureMockMvc
public class PortalUserTest extends BaseIntegrationTest {

    
    @Autowired
    private MockMvc mockmvc;

    @MockitoBean
    private ClientAuth auth;


    @Test
    void admin_canNOtRead() throws Exception{ 
        AuthResult user = new AuthResult(true, "ADMIN");

        when(auth.checkMembership(any())).thenReturn(user);
            
        mockmvc.perform(
            get("/users")
        )
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error").value("FORBIDDEN"))
        .andExpect(jsonPath("$.message").value("Unauthorized Access"));
    }

    @Test
    void user_isAuthorized_butServiceNOtMocked() throws Exception{ 
        AuthResult user = new AuthResult(true, "USER");
        when(auth.checkMembership(any())).thenReturn(user);
            
        mockmvc.perform(
            get("/users")
        )
        .andExpect(status().is5xxServerError());
    }

    @Test
    void req_forbidden() throws Exception{ 
        when(auth.checkMembership(any())).thenThrow(new ForbiddenException("Admin access required"));
            
        mockmvc.perform(
            get("/users")
        )
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error").value("FORBIDDEN"))
        .andExpect(jsonPath("$.message").value("Admin access required"));
    }

    @Test
    void req_unavailable() throws Exception{ 
        when(auth.checkMembership(any())).thenThrow(new RuntimeException("Service UnAvailable"));
            
        mockmvc.perform(
            get("/users")
        )
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"))
        .andExpect(jsonPath("$.message").value("internal server error"));
    }    
}
