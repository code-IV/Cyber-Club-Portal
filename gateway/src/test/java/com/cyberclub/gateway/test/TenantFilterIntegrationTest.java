package com.cyberclub.gateway.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TenantFilterIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Test
    void rejectsInvalidTenant() throws Exception {
        mvc.perform(
                get("/anything")
                        .header("Host", "bad!!.example.com")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void invalidTenant() throws Exception {
        mvc.perform(
                get("/anything")
                        .header("Host", "example.com")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void allowsValidTenant() throws Exception {
        mvc.perform(
                get("/anything")
                        .header("Host", "tenantA.example.com")
        ).andExpect(status().isOk());
    }
}


