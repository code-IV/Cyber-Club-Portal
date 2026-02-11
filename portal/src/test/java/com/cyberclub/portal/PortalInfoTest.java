package com.cyberclub.portal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cyberclub.portal.security.ClientAuth;
import com.cyberclub.portal.dtos.AuthResult;
import com.cyberclub.portal.exceptions.NotFoundException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@AutoConfigureMockMvc
public class PortalInfoTest extends BaseIntegrationTest {

    private @Value("${INTERNAL_GATEWAY_SECRET}") String secret;
    
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
                .header("X-Internal-Auth", secret)
                .header("X-User-Id", "11111111-1111-1111-1111-111111111111")
        )
        .andExpect(status().isOk());
    }

    @Test
    void req_User_Info() throws Exception{ 
        AuthResult user = new AuthResult(true, "USER");
        when(auth.checkMembership(any())).thenReturn(user);    

        mockmvc.perform(
            get("/portal/info")
                .header("X-Internal-Auth", secret)
                .header("X-User-Id", "11111111-1111-1111-1111-111111111111")
        )
        .andExpect(status().isOk());
    }

    @Test
    void unauthorized_User_Info() throws Exception{ 
        AuthResult user = new AuthResult(true, "");
        when(auth.checkMembership(any())).thenReturn(user);    

        mockmvc.perform(
            get("/portal/info")
                .header("X-Internal-Auth", secret)
                .header("X-User-Id", "11111111-1111-1111-1111-111111111111")
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
                .header("X-Internal-Auth", secret)
                .header("X-User-Id", "11111111-1111-1111-1111-111111111111")
        )
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }
}
