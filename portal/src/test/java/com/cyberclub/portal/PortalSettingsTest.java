package com.cyberclub.portal;

import com.cyberclub.portal.dtos.AuthResult;
import com.cyberclub.portal.exceptions.ForbiddenException;
import com.cyberclub.portal.security.ClientAuth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class PortalSettingsTest extends BaseIntegrationTest {
    private @Value("${INTERNAL_GATEWAY_SECRET}") String secret;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientAuth auth;

    private static final String BASE_PAYLOAD = """
            {
                "theme": "blue",
                "notifications_enabled": true,
                "language_code": "am"
            }
            """;

    @Test
    void admin_canUpdate_settings() throws Exception {
        AuthResult admin = new AuthResult(true, "ADMIN");
        when(auth.checkMembership(any())).thenReturn(admin);

        mockMvc.perform(
                        post("/setting")
                                .header("X-Internal-Auth", secret)
                                .header("X-User-Id", "11111111-1111-1111-1111-111111111111")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(BASE_PAYLOAD)
                )
                .andExpect(status().isOk());
    }

    @Test
    void user_canNotUpdate_settings() throws Exception {

        AuthResult user = new AuthResult(true, "USER");
        when(auth.checkMembership(any())).thenReturn(user);

        mockMvc.perform(
                        post("/setting")
                                .header("X-Internal-Auth", secret)
                                .header("X-User-Id", "11111111-1111-1111-1111-111111111111")
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
                                .header("X-Internal-Auth", secret)
                                .header("X-User-Id", "11111111-1111-1111-1111-111111111111")
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
                                .header("X-Internal-Auth", secret)
                                .header("X-User-Id", "11111111-1111-1111-1111-111111111111")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(BASE_PAYLOAD)
                )
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("internal server error"));
    }
}
