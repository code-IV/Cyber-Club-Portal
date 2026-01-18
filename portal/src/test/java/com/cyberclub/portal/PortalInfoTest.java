package com.cyberclub.portal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyberclub.portal.security.ClientAuth;
import com.cyberclub.portal.dtos.AuthResult;
import com.cyberclub.portal.exceptions.NotFoundException;
import com.cyberclub.portal.config.ContextConfigTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = ContextConfigTest.class)
@AutoConfigureMockMvc
public class PortalInfoTest {

    
    @Autowired
    private MockMvc mockmvc;

    @MockitoBean
    private ClientAuth auth;

    @Test
    void req_Admin_Info() throws Exception{ 
        AuthResult user = new AuthResult(true, "ADMIN");

        when(auth.checkMembership(any())).thenReturn(user);
            
        mockmvc.perform(
            get("/portal/info")
        )
        .andExpect(status().isOk());
    }

    @Test
    void req_User_Info() throws Exception{ 
        AuthResult user = new AuthResult(true, "USER");
        when(auth.checkMembership(any())).thenReturn(user);    

        mockmvc.perform(
            get("/portal/info")
        )
        .andExpect(status().isOk());
    }

    @Test
    void unauthorized_User_Info() throws Exception{ 
        AuthResult user = new AuthResult(true, "");
        when(auth.checkMembership(any())).thenReturn(user);    

        mockmvc.perform(
            get("/portal/info")
        )
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error").value("FORBIDDEN"))
        .andExpect(jsonPath("$.message").value("Unauthorized Access"));
    }

    @Test
    void notFound_User_Info() throws Exception{ 
        
        when(auth.checkMembership(any())).thenThrow(new NotFoundException("NOT_FOUND"));

        mockmvc.perform(
            get("/portal/info")
        )
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }
}
