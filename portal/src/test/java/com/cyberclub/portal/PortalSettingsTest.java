package com.cyberclub.portal;

import com.cyberclub.portal.dtos.AuthResult;
import com.cyberclub.portal.context.UserContext;
import com.cyberclub.portal.exceptions.ForbiddenException;
import com.cyberclub.portal.security.ClientAuth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(PortalSettingsTest.TestUserContextFilterConfig.class)
@AutoConfigureMockMvc
public class PortalSettingsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientAuth auth;

    // Dummy filter to inject UserContext for tests
    @TestConfiguration
    static class TestUserContextFilterConfig {
        @Bean
        public OncePerRequestFilter userContextFilter() {
            return new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request,
                                                HttpServletResponse response,
                                                FilterChain filterChain)
                        throws ServletException, IOException {
                    // Inject dummy admin user
                    UserContext.set("jjjj");
                    try {
                        filterChain.doFilter(request, response);
                    } finally {
                        UserContext.clear();
                    }
                }
            };
        }
    }

    private static final String BASE_PAYLOAD = """
            {
                "theme": "blue",
                "notifications_enabled": true,
                "language_code": "am"
            }
            """;

    @BeforeEach
    void setup() {
        // Ensure UserContext is cleared before each test
        UserContext.clear();
    }

    @Test
    void admin_canUpdate_settings() throws Exception {
        AuthResult admin = new AuthResult(true, "ADMIN");
        when(auth.checkMembership(any())).thenReturn(admin);

        mockMvc.perform(
                        post("/setting")
                                .header("X-Internal-Auth", "test-secret")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(BASE_PAYLOAD)
                )
                .andExpect(status().isOk());
    }

    @Test
    void user_canNotUpdate_settings() throws Exception {
        // Override UserContext to have USER role
        UserContext.set("jjj");

        AuthResult user = new AuthResult(true, "USER");
        when(auth.checkMembership(any())).thenReturn(user);

        mockMvc.perform(
                        post("/setting")
                                .header("X-Internal-Auth", "test-secret")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(BASE_PAYLOAD)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("Unauthorized Access"));
    }

    @Test
    void req_forbidden_settings() throws Exception {
        when(auth.checkMembership(any()))
                .thenThrow(new ForbiddenException("Admin access required"));

        mockMvc.perform(
                        post("/setting")
                                .header("X-Internal-Auth", "test-secret")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(BASE_PAYLOAD)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("Admin access required"));
    }

    @Test
    void req_Unavailable_settings() throws Exception {
        when(auth.checkMembership(any()))
                .thenThrow(new RuntimeException("Service UnAvailable"));

        mockMvc.perform(
                        post("/setting")
                                .header("X-Internal-Auth", "test-secret")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(BASE_PAYLOAD)
                )
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("internal server error"));
    }
}
