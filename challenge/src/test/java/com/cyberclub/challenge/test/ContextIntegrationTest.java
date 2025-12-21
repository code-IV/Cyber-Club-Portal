package com.cyberclub.challenge.test;

import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cyberclub.challenge.config.ContextTest;

@SpringBootTest(classes = ContextTest.class)
@AutoConfigureMockMvc
public class ContextIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void tenantAndUserPopulated() throws Exception {
        // String jwt = jwtWithSub("user-123");
        mockMvc.perform(
            get("/test/context")
                .header("X-User-Id", "Bearer user-123")
                .header("X-Tenant-Id", "challenge")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tenant").value("challenge"))
            .andExpect(jsonPath("$.userId").value("Bearer user-123"));
    }

    @Test
    void missingTenantReturns400() throws Exception {
        // String jwt = jwtWithSub("user-123");

        mockMvc.perform(
                get("/test/context")
                        .header("Host","example.com")
                        .header("X-User-Id", "Bearer user-123")
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void missingJwtStillAllowsTenantButUserContextFailsInController() throws Exception {
        mockMvc.perform(
                get("/test/context")
                        .header("X-Tenant-Id", "challenge")
        )
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error").value("no user in context"));
    }

    private String jwtWithSub(String userId) {
        String header = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString("{\"alg\":\"none\"}".getBytes());

        String payload = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(("{\"sub\":\"" + userId + "\"}").getBytes());

        return header + "." + payload + ".";
    }
    
}
