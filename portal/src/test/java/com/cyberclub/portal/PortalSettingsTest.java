package com.cyberclub.portal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyberclub.portal.dtos.AuthResult;
import com.cyberclub.portal.exceptions.ForbiddenException;
import com.cyberclub.portal.security.ClientAuth;
import com.cyberclub.portal.config.ContextConfigTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = ContextConfigTest.class)
@AutoConfigureMockMvc
public class PortalSettingsTest {

    
    @Autowired
    private MockMvc mockmvc;

    @MockitoBean
    private ClientAuth auth;


    @Test
    void admin_canUpdate_settings() throws Exception{ 
        AuthResult user = new AuthResult(true, "ADMIN");

        when(auth.checkMembership(any())).thenReturn(user);
            
        mockmvc.perform(
            post("/setting")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "theme": "blue",
                            "notifications_enabled": true,
                            "language_code": "am"
                        }
                        """)
        )
        .andExpect(status().isOk());
    }

    @Test
    void user_canNotUpdate_settings() throws Exception{ 
        AuthResult user = new AuthResult(true, "USER");

        when(auth.checkMembership(any())).thenReturn(user);
            
        mockmvc.perform(
            post("/setting")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "theme": "blue",
                            "notifications_enabled": true,
                            "language_code": "am"
                        }
                        """)
        )
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error").value("FORBIDDEN"))
        .andExpect(jsonPath("$.message").value("Unauthorized Access"));
    }

    @Test
    void req_forbidden_settings() throws Exception{ 
        when(auth.checkMembership(any())).thenThrow(new ForbiddenException("Admin access required"));
            
        mockmvc.perform(
            post("/setting")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "theme": "blue",
                            "notifications_enabled": true,
                            "language_code": "am"
                        }
                        """)
        )
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error").value("FORBIDDEN"))
        .andExpect(jsonPath("$.message").value("Admin access required"));
    }

    @Test
    void req_Unavailable_settings() throws Exception{ 
        when(auth.checkMembership(any())).thenThrow(new RuntimeException("Service UnAvailable"));
            
        mockmvc.perform(
            post("/setting")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "theme": "blue",
                            "notifications_enabled": true,
                            "language_code": "am"
                        }
                        """)
        )
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"))
        .andExpect(jsonPath("$.message").value("internal server error"));
    }    
}
