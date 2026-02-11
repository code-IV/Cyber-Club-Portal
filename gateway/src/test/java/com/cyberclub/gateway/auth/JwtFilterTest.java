package com.cyberclub.gateway.auth;

import com.cyberclub.gateway.config.JwtProperties;
import com.cyberclub.gateway.filters.JwtFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtFilterTest {

    private JwtFilter filter;
    private JwtProperties properties;
    private SecretKey key;

    @BeforeEach
    void setup() {
        properties = mock(JwtProperties.class);

        when(properties.secret())
                .thenReturn("very-secure-test-secret-key-1234567890");
        when(properties.issuer())
                .thenReturn("cyberclub");

        filter = new JwtFilter(properties);

        key = Keys.hmacShaKeyFor(
                properties.secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    private String generateValidToken() {
        return Jwts.builder()
                .setSubject("user-123")
                .claim("email", "user@test.com")
                .setIssuer("cyberclub")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(key)
                .compact();
    }

    @Test
    void shouldAuthenticateValidToken() throws Exception {

        String token = generateValidToken();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(request.getAttribute("authenticated")).isEqualTo(true);
        assertThat(request.getAttribute("userId")).isEqualTo("user-123");
        assertThat(request.getAttribute("email")).isEqualTo("user@test.com");
    }

    @Test
    void shouldRejectMissingToken() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(request.getAttribute("authenticated")).isEqualTo(false);
    }

    @Test
    void shouldRejectInvalidToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        request.addHeader("Authorization", "Bearer " + "token");

        filter.doFilter(request, response, chain);
        assertThat(request.getAttribute("authenticated")).isEqualTo(false);
    }
}
