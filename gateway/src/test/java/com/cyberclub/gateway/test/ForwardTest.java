package com.cyberclub.gateway.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ForwardTest{

    @Autowired
    MockMvc mvc;

    @Test
    void forwardsHeaders() throws Exception {
        mvc.perform(
                get("/forwardtest")
                        .header("Host", "tenantA.example.com")
                        .header("Authorization", "Bearer user-123")
        )
        .andExpect(status().isOk())
        .andExpect(header().exists("X-Tenant-Id"))
        .andExpect(header().exists("X-Request-Id"))
        .andExpect(header().exists("X-User-Id"));
    }
}
