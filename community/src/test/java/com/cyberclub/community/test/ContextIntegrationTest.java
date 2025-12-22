package com.cyberclub.community.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cyberclub.community.config.ContextTest;

@SpringBootTest(classes = ContextTest.class)
@AutoConfigureMockMvc
public class ContextIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    void tenantAndUserPopulated()throws Exception{
        mockMvc.perform(
            get("/test/context")
                .header("X-User-Id", "user-123")
                .header("X-Tenant-Id", "community")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tenant").value("community"))
        .andExpect(jsonPath("$.userId").value("user-123"));

    }

    @Test
    void missingTenantReturns400() throws Exception{
        mockMvc.perform(
            get("/test/context")
                .header("X-User-Id", "user-123")
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void missingJwtStillAllowsTenantButUserContextFailsInController() throws Exception{
        mockMvc.perform(
            get("/test/context")
                .header("X-Tenant-Id", "community")
        )
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("error").value("no user in Context"));
    }
}
